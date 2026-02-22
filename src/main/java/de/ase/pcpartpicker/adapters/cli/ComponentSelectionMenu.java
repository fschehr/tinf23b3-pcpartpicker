package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.domain.models.CPU;
import de.ase.pcpartpicker.domain.models.PCConfiguration;
import de.ase.pcpartpicker.domain.services.ComponentService;
import java.util.List;

public class ComponentSelectionMenu {
    
    private final InputReader reader;
    private final ComponentService service; 
    private final PCConfiguration config;

    public ComponentSelectionMenu(InputReader reader, ComponentService service, PCConfiguration config) {
        this.reader = reader;
        this.service = service; 
        this.config = config; 
    }

    public void start() {
        boolean back = false; 
        while (!back) {
            System.out.println("\n--- Komponenten Auswahl ---");
            System.out.println("1. CPU auswählen");
            System.out.println("2. GPU auswählen");
            System.out.println("3. RAM auswählen");
            System.out.println("0. Zurück zum Hauptmenü");
        

            int choice = reader.readInt("Wählen Sie eine Kategorie", 0, 3);

            switch (choice) {
                case 1 -> showCpuList();
                case 2 -> System.out.println("[Logik für GPU folgt...]");
                case 3 -> System.out.println("[Logik für RAM folgt...]");
                case 0 -> back = true;
            }
        }
    }


    public void showCpuList() {
        TableGenerator table = new TableGenerator("ID", "Name", "Sockel", "Kerne", "Preis"); 


        List<CPU> cpus = service.getCPUs(); 

        for(CPU cpu: cpus) {
            table.addRow(
                cpu.getId(),
                cpu.getName(),
                cpu.getSocket(),
                String.valueOf(cpu.getCores()),
                String.format("%.2f€", cpu.getPriceInEuro())
            );
        }
        
        System.out.println("\n--- Verfügbare Prozessoren ---");
        table.printTable();
        String input = reader.readString("Geben Sie die ID der gewünschten CPU ein (oder '0' zum Abbrechen)");

        if(!input.equals("0")) {
            CPU selected = null; 
            for(CPU cpu: cpus) {
                if(cpu.getId().equalsIgnoreCase(input)) {
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


}
