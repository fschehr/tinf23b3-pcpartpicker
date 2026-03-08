package de.ase.pcpartpicker.adapters.cli.commands;

public class ExitCommand implements ICommand {
    private final Runnable onExit;
    
    public ExitCommand(Runnable onExit) {
        this.onExit = onExit;
    }
    
    @Override
    public void execute() {
        System.out.println("Auf Wiedersehen!");
        onExit.run();
    }

}