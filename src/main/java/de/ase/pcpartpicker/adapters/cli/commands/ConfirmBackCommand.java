package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.IMenuComponent;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.ColorConstants;

public class ConfirmBackCommand implements ICommand, IMenuComponent {
    private final String warningMessage;
    private final InputReader inputReader;
    private final Runnable onConfirm;
    
    public ConfirmBackCommand(String warningMessage, InputReader inputReader, Runnable onConfirm) {
        this.warningMessage = warningMessage;
        this.inputReader = inputReader;
        this.onConfirm = onConfirm;
    }
    
    @Override
    public void execute() {
        System.out.println("\n" + ColorConstants.YELLOW("WARNUNG") + " | " + warningMessage);
        int choice = inputReader.readInt("Willst du wirklich zurückgehen? [0: Nein (bleiben), 1: Ja (verlassen)]", 0, 1);
        
        if (choice == 1) {
            onConfirm.run();
        }
    }

    @Override
    public String getTitle() {
        return "Zurück";
    }
}