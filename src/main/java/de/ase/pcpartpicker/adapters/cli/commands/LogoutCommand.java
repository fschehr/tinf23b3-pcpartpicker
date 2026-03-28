package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.SessionManager;

public class LogoutCommand implements ICommand {
    
    @Override
    public void execute() {
        SessionManager.setCurrentUser(null);
    }
}
