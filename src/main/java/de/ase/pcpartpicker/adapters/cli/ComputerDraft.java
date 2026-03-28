package de.ase.pcpartpicker.adapters.cli;

import java.util.ArrayList;
import java.util.List;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.part_assembly.Computer;

public class ComputerDraft {
    private Computer.Builder builder;
    private CPU cpu;
    private GPU gpu;
    private Mainboard mainboard;
    private RAM ram;
    private int ramModule;
    private PSU psu;
    private Case computerCase;
    private List<Storage> storage;

    public void startNewDraft() {
        this.builder = new Computer.Builder();
        this.cpu = null; this.gpu = null; this.mainboard = null;
        this.ram = null; this.ramModule = 0; this.psu = null;
        this.computerCase = null; this.storage = null;
    }

    public Computer.Builder getBuilder() {
        return builder;
    }

    public void clear() {
        this.builder = null;
    }


    public void setCpu(CPU cpu) {
        this.cpu = cpu;
        this.builder.setCPU(cpu);
    }

    public void setGpu(GPU gpu) {
        this.gpu = gpu;
        this.builder.setGPU(gpu);
    }

    public void setMainboard(Mainboard mb) {
        this.mainboard = mb;
        this.builder.setMainboard(mb);
    }

    public void setRam(RAM ram, int module) {
        this.ram = ram;
        this.ramModule = module;
        this.builder.setRAM(ram, module);
    }

    public void setPsu(PSU psu) {
        this.psu = psu;
        this.builder.setPSU(psu);
    }

    public void setComputerCase(Case c) {
        this.computerCase = c;
        this.builder.setComputerCase(c);
    }

    public void setStorage(List<Storage> storage) {
        this.storage = storage;
        this.builder.setStorageDevices(storage.toArray(new Storage[0])); 
    }

    public void addStorage(Storage s) {
        if (this.storage == null) {
            this.storage = new java.util.ArrayList<>();
        }
        this.storage.add(s);
        
        this.builder.setStorageDevices(this.storage.toArray(new Storage[0]));
    }

    public GPU getGPU() {
        return gpu; 
    }

    public CPU getCPU() {
        return cpu; 
    }

    public Mainboard getMainboard() {
        return mainboard;
    }

    public RAM getRAM() {
        return ram; 
    }

    public PSU getPSU() {
        return psu; 
    }

    public Case getComputerCase() {
        return computerCase; 
    }

    public List<Storage> getStorage() {
        return storage;
    }

    public int getRamModule() {
        return ramModule;
    }


    public String[][] getDraftAsTableRows() {
    List<String[]> rows = new ArrayList<>();

        // Gehäuse
        if (computerCase != null) {
            rows.add(new String[]{"Gehäuse", "Name", computerCase.getName()});
            rows.add(new String[]{"", "Formfaktor", computerCase.getMotherboardFormFactor().getName()});
            rows.add(new String[]{"", "Preis", String.valueOf(computerCase.getPrice())});
        } else {
            rows.add(new String[]{"Gehäuse", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // CPU
        if (cpu != null) {
            rows.add(new String[]{"CPU", "Name", cpu.getName()});
            rows.add(new String[]{"", "Takt", cpu.getSpeedGHz() + " GHz"});
            rows.add(new String[]{"", "Preis", String.valueOf(cpu.getPrice())});
        } else {
            rows.add(new String[]{"CPU", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // GPU
        if (gpu != null) {
            rows.add(new String[]{"GPU", "Name", gpu.getName()});
            rows.add(new String[]{"", "VRAM", gpu.getVramGB() + " GB"});
            rows.add(new String[]{"", "Preis", String.valueOf(gpu.getPrice())});
        } else {
            rows.add(new String[]{"GPU", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // Mainboard
        if (mainboard != null) {
            rows.add(new String[]{"Mainboard", "Name", mainboard.getName()});
            rows.add(new String[]{"", "Formfaktor", mainboard.getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", String.valueOf(mainboard.getPrice())});
        } else {
            rows.add(new String[]{"Mainboard", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // RAM
        if (ram != null) {
            rows.add(new String[]{"RAM", "Name", ram.getName()});
            rows.add(new String[]{"", "Module", String.valueOf(ramModule)});
            rows.add(new String[]{"", "Kapazität", ram.getCapacityGB() + " GB"});
            rows.add(new String[]{"", "Preis", String.valueOf(ram.getPrice())});
        } else {
            rows.add(new String[]{"RAM", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // Netzteil
        if (psu != null) {
            rows.add(new String[]{"Netzteil", "Name", psu.getName()});
            rows.add(new String[]{"", "Leistung", psu.getWattage() + " W"});
            rows.add(new String[]{"", "Formfaktor", psu.getFormFactor().getName()});
            rows.add(new String[]{"", "Preis", String.valueOf(psu.getPrice())});
        } else {
            rows.add(new String[]{"Netzteil", "Status", "Nicht ausgewählt"});
        }
        rows.add(new String[]{"", "", ""});

        // Storage (alle Geräte auflisten)
        if (storage != null && !storage.isEmpty()) {
            int i = 0;
            for (Storage s : storage) {
                String comp = (i == 0) ? "Speicher" : "";
                rows.add(new String[]{comp, "Name", s.getName()});
                rows.add(new String[]{"", "Kapazität", s.getCapacityGB() + " GB"});
                rows.add(new String[]{"", "Preis", String.valueOf(s.getPrice())});
                rows.add(new String[]{"", "", ""});
                i++;
            }
        } else {
            rows.add(new String[]{"Speicher", "Status", "Nicht ausgewählt"});
            rows.add(new String[]{"", "", ""});
        }
        
        rows.add(new String[]{"------", "------", "------"});
        rows.add(new String[]{"Gesamtpreis","", String.format("%.2f €", getTotalPrice()) }); 

        return rows.toArray(new String[0][]);
    }

    private double getTotalPrice() {
        double totalPrice = 0.0;
        if (computerCase != null) totalPrice += computerCase.getPrice();
        if (cpu != null) totalPrice += cpu.getPrice();
        if (gpu != null) totalPrice += gpu.getPrice();
        if (mainboard != null) totalPrice += mainboard.getPrice();
        if (ram != null) totalPrice += ram.getPrice();
        if (psu != null) totalPrice += psu.getPrice();
        if (storage != null && !storage.isEmpty()) {
            for (Storage s : storage) {
                totalPrice += s.getPrice();
            }
        }
        return totalPrice; 
    }
}