package de.ase.pcpartpicker.adapters.cli.commands;
import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class StartComputerDraftCommand implements ICommand {

    /** 
     * Prüft ob die Vorrausetzungen um den Draft zu starten erfüllt sind
     * wenn ja starte den Draft
     * wenn nein zeige eine Fehlermeldung
    */
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