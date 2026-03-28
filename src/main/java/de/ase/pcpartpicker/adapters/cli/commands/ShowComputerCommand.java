package de.ase.pcpartpicker.adapters.cli.commands;


import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.TableUtils;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowComputerCommand implements ICommand{
    
    private final InputReader inputReader; 
    private final ComputerRepository computerRepository;
    private final TableUtils tableUtils = new TableUtils(); 
    private boolean showAll; 

    public ShowComputerCommand(InputReader inputReader, ComputerRepository computerRepository, boolean showAll) {
        this.inputReader = inputReader; 
        this.computerRepository = computerRepository;
        this.showAll = showAll; 
    }

    @Override
    public void execute() {

        if(!showAll) {
            // Zeige alle Computer des akutellen Users 
            try {
                
                User currentUser= SessionManager.getcurrentUser();
                int userID = currentUser.getId();
                List<Computer> computers = computerRepository.findAllByUserId(userID);
    
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
            List<Computer> computers = computerRepository.findAll();
            
            if(computers.isEmpty()) {
                System.out.println("Es sind keine Computer verfügbar.");
            } else {
                for (Computer computer: computers) {
                    int cid = computer.getId();
                    String userName = tableUtils.getUserName(cid); 
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
            inputReader.waitForEnter("Enter drücken um zurückzukehren...");
    }

}

