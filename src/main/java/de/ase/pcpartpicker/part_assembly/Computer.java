package de.ase.pcpartpicker.part_assembly;

import java.util.List;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.domain.Component;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;

/**
 * Klasse die einen vollständig konfigurierten Computer abbildet
 * Diese Klasse ist unveränderlich. <br>
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

    public Case getComputerCase() {
        return computerCase;
    }
    public CPU getCPU() {
        return cpu;
    }
    public GPU getGPU() {
        return gpu;
    }
    public Mainboard getMainboard() {
        return mainboard;
    }
    public RAM getRAM() {
        return ram;
    }
    public int getRamModule() {
        return ramModule;
    }
    public PSU getPSU() {
        return psu;
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
     * Ermöglicht die Validierung von Komponenten vor der Instanziierung. <br>
     * {@link Builder#setCPU(CPU)} dient zum Festlegen der CPU. <br>
     * {@link Builder#setGPU(GPU)} dient zum Festlegen der GPU. <br>
     * {@link Builder#setMainboard(Mainboard)} dient zum Festlegen des Mainboards. <br>
     * {@link Builder#setRAM(RAM, int)} dient zum Festlegen des RAMs und der Anzahl der Module. <br>
     * {@link Builder#setPSU(PSU)} dient zum Festlegen des Netzteils. <br>
     * {@link Builder#setComputerCase(Case)} dient zum Festlegen des Gehäuses. <br> 
     * {@link Builder#setStorageDevices(Storage[])} dient zum Festlegen der Speichermedien. Es können mehrere Speichermedien hinzugefügt werden, z.B. eine Kombination aus SSD und HDD. <br>
     * {@link Builder#build()} erstellt den Computer nach erfolgreicher Validierung. <br>
     * @author Tuluhan
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

        /**
         * Setzt das Gehäuse für den Computer. Es muss ein Gehäuse ausgewählt werden, damit der Computer erstellt werden kann.
         * @param computerCase Das Gehäuse, z.B. ein ATX-Gehäuse mit Fenster
         */
        public Builder setComputerCase(Case computerCase) {
            this.computerCase = computerCase;
            return this;
        }

        /**
         * Setzt die CPU für den Computer. Es muss eine CPU ausgewählt werden, damit der Computer erstellt werden kann.
         * @param cpu Die CPU, z.B. ein AMD Ryzen 5 5600X
         */
        public Builder setCPU(CPU cpu) {
            this.cpu = cpu;
            return this;
        }

        /**
         * Setzt die GPU für den Computer. Es ist möglich, keinen GPU zu wählen, wenn die CPU über integrierte Grafik verfügt.
         * @param gpu Die GPU, z.B. eine NVIDIA GeForce RTX 3080
         */
        public Builder setGPU(GPU gpu) {
            this.gpu = gpu;
            return this;
        }

        /**
         * Setzt das Mainboard für den Computer. Es muss ein Mainboard ausgewählt werden, damit der Computer erstellt werden kann.
         * @param mainboard Das Mainboard, z.B. ein ATX-Mainboard mit AM4-Sockel
         */
        public Builder setMainboard(Mainboard mainboard) {
            this.mainboard = mainboard;
            return this;
        }

        /**
         * Setzt den RAM und die Anzahl der Module für den Computer. Es muss mindestens ein RAM-Modul ausgewählt werden, damit der Computer erstellt werden kann.
         * @param ram Das RAM-Modul, z.B. 16GB DDR4
         * @param anzahlModule Die Anzahl der RAM-Module, z.B. 2 für 2x8GB
         */
        public Builder setRAM(RAM ram, int anzahlModule) {
            this.ram = ram;
            this.ramModule = anzahlModule;
            return this;
        }

        /**
         * Setzt das Netzteil für den Computer. Es muss ein Netzteil ausgewählt werden, damit der Computer erstellt werden kann.
         * @param psu
         */
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

        /**
         * Diese Methode soll dazu dienen die Kompatibilität von Komponenten live zu überprüfen, während der Benutzer die Komponenten auswählt.
         * @return die Warnung die ausgegeben werden soll. Wenn kein Konflikt herrscht, wird null zurückgegeben.
         * @author Tuluhan
         */
        public String validate(ComputerDraft draft, Component component) {
            
            if(component instanceof CPU) {
                if(draft.getMainboard() != null && !((CPU) component).getSocket().equals(draft.getMainboard().getSocket())) {
                    //System.out.println("LIVE: Die ausgewählte CPU ist nicht mit dem Mainboard kompatibel.");
                    return "Dieser Prozessor ist nicht mit dem Mainboard kompatibel.";
                }
            }

            if(component instanceof Mainboard) {
                if(draft.getCPU() != null && !((Mainboard) component).getSocket().equals(draft.getCPU().getSocket())) {
                    //System.out.println("LIVE: Das ausgewählte Mainboard ist nicht mit der CPU kompatibel.");
                    return "Dieses Mainboard ist nicht mit der CPU kompatibel.";
                } else if(draft.getRamModule() > ((Mainboard) component).getRamSlots()) {
                    //System.out.println("LIVE: Das ausgewählte Mainboard hat nicht genug RAM-Slots für die Anzahl der RAM-Module.");
                    return "Dieses Mainboard hat nicht genug RAM-Slots für die Anzahl der RAM-Module.";
                } else if(draft.getComputerCase() != null && !checkMainboardCaseCompatibility((Mainboard) component, draft.getComputerCase())) {
                    //System.out.println("LIVE: Das ausgewählte Mainboard passt nicht ins Gehäuse.");
                    return "Dieses Mainboard passt nicht in das ausgewählte Gehäuse.";
                }
            }

            if(component instanceof Case) {
                if(draft.getPsu() != null && !((Case) component).getPSUFormFactor().equals(draft.getPsu().getFormFactor())) {
                    //System.out.println("LIVE: Die Form des ausgewählten Netzteils passt nicht zum Gehäuse.");
                    return "Das ausgewählte Netzeil passt nicht in das Gehäuse.";
                    // WICHTIG: Hier ergänzen dass abwärtskompatibel
                } else if(draft.getMainboard() != null && !((Case) component).getMotherboardFormFactor().equals(draft.getMainboard().getFormFactor())) {
                    //System.out.println("LIVE: Das ausgewählte Mainboard passt nicht ins Gehäuse.");
                    return "Das ausgewählte Mainboard ist zu groß für das Gehäuse.";
                }
            }

            if(component instanceof PSU) {
                if(draft.getComputerCase() != null && !((PSU) component).getFormFactor().equals(draft.getComputerCase().getPSUFormFactor())) {
                    //System.out.println("LIVE: Die Form des ausgewählten Netzteils passt nicht zum Gehäuse.");
                    return "Dieses Netzeil passt nicht in das ausgewählte Gehäuse.";
                }
            }

            return null;
        }

        /**
         * Checkt die Kompatibilität von Mainboard und Gehäuse bezüglich der Formfaktoren. Es wird berücksichtigt, dass größere Formfaktoren abwärtskompatibel zu kleineren sind (z.B. passt ein ATX-Mainboard in ein E-ATX-Gehäuse, aber nicht umgekehrt).
         * @return true, wenn das Mainboard mit dem Gehäuse kompatibel ist, andernfalls false.
         * @author Tuluhan
         */
        private boolean checkMainboardCaseCompatibility(Mainboard mainboard, Case caseComponent) {
            String[] mainboardFormFactors = {"E-ATX", "ATX", "Micro-ATX", "Mini-ITX"};

            String mainboardFormFactor = mainboard.getFormFactor().getName();
            String caseFormFactor = caseComponent.getMotherboardFormFactor().getName();

            for(int i = 0; i < mainboardFormFactors.length; i++) {
                if(mainboardFormFactors[i].equals(caseFormFactor)) {
                    for(int j = i; j < mainboardFormFactors.length; j++) {
                        if(mainboardFormFactors[j].equals(mainboardFormFactor)) {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        }

        /**
         * Diese Methode überprüft die Kompatibilität der Komponenten.
         * Es wird lediglich überprüft, ob der PC so gebaut werden und existieren könnte.
         * Bspw. ob das Netzteil genug Leistung hat, um die Komponenten zu versorgen, wird hier nicht überprüft.
         * @return true, wenn die Komponenten kompatibel sind und der Computer erstellt werden kann, andernfalls false. Es werden auch Fehlermeldungen ausgegeben, die erklären, warum der Computer nicht erstellt werden kann.
         * @author Tuluhan
         */
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

            if (checkMainboardCaseCompatibility(mainboard, computerCase)) {
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

        /**
         * Erstellt den Computer nach erfolgreicher Validierung. Ansonsten wird eine Fehlermeldung ausgegeben und null zurückgegeben.
         * @return Der erstellte Computer oder null, falls die Validierung fehlschlägt.
         */
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