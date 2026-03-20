package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComponentConfigs;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;

public class ShowAllUserCommand implements ICommand {
    

    private final ConnectionFactory connectionFactory;
    private final ComponentConfigs componentConfigs;
    private InputReader inputReader;

    public ShowAllUserCommand(ConnectionFactory connectionFactory, InputReader inputReader, ComponentConfigs componentConfigs) {
        this.connectionFactory = connectionFactory; 
        this.inputReader = inputReader;
        this.componentConfigs = componentConfigs;
    }


    @Override
    public void execute() {
        ShowListCommand showList = new ShowListCommand<>(componentConfigs.user(), inputReader);  
        showList.execute();
    }


    
}
