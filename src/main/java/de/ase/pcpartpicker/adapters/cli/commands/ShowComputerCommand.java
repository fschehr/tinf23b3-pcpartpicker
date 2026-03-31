package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.utils.TableUtils;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowComputerCommand implements ICommand{
    
    private final AppContext context; 
    private boolean showAll; 
    private int userID; 

    public ShowComputerCommand(AppContext context, boolean showAll) {
        this.context = context;
        this.showAll = showAll; 
    }

    //wenn man nur 
    public ShowComputerCommand(AppContext context, int userID) {
        this.context = context;
        this.userID= userID;
    }

    @Override
    public void execute() {
        if (userID > 0) { // userID wurde gesetzt
            List<Computer> computers = context.computerRepository.findAllByUserId(userID);
            if (computers.isEmpty()) {
                System.out.println("Dieser Nutzer hat noch keine Computer angelegt.");
            } else {
                for (Computer computer : computers) {
                    TableGenerator table = new TableGenerator(new String[] {"Komponente", "Eigenschaften", "Details"});
                    for (String[] row : TableUtils.getComputerAsTableRows(computer)) {
                        table.addRow(row);
                    }
                    table.printTable();
                    System.out.println("--------------------------------------------------");
                }
            }
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
            return;
        }

        if(!showAll) {
            // Zeige alle Computer des akutellen Users 
            try {
                
                User currentUser= SessionManager.getcurrentUser();
                int userID = currentUser.getId();
                List<Computer> computers = context.computerRepository.findAllByUserId(userID);
    
                if(computers.isEmpty()) {
                    System.out.println("Du hast noch keine Computer angelegt.");
                }
                else {
                    for(Computer computer: computers) {
                        TableGenerator table = new TableGenerator(new String[] {"Komponente", "Eigenschaten", "Details"}); 
                        for(String[] row: TableUtils.getComputerAsTableRows(computer)) {
                            table.addRow(row);
                        }
                        table.printTable();
                        System.out.println("--------------------------------------------------");
                    }
                }
            } catch (Exception e) {
                System.out.println("Keine Computer verfügbar, da du momentan nicht eingeloggt bist."); 
            }
        }
        else {
            List<Computer> computers = context.computerRepository.findAll();
            
            if(computers.isEmpty()) {
                System.out.println("Es sind keine Computer verfügbar.");
            } else {
                for (Computer computer: computers) {
                    int cid = computer.getId();
                    String userName = getUserName(cid); 
                    System.out.println(userName); 
                    TableGenerator table = new TableGenerator(new String[] {"Komponente", "Eigenschaten", "Details"}); 
                    for(String[] row: TableUtils.getComputerAsTableRows(computer)){
                        table.addRow(row); 
                    }
                    table.printTable();
                    System.out.println("--------------------------------------------------");
                }
            }

        }
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
    }


    public String getUserName(int computerID) {
        User user = context.userRepository.findByComputerId(computerID);
        return user.getName();
    }
}

