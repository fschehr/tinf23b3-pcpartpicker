package de.ase.pcpartpicker.adapters.cli;

public class ConsoleMenu {
    private final InputReader reader = new InputReader();
    private final ComponentSelectionMenu selectionMenu = new ComponentSelectionMenu(reader);
    private boolean running = true; 


    public void start() {
        while(running) {
            System.out.println("\n--- PC Part Picker Hauptmenü ---");
            System.out.println("1. Komponenten auswählen");
            System.out.println("2. Konfiguration prüfen");
            System.out.println("0. Beenden");

            int choice = reader.readInt("Gewählt", 0, 2);

            //hier wird der moderne Standard eines Switch Case verwendet mit "->" statt "break"
            switch (choice) {
                case 1 -> selectionMenu.start();
                case 2 -> System.out.println("Wechsle zu Prüfung...");
                case 0 -> {
                    System.out.println("Programm beendet.");
                    running = false; 
                }
            }
        }
    }
}
