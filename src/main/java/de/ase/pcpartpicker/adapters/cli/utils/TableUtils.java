package de.ase.pcpartpicker.adapters.cli.utils;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.part_assembly.Computer;

public class TableUtils {
    ConnectionFactory cf = new ConnectionFactory();
    UserRepository userRepository = new UserRepository(cf);

    public static String[][] getComputerAsTableRows(Computer computer) {
        List<String[]> rows = new java.util.ArrayList<>();

        // Gehäuse
        if (computer.getComputerCase() != null) {
            rows.add(new String[]{"Gehäuse", "Name", computer.getComputerCase().getName()});
            rows.add(new String[]{"", "Formfaktor", computer.getComputerCase().getMotherboardFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getComputerCase().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Gehäuse", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // CPU
        if (computer.getCPU() != null) {
            rows.add(new String[]{"CPU", "Name", computer.getCPU().getName()});
            rows.add(new String[]{"", "Takt", computer.getCPU().getSpeedGHz() + " GHz"});
            rows.add(new String[]{"", "Preis", computer.getCPU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"CPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // GPU
        if (computer.getGPU() != null) {
            rows.add(new String[]{"GPU", "Name", computer.getGPU().getName()});
            rows.add(new String[]{"", "VRAM", computer.getGPU().getVramGB() + " GB"});
            rows.add(new String[]{"", "Preis", computer.getGPU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"GPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Mainboard
        if (computer.getMainboard() != null) {
            rows.add(new String[]{"Mainboard", "Name", computer.getMainboard().getName()});
            rows.add(new String[]{"", "Formfaktor", computer.getMainboard().getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getMainboard().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Mainboard", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // RAM
        if (computer.getRAM() != null) {
            rows.add(new String[]{"RAM", "Name", computer.getRAM().getName()});
            rows.add(new String[]{"", "Module", String.valueOf(computer.getRamModule())});
            rows.add(new String[]{"", "Gesamtkapazität", getRAMCapacity(computer.getRamModule(), computer.getRAM().getCapacityGB()) + " GB"});
            rows.add(new String[]{"", "Preis", computer.getRAM().getPrice() + " €"});
        } else {
            rows.add(new String[]{"RAM", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Netzteil
        if (computer.getPSU() != null) {
            rows.add(new String[]{"Netzteil", "Name", computer.getPSU().getName()});
            rows.add(new String[]{"", "Leistung", computer.getPSU().getWattage() + " W"});
            rows.add(new String[]{"", "Formfaktor", computer.getPSU().getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getPSU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Netzteil", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Storage
        List<Storage> storage = computer.getStorageDevices();
        if (storage != null && !storage.isEmpty()) {
            int i = 0;
            for (Storage s : storage) {
                String comp = (i == 0) ? "Speicher" : "";
                rows.add(new String[]{comp, "Name", s.getName()});
                rows.add(new String[]{"", "Kapazität", s.getCapacityGB() + " GB"});
                rows.add(new String[]{"", "Preis", s.getPrice() + " €"});
                rows.add(new String[]{"", "", ""});
                i++;
            }
        } else {
            rows.add(new String[]{"Speicher", "nicht ausgewählt", ""});
            rows.add(new String[]{"", "", ""});
        }

        // Trennlinie und Gesamtpreis
        rows.add(new String[]{"---", "---", "---"});
        rows.add(new String[]{"Leistungsaufnahme/Gesamt", "",
            computer.getPSU() != null
                ? String.format("%d W / %d W", computer.getTotalPowerConsumption(), computer.getPSU().getWattage())
                : "nicht berechenbar"});
        rows.add(new String[]{"Gesamtpreis", "", String.format("%.2f €", computer.getTotalPrice())});

        return rows.toArray(new String[0][]);
    }

    public static String[][] getDraftAsTableRows(ComputerDraft computer) {
    List<String[]> rows = new java.util.ArrayList<>();

        // Gehäuse
        if (computer.getComputerCase() != null) {
            rows.add(new String[]{"Gehäuse", "Name", computer.getComputerCase().getName()});
            rows.add(new String[]{"", "Formfaktor", computer.getComputerCase().getMotherboardFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getComputerCase().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Gehäuse", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // CPU
        if (computer.getCPU() != null) {
            rows.add(new String[]{"CPU", "Name", computer.getCPU().getName()});
            rows.add(new String[]{"", "Takt", computer.getCPU().getSpeedGHz() + " GHz"});
            rows.add(new String[]{"", "Preis", computer.getCPU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"CPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // GPU
        if (computer.getGPU() != null) {
            rows.add(new String[]{"GPU", "Name", computer.getGPU().getName()});
            rows.add(new String[]{"", "VRAM", computer.getGPU().getVramGB() + " GB"});
            rows.add(new String[]{"", "Preis", computer.getGPU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"GPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Mainboard
        if (computer.getMainboard() != null) {
            rows.add(new String[]{"Mainboard", "Name", computer.getMainboard().getName()});
            rows.add(new String[]{"", "Formfaktor", computer.getMainboard().getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getMainboard().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Mainboard", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // RAM
        if (computer.getRAM() != null) {
            rows.add(new String[]{"RAM", "Name", computer.getRAM().getName()});
            rows.add(new String[]{"", "Module", String.valueOf(computer.getRamModule())});
            rows.add(new String[]{"", "Kapazität/Gesamtkapazität",
                computer.getRAM().getCapacityGB() + " GB / " +
                getRAMCapacity(computer.getRamModule(), computer.getRAM().getCapacityGB()) + " GB"});
            rows.add(new String[]{"", "Preis", computer.getRAM().getPrice() + " €"});
        } else {
            rows.add(new String[]{"RAM", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Netzteil
        if (computer.getPSU() != null) {
            rows.add(new String[]{"Netzteil", "Name", computer.getPSU().getName()});
            rows.add(new String[]{"", "Leistung", computer.getPSU().getWattage() + " W"});
            rows.add(new String[]{"", "Formfaktor", computer.getPSU().getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", computer.getPSU().getPrice() + " €"});
        } else {
            rows.add(new String[]{"Netzteil", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Storage
        List<Storage> storage = computer.getStorage();
        if (storage != null && !storage.isEmpty()) {
            int i = 0;
            for (Storage s : storage) {
                String comp = (i == 0) ? "Speicher" : "";
                rows.add(new String[]{comp, "Name", s.getName()});
                rows.add(new String[]{"", "Kapazität", s.getCapacityGB() + " GB"});
                rows.add(new String[]{"", "Preis", s.getPrice() + " €"});
                rows.add(new String[]{"", "", ""});
                i++;
            }
        } else {
            rows.add(new String[]{"Speicher", "nicht ausgewählt", ""});
            rows.add(new String[]{"", "", ""});
        }

        // Trennlinie und Gesamtpreis
        rows.add(new String[]{"---", "---", "---"});
        rows.add(new String[]{"Leistungsaufnahme/Gesamt", "",
         String.format("%d W / %d W", computer.getTotalPowerConsumption(),
          computer.getPSU() == null ? 0 : computer.getPSU().getWattage())});
        rows.add(new String[]{"Gesamtpreis", "", String.format("%.2f €", computer.getTotalPrice())});

        return rows.toArray(new String[0][]);
    }
    

    private static int getRAMCapacity(int module, int capacity) {
        return module* capacity; 
    }

    
}
