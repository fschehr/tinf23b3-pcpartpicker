package de.ase.pcpartpicker.adapters.cli.commands;


import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.part_assembly.Computer;


public class CreateComputerCommand implements ICommand {
    
    private final InputReader inputReader; 
    private final ComputerRepository computerRepository;
    private Computer computer; 

    public CreateComputerCommand(InputReader inputReader, ComputerRepository computerRepository ) {
        this.inputReader = inputReader;
        this.computerRepository = computerRepository; 
    }

    @Override
    public void execute() {
        boolean loggedIn = SessionManager.isLoggedIn();
        if(loggedIn) {
            
        }
        else {
            System.out.println("Sie sind nich eingeloggt. Bitte melden Sie sich an um einen neuen Computer anzulegen."); 
        }


        inputReader.waitForEnter("Enter drücken um zurückzukehren");
    }


}
