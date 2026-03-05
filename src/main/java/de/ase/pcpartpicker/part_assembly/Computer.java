package de.ase.pcpartpicker.part_assembly;

import java.util.HashMap;

import de.ase.pcpartpicker.domain.*;

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
    private final PSU psu;

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
        this.psu = builder.psu;
    }

    public void printConfiguration() {
        System.out.println("Computer-Konfiguration:");
        System.out.println("Gehäuse: " + computerCase.getName());
        System.out.println("CPU: " + cpu.getName());
        System.out.println("GPU: " + (gpu != null ? gpu.getName() : "Keine dedizierte Grafikkarte"));
        System.out.println("Mainboard: " + mainboard.getName());
        System.out.println("RAM: " + ram.getName());
        System.out.println("Netzteil: " + psu.getName());
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
        return totalPrice;
    }

    // public double getTotalPowerConsumption() {
    //     double totalPower = 0;
    // }



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
        private PSU psu;

        public Builder setComputerCase(Case computerCase) {
            this.computerCase = computerCase;
            return this;
        }

        public Builder setCpu(CPU cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder setGpu(GPU gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder setMainboard(Mainboard mainboard) {
            this.mainboard = mainboard;
            return this;
        }

        public Builder setRam(RAM ram) {
            this.ram = ram;
            return this;
        }

        public Builder setPsu(PSU psu) {
            this.psu = psu;
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
//     .setCpu(cpu)
//     .setGpu(gpu)
//     .setMainboard(mainboard)
//     .setRam(ram)
//     .setPsu(psu)
//     .setComputerCase(computerCase)
//     .build();

// ein Konstruktor mit so vielen Elementen wäre unübersichtlich, daher der Builder Pattern.