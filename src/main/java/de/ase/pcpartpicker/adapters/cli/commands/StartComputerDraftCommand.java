package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.Menu;
// ... imports ...

public class StartComputerDraftCommand implements ICommand {
    private final ComputerDraft draft;
    private final Menu configuratorMenu; 

    public StartComputerDraftCommand(ComputerDraft draft, Menu configuratorMenu) {
        this.draft = draft;
        this.configuratorMenu = configuratorMenu;
    }

    @Override
    public void execute() {
   
        draft.startNewDraft();
        
        // 2. Wir öffnen das Konfigurator-Untermenü
        configuratorMenu.execute(); 
    }
}