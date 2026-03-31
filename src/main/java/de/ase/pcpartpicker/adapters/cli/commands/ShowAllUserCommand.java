package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.ListConfiguration;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class ShowAllUserCommand implements ICommand {

    private final ListConfiguration listConfiguration;
    private InputReader inputReader;

    public ShowAllUserCommand(InputReader inputReader, ListConfiguration listConfiguration) {
        this.inputReader = inputReader;
        this.listConfiguration = listConfiguration;
    }


    @Override
    public void execute() {
        ShowListCommand<User> showList = new ShowListCommand<>(listConfiguration.user(), inputReader);  
        showList.execute();
    }


    
}
