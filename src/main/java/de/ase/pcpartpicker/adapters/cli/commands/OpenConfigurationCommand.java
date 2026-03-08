package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.MenuFactory;

public class OpenConfigurationCommand implements ICommand {
    private final MenuFactory menuFactory;
    
    public OpenConfigurationCommand(MenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }
    
    @Override
    public void execute() {
        System.out.println("To be done"); 
        // menuFactory.createConfigurationMenu().show();
    }

}