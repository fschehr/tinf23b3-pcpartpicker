package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.PagingInput;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.adapters.cli.utils.UIUtils;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowComputerCommand implements ICommand {

    public enum Mode {OWN, ALL, USER}

    private final AppContext context;
    private final Mode mode;
    private final Integer userID;

    public ShowComputerCommand(AppContext context, Mode mode) {
        this.context = context;
        this.mode = mode;
        this.userID = null;
    }

    public ShowComputerCommand(AppContext context, int userID) {
        this.context = context;
        this.mode = Mode.USER;
        this.userID = userID;
    }


    @Override
    public void execute() {
        List<Computer> computers = loadComputers();

        if (!SessionManager.isLoggedIn()) {
            System.out.println("Du bist nicht eingeloggt!");
        }

        if (computers.isEmpty()) {
            System.out.println(mode == Mode.OWN
                    ? "Du hast nich keine Computer angelegt."
                    : "Es sind keine Computer verfügbar.");
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
            return;
        }

        int currentPage = 0;
        int totalPages = computers.size();

        while (true) {
            NavigationUtils.clear();

            Computer computer = computers.get(currentPage);
//            System.out.println("Computer " + (currentPage + 1) + " von " + totalPages);

            if (mode == Mode.ALL) {
                User owner = context.userRepository.findByComputerId(computer.getId());
                if (owner != null) {
                    System.out.println("Besitzer: " + owner.getName());
                }
            }

            TableGenerator table = new TableGenerator(new String[]{"Komponente", "Eigenschaften", "Details"});
            for (String[] row : TableUtils.getComputerAsTableRows(computer)) {
                table.addRow(row);
            }
            table.printTable();


            System.out.println("\nSeite " + (currentPage + 1) + " von " + totalPages
                    + " | m = nächste Seite | n = vorherige Seite | 0 = zurück");

            String input = context.inputReader
                    .readString("ID oder Aktion (m/n/0)")
                    .trim().toLowerCase();

            PagingInput.Action action = PagingInput.parse(input);

            if (action == PagingInput.Action.BACK) {
                System.out.println("-> Auswahl abgebrochen");
                return;
            }

            currentPage = PagingInput.movePage(currentPage, totalPages, action);

        }


    }

    private List<Computer> loadComputers() {
        return switch (mode) {
            case ALL -> context.computerRepository.findAll();
            case OWN -> {
                if (!SessionManager.isLoggedIn()) yield List.of();
                yield context.computerRepository.findAllByUserId(SessionManager.getcurrentUser().getId());
            }
            // TODO: nullPointer Exception behandeln
            case USER -> context.computerRepository.findAllByUserId(userID);
        };
    }
}