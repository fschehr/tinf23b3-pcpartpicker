package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.Menu;

public class OpenComponentSelectionCommand implements ICommand {
    private final Menu componentMenu; 
    
    public OpenComponentSelectionCommand(Menu componentMenu) {
        this.componentMenu = componentMenu;
    }
    
    @Override
    public void execute() {
        componentMenu.execute();
    }

}