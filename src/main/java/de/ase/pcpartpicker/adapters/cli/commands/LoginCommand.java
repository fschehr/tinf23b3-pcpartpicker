package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
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
            System.out.println("Willkommen zurück, " + foundUser.getName()); 
            SessionManager.setCurrentUser(foundUser); 
        }
        else{
            System.out.println("Nutzername nicht gefunden."); 
        }

    }   
}
