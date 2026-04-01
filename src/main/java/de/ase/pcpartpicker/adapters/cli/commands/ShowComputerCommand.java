package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.PagingInput;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowComputerCommand implements ICommand {

    public enum Mode {OWN, ALL, USER}

    private final AppContext context;
    private final Menu configuratorMenu;
    private final Mode mode;
    private final Integer userID;

    public ShowComputerCommand(AppContext context, Menu configuratorMenu, Mode mode) {
        this.context = context;
        this.configuratorMenu = configuratorMenu;
        this.mode = mode;
        this.userID = null;
    }

    public ShowComputerCommand(AppContext context, Menu configuratorMenu, int userID) {
        this.context = context;
        this.configuratorMenu = configuratorMenu;
        this.mode = Mode.USER;
        this.userID = userID;
    }


    @Override
    public void execute() {

        if(mode == Mode.OWN && !SessionManager.isLoggedIn()) {
            showInfo("Du musst eingeloggt sein, um deine Computer zu sehen.");
            return;
        }

        List<Computer> computers;
        try {
            computers = loadComputers();
        } catch (Exception e) {


            return;
        }

        if (computers == null || computers.isEmpty()) {
            showInfo(getEmptyMessageForMode());
            return;
        }

        int currentPage = 0;
        int totalPages = computers.size();

        while (true) {
            NavigationUtils.clear();

            Computer computer = computers.get(currentPage);

            if (mode == Mode.ALL) {
                User owner = context.userRepository.findByComputerId(computer.getId());
                if (owner != null) {
                    System.out.println("Besitzer: " + owner.getName());
                }
            }

            TableGenerator table = new TableGenerator("Komponente", "Eigenschaften", "Details");
            for (String[] row : TableUtils.getComputerAsTableRows(computer)) {
                table.addRow(row);
            }
            table.printTable();


                System.out.println("\nSeite " + (currentPage + 1) + " von " + totalPages
                    + " | m = nächste Seite | n = vorherige Seite | e = bearbeiten | 0 = zurück");

            String input = context.inputReader
                    .readString("Aktion (m/n/e/0)")
                    .trim().toLowerCase();

            PagingInput.Action action = PagingInput.parse(input);

            if (action == PagingInput.Action.BACK) {
                return;
            }

            if (action == PagingInput.Action.EDIT) {
                if (!SessionManager.isLoggedIn()) {
                    showInfo("Du musst eingeloggt sein, um einen Computer zu bearbeiten.");
                    continue;
                }

                int currentUserId = SessionManager.getcurrentUser().getId();
                int ownerId = context.userRepository.findUserIdByComputerId(computer.getId());
                if (ownerId != currentUserId) {
                    showInfo("Nur eigene Computer dürfen bearbeitet werden.");
                    continue;
                }

                context.computerDraft.editDraft(computer);
                configuratorMenu.execute();

                computers = loadComputers();
                if (computers == null || computers.isEmpty()) {
                    showInfo(getEmptyMessageForMode());
                    return;
                }
                totalPages = computers.size();
                currentPage = Math.min(currentPage, totalPages - 1);
                continue;
            }


            currentPage = PagingInput.movePage(currentPage, totalPages, action);

        }


    }

    private List<Computer> loadComputers() {
        return switch (mode) {
            case ALL -> context.computerRepository.findAll();
            case OWN -> context.computerRepository.findAllByUserId(SessionManager.getcurrentUser().getId());
            case USER -> {
                if (userID == null || userID <= 0) {
                    yield List.of();
                }
                yield context.computerRepository.findAllByUserId(userID);
            }
        };
    }

    private String getEmptyMessageForMode() {
        return switch (mode) {
            case OWN -> "Du hast noch keine Computer angelegt.";
            case USER -> "Dieser Nutzer hat noch keine Computer angelegt.";
            case ALL -> "Es sind keine Computer verfügbar.";
        };
    }


    private void showInfo(String message) {
        System.out.println(ColorConstants.BLUE("INFO") + " | " + message);
        context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
    }

}