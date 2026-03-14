package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.cli.commands.ICommand;

public class MenuItem implements IMenuComponent {
    private final String title;
    private final ICommand command;
    
    public MenuItem(String title, ICommand command) {
        this.title = title;
        this.command = command;
    }
    
    @Override
    public void execute() {
        command.execute();
    }
    
    @Override
    public String getTitle() {
        return title;
    }
}