package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;


public class NewUserCommand implements ICommand{

    private final InputReader inputReader; 
    private final UserRepository userRepository; 

    public NewUserCommand(InputReader inputReader, UserRepository userRepository) {
        this.inputReader = inputReader;
        this.userRepository = userRepository;
    }

    @Override
    public void execute() {
        String username = inputReader.readString("Bitte Nutzernamen eingeben");
        boolean exists = userRepository.findAll().stream()
            .anyMatch(user -> user.getName().equalsIgnoreCase(username));

        if (exists) {
            System.out.println("Nutzername existiert bereits!");
        } else {
            userRepository.save(username);
            System.out.println("Nutzer erfolgreich angelegt!");
        }

        inputReader.waitForEnter("\nEnter drücken zum fortfahren...");

    }
    
    

}
