package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.domain.models.PCConfiguration;
import de.ase.pcpartpicker.domain.services.ComponentService;

public class ConsoleMenu {
    private final InputReader reader = new InputReader();
    private final ComponentService service = new ComponentService();
    private final PCConfiguration currentConfig = new PCConfiguration();
    private final ComponentSelectionMenu selectionMenu = new ComponentSelectionMenu(reader, service, currentConfig);
    private final ConfigurationMenu configMenu = new ConfigurationMenu(currentConfig);
    private boolean running = true; 


    public void start() {
        while(running) {
            System.out.println("\n--- PC Part Picker Hauptmenü ---");
            System.out.println("1. Komponenten auswählen");
            System.out.println("2. Aktuelle Konfiguration anzeigen");
            System.out.println("0. Beenden");

            int choice = reader.readInt("Gewählt", 0, 2);

            //hier wird der moderne Standard eines Switch Case verwendet mit "->" statt "break"
            switch (choice) {
                case 1 -> selectionMenu.start();
                case 2 -> configMenu.showCurrentConfiguration();
                case 0 -> {
                    System.out.println("Programm beendet.");
                    running = false; 
                }
            }
        }
    }
}
