package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.cli.commands.BackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ExitCommand;

/**
 * Klasse, die Hilfsmethoden enhält
 * @link {@link #clear()} Bereinigt die CLI
 * @author Henri
 */
public class UIUtils {
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

}
