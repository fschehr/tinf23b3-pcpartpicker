package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.IMenuComponent;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;

public class ConfirmBackCommand implements ICommand, IMenuComponent {
    private final InputReader inputReader;
    private final ComputerDraft draft;
    private final Menu menu;

    public ConfirmBackCommand(InputReader inputReader, ComputerDraft draft, Menu menu) {
        this.inputReader = inputReader;
        this.draft = draft;
        this.menu = menu;
    }
    
    @Override
    public void execute() {
        if (draft.hasUnsavedChanges()) {
            // Es gibt ungespeicherte Änderungen
            System.out.println(ColorConstants.YELLOW("WARNUNG") + " | Wenn du zurückgehst, gehen ungespeicherte Änderungen am Entwurf verloren.");
            int confirm = inputReader.readInt("Willst du wirklich zurück? [0: Nein, 1: Ja]", 0, 1);
            if (confirm == 1) {
                draft.clear();
                menu.setRunning(false);
            }
        } else {
    
            menu.setRunning(false);
        }
    }

    @Override
    public String getTitle() {
        return "Zurück";
    }
}