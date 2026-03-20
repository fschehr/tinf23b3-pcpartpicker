package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;


public class NewUserCommand implements ICommand{

    private final InputReader inputReader; 
    private final ConnectionFactory connectionFactory;

    public NewUserCommand(InputReader inputReader, ConnectionFactory connectionFactory) {
        this.inputReader = inputReader;
        this.connectionFactory = connectionFactory; 
    }

    @Override
    public void execute() {
        String username = inputReader.readString("Bitte Nutzernamen eingeben:");
        UserRepository userRepository = new UserRepository(connectionFactory); 
        userRepository.save(username); 
    }
    
    

}
