package de.ase.pcpartpicker.adapters.cli;

import java.util.List;
import java.util.Locale;

import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.MainboardRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.RAM;

public class ComponentSelectionMenu {
    
    private final InputReader reader;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final RamRepository ramRepository;
    private final MainboardRepository mainboardRepository;

    public ComponentSelectionMenu(
        InputReader reader,
        CpuRepository cpuRepository,
        GpuRepository gpuRepository,
        RamRepository ramRepository,
        MainboardRepository mainboardRepository
    ) {
        this.reader = reader;
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.ramRepository = ramRepository;
        this.mainboardRepository = mainboardRepository;
    }

    public void start() {
        boolean back = false; 
        while (!back) {
            System.out.println("\n--- Komponenten Auswahl ---");
            System.out.println("1. CPU auswählen");
            System.out.println("2. GPU auswählen");
            System.out.println("3. RAM auswählen");
            System.out.println("4. Mainboard auswählen");
            System.out.println("0. Zurück zum Hauptmenü");
        

            int choice = reader.readInt("Wählen Sie eine Kategorie", 0, 4);

            switch (choice) {
                case 1 -> showCpuList();
                case 2 -> showGpuList();
                case 3 -> showRamList();
                case 4 -> showMainboardList();
                case 0 -> back = true;
            }
        }
    }



    public void showCpuList() {
        List<CPU> cpus = cpuRepository.findAll();
        if (cpus.isEmpty()) {
            System.out.println("\nKeine CPUs in der Datenbank gefunden.");
            return;
        }

        TableGenerator table = new TableGenerator("ID", "Name", "Sockel", "Preis");

        for (CPU cpu : cpus) {
            table.addRow(
                String.valueOf(cpu.getId()),
                cpu.getName(),
                cpu.getSocket().getName(),
                String.format(Locale.GERMANY, "%.2f€", cpu.getPrice())
            );
        }
        
        System.out.println("\n--- Verfügbare Prozessoren ---");
        table.printTable();
        String input = reader.readString("Geben Sie die ID der gewünschten CPU ein (oder '0' zum Abbrechen)");

        if(!input.equals("0")) {
            CPU selected = null; 
            for(CPU cpu: cpus) {
                if(String.valueOf(cpu.getId()).equals(input)) {
                    selected = cpu; 
                    break;
                }
            }

            if(selected != null) {
                config.addComponent(selected);
                System.out.println(">> " + selected.getName() + " wurde ihrer Konfiguration hinzugefügt!");
            }
            else {
                System.out.println(">> Fehler: ID '" + input + "' nicht gefunden.");
            }
        }


    }

    public void showGpuList() {
        List<GPU> gpus = gpuRepository.findAll();
        if (gpus.isEmpty()) {
            System.out.println("\nKeine GPUs in der Datenbank gefunden.");
            return;
        }

        TableGenerator table = new TableGenerator("ID", "Name", "VRAM (GB)", "Preis");

        for (GPU gpu : gpus) {
            table.addRow(
                String.valueOf(gpu.getId()),
                gpu.getName(),
                String.valueOf(gpu.getVramGB()),
                String.format(Locale.GERMANY, "%.2f€", gpu.getPrice())
            );
        }

        System.out.println("\nVerfügbare Grafikkarten:");
        table.printTable();
    }

    public void showRamList() {
        List<RAM> ramModules = ramRepository.findAll();
        if (ramModules.isEmpty()) {
            System.out.println("\nKein RAM in der Datenbank gefunden.");
            return;
        }

        TableGenerator table = new TableGenerator("ID", "Name", "Kapazität", "Geschwindigkeit", "Preis");

        for (RAM ram : ramModules) {
            table.addRow(
                String.valueOf(ram.getId()),
                ram.getName(),
                ram.getCapacityGB() + " GB",
                ram.getSpeedMHz() + " MHz",
                String.format(Locale.GERMANY, "%.2f€", ram.getPrice())
            );
        }

        System.out.println("\nVerfügbarer Arbeitsspeicher:");
        table.printTable();
    }

    public void showMainboardList() {
        List<Mainboard> mainboards = mainboardRepository.findAll();
        if (mainboards.isEmpty()) {
            System.out.println("\nKeine Mainboards in der Datenbank gefunden.");
            return;
        }

        TableGenerator table = new TableGenerator("ID", "Name", "Sockel", "Formfaktor", "Preis");

        for (Mainboard mainboard : mainboards) {
            table.addRow(
                String.valueOf(mainboard.getId()),
                mainboard.getName(),
                mainboard.getSocket().getName(),
                mainboard.getFormFactor().getName(),
                String.format(Locale.GERMANY, "%.2f€", mainboard.getPrice())
            );
        }

        System.out.println("\nVerfügbare Mainboards:");
        table.printTable();
    }
}
