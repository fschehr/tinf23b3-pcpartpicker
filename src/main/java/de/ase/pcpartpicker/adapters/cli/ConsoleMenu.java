package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.MainboardRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;

public class ConsoleMenu {
    private final InputReader reader = new InputReader();
    private final ComponentSelectionMenu selectionMenu;
    private boolean running = true; 

    public ConsoleMenu(
        CpuRepository cpuRepository,
        GpuRepository gpuRepository,
        RamRepository ramRepository,
        MainboardRepository mainboardRepository
    ) {
        this.selectionMenu = new ComponentSelectionMenu(reader, cpuRepository, gpuRepository, ramRepository, mainboardRepository);
    }


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
