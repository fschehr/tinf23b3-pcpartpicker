package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.IMenuComponent;

public class BackCommand implements ICommand, IMenuComponent {
    private final Runnable onBack;
    
    public BackCommand(Runnable onBack) {
        this.onBack = onBack;
    }
    
    @Override
    public void execute() {
        onBack.run();
    }

    @Override
    public String getTitle() {
        return "Zurück";
    }
    
}