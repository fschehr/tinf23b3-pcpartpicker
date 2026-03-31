package de.ase.pcpartpicker.adapters.cli.commands;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.adapters.cli.AppContext;

public class StartComputerDraftCommand implements ICommand {
    private final AppContext context; 
    private final Menu configuratorMenu; 

    public StartComputerDraftCommand(AppContext context, Menu configuratorMenu) {
        this.context = context; 
        this.configuratorMenu = configuratorMenu;
    }

    @Override
    public void execute() {
        if(!SessionManager.isLoggedIn()) {
            User standardUser = context.userRepository.findById(1); 

            System.out.println("Du bist momentan nicht eingeloggt möchtest du mit dem Standard User fortfahren?");
            int input = context.inputReader.readInt("Drücke 1 um mit dem Standard User fortzufahren. Drücke 0 um zurückzukehren", 0, 1);

            if (input == 1) {
                SessionManager.setCurrentUser(standardUser);
                context.computerDraft.startNewDraft();
                configuratorMenu.execute();
            }
        }
        else {
            context.computerDraft.startNewDraft();
            configuratorMenu.execute();
        }
        
    }
}