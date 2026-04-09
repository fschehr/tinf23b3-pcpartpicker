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
    private Integer editingComputerId;
    private CPU cpu;
    private GPU gpu;
    private Mainboard mainboard;
    private RAM ram;
    private int ramModule;
    private PSU psu;
    private Case computerCase;
    private List<Storage> storage;
    private boolean unsavedChanges; 
    private boolean bottleneckMode = false; 

    public void startNewDraft() {
        this.builder = new Computer.Builder();
        this.editingComputerId = null;
        this.cpu = null; 
        this.gpu = null; 
        this.mainboard = null;
        this.ram = null; 
        this.ramModule = 0; 
        this.psu = null;
        this.computerCase = null; 
        this.storage = null;
        this.unsavedChanges = false;
        this.bottleneckMode = false; 
    }

    public void loadFromComputer(Computer computer) {
        this.builder = new Computer.Builder();
        this.editingComputerId = computer.getId();
        this.cpu = computer.getCPU();
        this.gpu = computer.getGPU();
        this.mainboard = computer.getMainboard();
        this.ram = computer.getRAM();
        this.ramModule = computer.getRamModule();
        this.psu = computer.getPSU();
        this.computerCase = computer.getComputerCase();
        this.storage = computer.getStorageDevices() == null ? null : new java.util.ArrayList<>(computer.getStorageDevices());

        this.builder.setId(computer.getId());
        if (this.cpu != null) {
            this.builder.setCPU(this.cpu);
        }
        this.builder.setGPU(this.gpu);
        if (this.mainboard != null) {
            this.builder.setMainboard(this.mainboard);
        }
        if (this.ram != null) {
            this.builder.setRAM(this.ram, this.ramModule);
        }
        if (this.psu != null) {
            this.builder.setPSU(this.psu);
        }
        if (this.computerCase != null) {
            this.builder.setComputerCase(this.computerCase);
        }
        if (this.storage != null && !this.storage.isEmpty()) {
            this.builder.setStorageDevices(this.storage.toArray(new Storage[0]));
        }

        this.unsavedChanges = false;
    }

    public void markAsSaved() {
        this.unsavedChanges = false;
    }

    public void editDraft(Computer computer) {
        loadFromComputer(computer);
    }

    public Computer.Builder getBuilder() {
        return builder;
    }

    public void clear() {
        this.builder = null;
        this.editingComputerId = null;
    }


    public void setBottleneckMode(boolean bottleneckMode) {
        this.bottleneckMode = bottleneckMode; 
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
        this.builder.setCPU(cpu);
        this.unsavedChanges = true;
    }

    public void setGpu(GPU gpu) {
        this.gpu = gpu;
        this.builder.setGPU(gpu);
        this.unsavedChanges = true;
    }

    public void setMainboard(Mainboard mb) {
        this.mainboard = mb;
        this.builder.setMainboard(mb);
        this.unsavedChanges = true;
    }

    public void setRam(RAM ram, int module) {
        this.ram = ram;
        this.ramModule = module;
        this.builder.setRAM(ram, module);
        this.unsavedChanges = true;
    }

    public void setPsu(PSU psu) {
        this.psu = psu;
        this.builder.setPSU(psu);
        this.unsavedChanges = true;
    }

    public void setComputerCase(Case c) {
        this.computerCase = c;
        this.builder.setComputerCase(c);
        this.unsavedChanges = true;
    }

    public void setStorage(List<Storage> storage) {
        this.storage = storage;
        if (storage != null && !storage.isEmpty()) {
            this.builder.setStorageDevices(storage.toArray(new Storage[0]));
        }
        this.unsavedChanges = true;
    }

    public void setEditingComputerId(int id) {
        this.editingComputerId = id; 
        if(this.builder != null) {
            this.builder.setId(id);
        }
    }

    public void addStorage(Storage s) {
        if (this.storage == null) {
            this.storage = new java.util.ArrayList<>();
        }
        this.storage.add(s);
        
        this.builder.setStorageDevices(this.storage.toArray(new Storage[0]));
        this.unsavedChanges = true;
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
    
    public Integer getEditingComputerId() {
        return editingComputerId;
    }
    
    public boolean hasUnsavedChanges() {
        return this.unsavedChanges;
    }

    public boolean isBottlneckMode() {
        return bottleneckMode; 
    }
    


    public double getTotalPrice() {
        double totalPrice = 0.0;
        if (computerCase != null) totalPrice += computerCase.getPrice();
        if (cpu != null) totalPrice += cpu.getPrice();
        if (gpu != null) totalPrice += gpu.getPrice();
        if (mainboard != null) totalPrice += mainboard.getPrice();
        if (ram != null) totalPrice += ramModule * ram.getPrice();
        if (psu != null) totalPrice += psu.getPrice();
        if (storage != null && !storage.isEmpty()) {
            for (Storage s : storage) {
                totalPrice += s.getPrice();
            }
        }
        return totalPrice; 
    }

    public int getTotalPowerConsumption() {
        int totalPower = 0;
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