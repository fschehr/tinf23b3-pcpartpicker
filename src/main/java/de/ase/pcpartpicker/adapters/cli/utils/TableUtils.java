package de.ase.pcpartpicker.adapters.cli.utils;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.part_assembly.Computer;

public class TableUtils {

    public static String[][] getComputerAsTableRows(Computer computer) {
        return buildTableRows(
            computer.getComputerCase(), computer.getCPU(), computer.getGPU(),
            computer.getMainboard(), computer.getRAM(), computer.getRamModule(),
            computer.getPSU(), computer.getStorageDevices(),
            computer.getTotalPowerConsumption(), computer.getTotalPrice()
        );
    }

    // 2. Methode für den unfertigen ComputerDraft
    public static String[][] getDraftAsTableRows(ComputerDraft draft) {
        return buildTableRows(
            draft.getComputerCase(), draft.getCPU(), draft.getGPU(),
            draft.getMainboard(), draft.getRAM(), draft.getRamModule(),
            draft.getPSU(), draft.getStorage(),
            draft.getTotalPowerConsumption(), draft.getTotalPrice()
        );
    }

    private static String[][] buildTableRows(Case pcCase, CPU cpu, GPU gpu, Mainboard mb, RAM ram, int ramModule, PSU psu, List<Storage> storage, int totalPower, double totalPrice) {
        List<String[]> rows = new java.util.ArrayList<>();

        // Gehäuse
        if (pcCase != null) {
            rows.add(new String[]{"Gehäuse", "Name", pcCase.getName()});
            rows.add(new String[]{"", "Formfaktor", pcCase.getMotherboardFormFactor().getName()});
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(pcCase.getPrice())});
        } else {
            rows.add(new String[]{"Gehäuse", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // CPU
        if (cpu != null) {
            rows.add(new String[]{"CPU", "Name", cpu.getName()});
            rows.add(new String[]{"", "Kerne", FormatUtils.formatNumber(cpu.getCoreCount())});
            rows.add(new String[]{"", "Takt", FormatUtils.formatNumber(cpu.getSpeedGHz()) + " GHz"}); // Tippfehler bei dir korrigiert (formatNumer -> formatNumber)
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(cpu.getPrice())});
        } else {
            rows.add(new String[]{"CPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // GPU
        if (gpu != null) {
            rows.add(new String[]{"GPU", "Name", gpu.getName()});
            rows.add(new String[]{"", "VRAM", FormatUtils.formatNumber(gpu.getVramGB()) + " GB"});
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(gpu.getPrice())});
        } else {
            rows.add(new String[]{"GPU", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Mainboard
        if (mb != null) {
            rows.add(new String[]{"Mainboard", "Name", mb.getName()});
            rows.add(new String[]{"", "Formfaktor", mb.getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(mb.getPrice())});
        } else {
            rows.add(new String[]{"Mainboard", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // RAM
        if (ram != null) {
            rows.add(new String[]{"RAM", "Name", ram.getName()});
            rows.add(new String[]{"", "Module", FormatUtils.formatNumber(ramModule)});
            rows.add(new String[]{"", "Gesamtkapazität", FormatUtils.formatNumber(getRAMCapacity(ramModule, ram.getCapacityGB())) + " GB"});
            rows.add(new String[]{"", "Takt", FormatUtils.formatNumber(ram.getSpeedMHz()) + " MHz"});
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(ramModule * ram.getPrice())});
        } else {
            rows.add(new String[]{"RAM", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Netzteil
        if (psu != null) {
            rows.add(new String[]{"Netzteil", "Name", psu.getName()});
            rows.add(new String[]{"", "Leistung", FormatUtils.formatNumber(psu.getWattage()) + " W"});
            rows.add(new String[]{"", "Formfaktor", psu.getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(psu.getPrice())});
        } else {
            rows.add(new String[]{"Netzteil", "nicht ausgewählt", ""});
        }
        rows.add(new String[]{"", "", ""});

        // Storage
        if (storage != null && !storage.isEmpty()) {
            int i = 0;
            for (Storage s : storage) {
                String comp = (i == 0) ? "Speicher" : "";
                rows.add(new String[]{comp, "Name", s.getName()});
                rows.add(new String[]{"", "Kapazität", FormatUtils.formatNumber(s.getCapacityGB()) + " GB"});
                rows.add(new String[]{"", "Preis", FormatUtils.formatPrice(s.getPrice())});
                rows.add(new String[]{"", "", ""});
                i++;
            }
        } else {
            rows.add(new String[]{"Speicher", "nicht ausgewählt", ""});
            rows.add(new String[]{"", "", ""});
        }

        // Trennlinie und Gesamtpreis (Logik vereint)
        rows.add(new String[]{"---", "---", "---"});
        String psuWattage = psu != null ? FormatUtils.formatNumber(psu.getWattage()) + " W" : "0 W";
        rows.add(new String[]{"Leistungsaufnahme/Gesamt", "", FormatUtils.formatNumber(totalPower) + " W / " + psuWattage});
        rows.add(new String[]{"Gesamtpreis", "", FormatUtils.formatPrice(totalPrice)});

        return rows.toArray(new String[0][]);
    }

    private static int getRAMCapacity(int module, int capacity) {
        return module * capacity; 
    }
}
