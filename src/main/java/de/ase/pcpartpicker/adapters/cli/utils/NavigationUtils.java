package de.ase.pcpartpicker.adapters.cli.utils;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.commands.BackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ConfirmBackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ExitCommand;

public class NavigationUtils {


    private NavigationUtils() {

    }

    
    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {}
    }


    public static void addBackNavigation(Menu menu) {
        menu.setZeroComponent(new BackCommand(() -> menu.setRunning(false)));
    }

    public static void addExitNavigation(Menu menu) {
        menu.setZeroComponent(new ExitCommand(()-> System.exit(0))); 
    }



    public static void addConfiguratorBackNavigation(Menu menu, InputReader inputReader, ComputerDraft draft) {
        menu.setZeroComponent(new ConfirmBackCommand(inputReader, draft, menu));
    }
    

}
