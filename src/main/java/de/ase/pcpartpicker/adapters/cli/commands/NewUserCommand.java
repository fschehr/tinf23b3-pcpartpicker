package de.ase.pcpartpicker.adapters.cli.commands;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
public class NewUserCommand implements ICommand{

    private final InputReader inputReader; 
    private final UserRepository userRepository; 

    public NewUserCommand(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        String username = inputReader.readString("Bitte Nutzernamen eingeben:");
        save()

    }
    
    

}
