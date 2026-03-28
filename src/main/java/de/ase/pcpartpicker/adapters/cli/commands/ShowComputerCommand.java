package de.ase.pcpartpicker.adapters.cli.commands;


import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.part_assembly.Computer;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowComputerCommand implements ICommand{
    
    private final InputReader inputReader; 
    private final ComputerRepository computerRepository;
    private boolean showAll; 

    public ShowComputerCommand(InputReader inputReader, ComputerRepository computerRepository, boolean showAll) {
        this.inputReader = inputReader; 
        this.computerRepository = computerRepository;
        this.showAll = showAll; 
    }

    @Override
    public void execute() {

        if(!showAll) {
            User currentUser= SessionManager.getcurrentUser();
            int userID = currentUser.getId();
            List<Computer> computers = computerRepository.findAllByUserId(userID);
            for(Computer computer: computers) {
                computer.printConfiguration();
                System.out.println("Gesamtpreis: " + computer.getTotalPrice() + " EUR");
                System.out.println("--------------------------------------------------");
            }
        }
        else {
            List<Computer> computers = computerRepository.findAll();
            for (Computer computer: computers) {
                computer.printConfiguration();
                System.out.println("Gesamtpreis: " + computer.getTotalPrice() + " EUR");
                System.out.println("--------------------------------------------------");
            }
        }
            inputReader.waitForEnter("Enter drücken um zurückzukehren...");
    }

}

