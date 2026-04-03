package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.MenuFactory;
import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.Paging;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.part_assembly.Computer;

public class ShowComputerCommand implements Renderable, ICommand {

    public enum Mode {OWN, ALL, USER}

    private final AppContext context;
    private final Mode mode;
    private final Integer userID;


    public ShowComputerCommand(AppContext context, Mode mode) {
        this.context = context; 
        this.mode = mode; 
        userID = null; 
    }
    public ShowComputerCommand(AppContext context, int userID) {
        this.context = context;
        this.mode = Mode.USER;
        this.userID = userID;
    }


    // TODO: Obsolet
    @Override
    public void execute() {

    }


    @Override
    public void render() {
        if (mode == Mode.OWN && !SessionManager.isLoggedIn()) {
            showInfo("Du musst eingeloggt sein, um deine Computer zu sehen.");
            return;
        }

        List<Computer> computers;
        try {
            computers = loadComputers();
        } catch (Exception e) {
            showInfo("Fehler beim Laden der Computer.");
            return;
        }

        if (computers == null || computers.isEmpty()) {
            showInfo(getEmptyMessageForMode());
            return;
        }

        Paging.pageThroughList(
            computers,
            (computer, currentPage) -> {
                // Optional: Titel
                if (mode == Mode.OWN) {
                    System.err.println("Meine Computer");
                }
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
            },
            () -> context.inputReader.readString("Aktion (m/n/e/0)"),
            true, 
            1,
            (computer) -> {
                if(!SessionManager.isLoggedIn()) {
                    showInfo("Du musst eingeloggt sein, um einen Computer zu bearbeiten.");
                    return null; // null = Liste hat sich nicht verändert
                }

                int currentUserId = SessionManager.getcurrentUser().getId();
                int ownerId = context.userRepository.findUserIdByComputerId(computer.getId());
                if (ownerId != currentUserId) {
                    showInfo("Nur eigene Computer dürfen bearbeitet werden.");
                    return null;
                }
        
                context.computerDraft.editDraft(computer);
                
                new MenuFactory(context).createConfiguratorMenu().execute();

         
                return loadComputers();
            }
        );
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