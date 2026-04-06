package de.ase.pcpartpicker.adapters.cli.commands;
import de.ase.pcpartpicker.adapters.cli.MenuFactory;


public class StartCommand implements ICommand{
    
    @Override
    public void execute() {
        MenuFactory.createApp().execute();
    }
}
