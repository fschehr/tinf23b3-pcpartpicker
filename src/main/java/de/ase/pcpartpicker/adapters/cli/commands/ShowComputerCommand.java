package de.ase.pcpartpicker.adapters.cli.commands;


import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.part_assembly.Computer;
public class ShowComputerCommand implements ICommand{
    
    private final InputReader inputReader; 
    private final ComputerRepository computerRepository;
    private static Computer computer; 
    private boolean showAll; 
    private int userID; 

    public ShowComputerCommand(InputReader inputReader, ComputerRepository computerRepository, boolean showAll) {
        this.inputReader = inputReader; 
        this.computerRepository = computerRepository;
        this.showAll = showAll; 
    }

    @Override
    public void execute() {

        if(!showAll) {
            userID = inputReader.readInt("Geben Sie eine UserID ein", 1, 99); 
            List<Computer> computers = computerRepository.findAllByUserId(userID);
            for (Computer computer: computers) {
                System.out.println(computer); 
            }

        }

    }



}
