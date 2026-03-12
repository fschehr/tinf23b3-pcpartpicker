package de.ase.pcpartpicker.part_assembly;

import de.ase.pcpartpicker.domain.*;
import java.util.List;

/**
 * Klasse die einen vollständig konfigurierten Computer abbildet
 * Diese Klasse ist unveränderlich.
 * {@link Computer.Builder} dient zum Erstellen von Computer-Objekten
 * 
 * @author Tuluhan
 */
public class Computer {
    
    private final Case computerCase;
    private final CPU cpu;
    private final GPU gpu;
    private final Mainboard mainboard;
    private final RAM ram;
    private final int ramModule; // Anzahl der RAM-Module, z.B. 2 für 2x8GB
    private final PSU psu;
    private final List<Storage> storageDevices; // Array von Speichermedien (z.B. SSDs, HDDs)

    /**
     * Erzeugt einen Computer basierend auf den Einstellungen im Builder.
     * @param builder Der Builder, der die Komponenten enthält.
     */
    private Computer(Builder builder) {
        this.computerCase = builder.computerCase;
        this.cpu = builder.cpu;
        this.gpu = builder.gpu;
        this.mainboard = builder.mainboard;
        this.ram = builder.ram;
        this.ramModule = builder.ramModule;
        this.psu = builder.psu;
        this.storageDevices = builder.storageDevices;
    }

    public void printConfiguration() {
        System.out.println("Computer-Konfiguration:");
        System.out.println("Gehäuse: " + computerCase.getName());
        System.out.println("CPU: " + cpu.getName());
        System.out.println("GPU: " + (gpu != null ? gpu.getName() : "Keine dedizierte Grafikkarte"));
        System.out.println("Mainboard: " + mainboard.getName());
        System.out.println("RAM: " + ram.getName() + " (" + ramModule + " Module)");
        System.out.println("Netzteil: " + psu.getName());
        System.out.println("Speichermedien:");
        for (Storage storage : storageDevices) {
            System.out.println(" - " + storage.getName());
        }
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        totalPrice += computerCase.getPrice();
        totalPrice += cpu.getPrice();
        if (gpu != null) {
            totalPrice += gpu.getPrice();
        }
        totalPrice += mainboard.getPrice();
        totalPrice += ram.getPrice();
        totalPrice += psu.getPrice();
        for (Storage storage : storageDevices) {
            totalPrice += storage.getPrice();
        }
        return totalPrice;
    }

    public double getTotalPowerConsumption() {
        double totalPower = 0;
        totalPower += computerCase.getPowerConsumptionW();
        totalPower += cpu.getPowerConsumptionW();
        if (gpu != null) {
            totalPower += gpu.getPowerConsumptionW();
        }
        totalPower += mainboard.getPowerConsumptionW();
        totalPower += ram.getPowerConsumptionW();
        totalPower += psu.getPowerConsumptionW();
        for (Storage storage : storageDevices) {
            totalPower += storage.getPowerConsumptionW();
        }
        return totalPower;
    }

    /**
     * Statische Hilfsklasse zum schrittweisen Aufbau eines Computer-Objekts.
     * Ermöglicht die Validierung von Komponenten vor der Instanziierung.
     */
    public static class Builder {
        private Case computerCase;
        private CPU cpu;
        private GPU gpu;
        private Mainboard mainboard;
        private RAM ram;
        private int ramModule; // Anzahl der RAM-Module, z.B. 2 für 2x8GB
        private PSU psu;
        private List<Storage> storageDevices;

        public Builder setComputerCase(Case computerCase) {
            this.computerCase = computerCase;
            return this;
        }

        public Builder setCPU(CPU cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder setGPU(GPU gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder setMainboard(Mainboard mainboard) {
            this.mainboard = mainboard;
            return this;
        }

        public Builder setRAM(RAM ram, int anzahlModule) {
            this.ram = ram;
            this.ramModule = anzahlModule;
            return this;
        }

        public Builder setPSU(PSU psu) {
            this.psu = psu;
            return this;
        }

        /**
         * Setzt die Speichermedien für den Computer. Es können mehrere Speichermedien hinzugefügt werden, z.B. eine Kombination aus SSD und HDD.
         * @param storageDevices Ein Array von Storage-Objekten
         */
        public Builder setStorageDevices(Storage[] storageDevices) {
            this.storageDevices = List.of(storageDevices);
            return this;
        }

        // Diese Methode überprüft die Kompatibilität der Komponenten.
        // Es wird lediglich überprüft, ob der PC so gebaut werden und existieren könnte.
        // Bspw. ob das Netzteil genug Leistung hat, um die Komponenten zu versorgen, wird hier nicht überprüft.
        private boolean validate() {

            if(ram == null) {
                System.out.println("Es muss mindestens ein RAM-Modul ausgewählt werden.");
                return false;
            }

            if(cpu == null) {
                System.out.println("Es muss eine CPU ausgewählt werden.");
                return false;
            }

            if(!cpu.hasIntegratedGraphics() && gpu == null) {
                System.out.println("Warnung: Der Computer hat keine dedizierte Grafikkarte und die CPU verfügt nicht über integrierte Grafik. Es könnte zu Problemen bei der Anzeige kommen.");
            }

            if(mainboard == null) {
                System.out.println("Es muss ein Mainboard ausgewählt werden.");
                return false;
            }

            if(psu == null) {
                System.out.println("Es muss ein Netzteil ausgewählt werden.");
                return false;
            }

            if(computerCase == null) {
                System.out.println("Es muss ein Gehäuse ausgewählt werden.");
                return false;
            }

            if (!computerCase.getPSUFormFactor().equals(psu.getFormFactor())) {
                System.out.println("Die Form des Netzteils passt nicht zum Gehäuse.");
                return false;
            }

            // Ergänzen: Mainboard-Gehäuse Kompatibilität bezüglich abwärtskompatibler Formfaktoren (z.B. ATX, Micro-ATX, Mini-ITX)
            if (!computerCase.getMotherboardFormFactor().equals(mainboard.getFormFactor())) {
                System.out.println("Das Mainboard passt nicht ins Gehäuse.");
                return false;
            }            

            if(!cpu.getSocket().equals(mainboard.getSocket())) {
                System.out.println("Die CPU ist nicht mit dem Mainboard kompatibel.");
                return false;
            }

            if(ramModule > mainboard.getRamSlots()) {
                System.out.println("Das Mainboard hat nicht genug RAM-Slots für die Anzahl der RAM-Module.");
                return false;
            }

            if(storageDevices.isEmpty()) {
                System.out.println("Es muss mindestens ein Speichermedium ausgewählt werden.");
                return false;
            }

            return true;
        }

        // Hier wird der eigentliche Computer nach erfolgreicher Validierung erstellt. Ansonsten wird eine Fehlermeldung ausgegeben und null zurückgegeben.
        public Computer build() {
            if(validate())
                return new Computer(this);
            else
                System.out.println("Der Computer konnte nicht erstellt werden. Bitte prüfen sie die Kompatibilität der Komponenten.\n");
            return null;
        }
    }
}
// Weil verwirrend hier ein Beispiel wie das aufgebaut werden könnte:
// Computer computer = new Computer.Builder()
//     .setCPU(cpu)
//     .setGPU(gpu)
//     .setMainboard(mainboard)
//     .setRAM(ram, 5)
//     .setPSU(psu)
//     .setComputerCase(computerCase)
//     .setStorageDevices({ssd, hdd})
//     .build();

// ein Konstruktor mit so vielen Elementen wäre unübersichtlich, daher das Builder Pattern.