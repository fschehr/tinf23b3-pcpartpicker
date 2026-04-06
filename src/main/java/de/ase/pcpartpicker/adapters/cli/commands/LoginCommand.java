package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.HelperClasses.User;


public class LoginCommand implements ICommand{
    
    private final InputReader inputReader; 
    private final UserRepository userRepository;

    public LoginCommand(InputReader inputReader, UserRepository userRepository) {
        this.inputReader = inputReader; 
        this.userRepository = userRepository; 
    }

    @Override
    public void execute() {

        String username = inputReader.readString("Nutzername");

        List<User> users = userRepository.findAll();

        User foundUser = null;
        for(User user: users) {
            if(user.getName().equalsIgnoreCase(username)) {
                foundUser = user; 
                break; 
            }
        }
        if(foundUser != null) {
            ExceptionUtils.printInfo("Willkommen zurück, " + foundUser.getName() + "!"); 
            SessionManager.setCurrentUser(foundUser); 
            inputReader.waitForEnter("Enter drücken um fortzufahren.");

        }
        else{
            ExceptionUtils.printInfo("Nutzername nicht gefunden!");
            inputReader.waitForEnter("Enter drücken um zurückzukehren...");
        }

    }   
}
