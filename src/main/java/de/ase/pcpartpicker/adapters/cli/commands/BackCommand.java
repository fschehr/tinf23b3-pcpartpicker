package de.ase.pcpartpicker.adapters.cli.commands;

public class BackCommand implements ICommand {
    private final Runnable onBack;
    
    public BackCommand(Runnable onBack) {
        this.onBack = onBack;
    }
    
    @Override
    public void execute() {
        onBack.run();
    }
    
}