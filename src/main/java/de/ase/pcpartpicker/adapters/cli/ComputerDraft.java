package de.ase.pcpartpicker.adapters.cli;

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


    public double getTotalPrice() {
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

    public double getTotalPowerConsumption() {
        double totalPower = 0.0;
        if (computerCase != null) totalPower += computerCase.getPowerConsumptionW();
        if (cpu != null) totalPower += cpu.getPowerConsumptionW();
        if (gpu != null) totalPower += gpu.getPowerConsumptionW();
        if (mainboard != null) totalPower += mainboard.getPowerConsumptionW();
        if (ram != null) totalPower += ram.getPowerConsumptionW();
        if (psu != null) totalPower += psu.getPowerConsumptionW();
        if (storage != null && !storage.isEmpty()) {
            for (Storage s : storage) {
                totalPower += s.getPowerConsumptionW();
            }
        }
        return totalPower; 
    }

    public int getMainboardRamSlots(){
        if(mainboard != null) {
            return mainboard.getRamSlots();
        }
        else {
            return 4; 
        }
    }
   

}