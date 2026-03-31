package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.IMenuComponent;

public class ExitCommand implements ICommand, IMenuComponent {
    private final Runnable onExit;
    
    public ExitCommand(Runnable onExit) {
        this.onExit = onExit;
    }
    
    @Override
    public void execute() {
        System.out.println("Auf Wiedersehen!");
        onExit.run();
    }

    @Override
    public String getTitle() {
        return "Beenden"; 
    }

}