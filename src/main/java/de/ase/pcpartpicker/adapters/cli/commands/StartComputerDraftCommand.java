package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class StartComputerDraftCommand implements ICommand {
    private final ComputerDraft draft;
    private final Menu configuratorMenu; 
    private final InputReader inputReader; 
    private final UserRepository userRepository; 

    public StartComputerDraftCommand(ComputerDraft draft, Menu configuratorMenu, UserRepository userRepository, InputReader inputReader) {
        this.draft = draft;
        this.configuratorMenu = configuratorMenu;
        this.inputReader = inputReader;
        this.userRepository = userRepository; 
    }

    @Override
    public void execute() {
        if(!SessionManager.isLoggedIn()) {
            User standardUser = userRepository.findById(1); 

            System.out.println("Du bist momentan nicht eingeloggt möchtest du mit dem Standard User fortfahren?");
            int input = inputReader.readInt("Drücke 1 um mit dem Standard User fortzufahren. Drücke 0 um zurückzukehren", 0, 1);

            if (input == 1) {
                SessionManager.setCurrentUser(standardUser);
            }
        }
        draft.startNewDraft();
        configuratorMenu.execute();
        
    }
}