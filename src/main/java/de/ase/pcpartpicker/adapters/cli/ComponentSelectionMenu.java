package de.ase.pcpartpicker.adapters.cli;

public class ComponentSelectionMenu {
    
    private final InputReader reader;

    public ComponentSelectionMenu(InputReader reader) {
        this.reader = reader;
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



    // nur temporär, um später das ganze durch eine Datenbankanbindung zu ersetzen
    public void showCpuList() {
        TableGenerator table = new TableGenerator("ID", "Name", "Sockel", "Preis");
        
        table.addRow("1", "Intel Core i9-13900K", "LGA1700", "589.00€");
        table.addRow("2", "AMD Ryzen 9 7950X", "AM5", "545.00€");
        table.addRow("3", "Intel Core i5-13600K", "LGA1700", "319.00€");
        
        System.out.println("\nVerfügbare Prozessoren:");
        table.printTable();
    }
}
