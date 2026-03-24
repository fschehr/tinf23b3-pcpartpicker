package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.SessionManager;

public class StartComputerDraftCommand implements ICommand {
    private final ComputerDraft draft;
    private final Menu configuratorMenu; 
    private final InputReader inputReader; 

    public StartComputerDraftCommand(ComputerDraft draft, Menu configuratorMenu, InputReader inputReader) {
        this.draft = draft;
        this.configuratorMenu = configuratorMenu;
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        if(!SessionManager.isLoggedIn()) {
            System.err.println("[Fehler] Bitte logge dich ein um eine Konfiguration anlegen zu können");
            inputReader.waitForEnter("Enter drücken, um zurückzukehren...");
            return;
        }
        draft.startNewDraft();
        
        configuratorMenu.execute(); 
    }
}