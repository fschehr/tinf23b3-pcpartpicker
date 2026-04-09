package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.MenuFactory;
import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.adapters.cli.utils.Paging;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.part_assembly.Computer;

/**
 * Klasse, die für die Auswahl der Computer für den Bottleneck-Check zuständig ist 
 * @param context Klasse mit Kontext, sodass nicht zu viele Parameter an den Konstruktor übergeben werden müssen
 * @author Henri
 */

public class ComputerToCheckCommand implements Renderable {
    private final AppContext context; 


    public ComputerToCheckCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void render(String title) {

        if(!SessionManager.isLoggedIn()) {
            ExceptionUtils.printInfo("Du musst eingeloggt sein, um Bottleneck-Checks durchzuführen!"); 
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren..."); 
            return;        
        }

        User currentUser = SessionManager.getcurrentUser(); 
        int userID = currentUser.getId();

        List<Computer> computers = context.computerRepository.findFinishedByUserId(userID);

        if(computers.isEmpty()) {
            ExceptionUtils.printInfo("Du hast noch keine Computer erstellt.");
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren..."); 
            return;
        }

        Paging.builder(computers)
            .withTitle(title)
            .withPageSize(1)
            .withInputReader(() -> context.inputReader.readString("").trim().toLowerCase())
            .withRenderer((computer, currentPage) -> {


                TableGenerator table = new TableGenerator("Komponente", "Eigenschaften", "Details");
                for (String[] row : TableUtils.getComputerAsTableRows(computer)) {
                    table.addRow(row);
                }
                table.printTable();
            })
            .onSelect((Computer selectedComputer) -> {
                context.setSelectedComputer(selectedComputer);

                new MenuFactory(context).createBottleneckMenu().execute();
                return context.computerRepository.findFinishedByUserId(userID); 
            }

        )
        .start();
    }
}
