package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.Menu;

public class OpenMenuCommand implements ICommand {
    private final Menu menu;

    public OpenMenuCommand(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void execute() {
        menu.execute();
    }
}
