package de.dhbw.tinf23b3.pcpartpicker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete PC build configuration
 */
public class PCBuild {
    private String buildName;
    private CPU cpu;
    private CPUCooler cpuCooler;
    private Motherboard motherboard;
    private Memory memory;
    private List<Storage> storageDevices;
    private VideoCard videoCard;
    private Case pcCase;
    private PowerSupply powerSupply;

    public PCBuild() {
        this.buildName = "New Build";
        this.storageDevices = new ArrayList<>();
    }

    public PCBuild(String buildName) {
        this.buildName = buildName;
        this.storageDevices = new ArrayList<>();
    }

    // Getters and setters
    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public CPUCooler getCpuCooler() {
        return cpuCooler;
    }

    public void setCpuCooler(CPUCooler cpuCooler) {
        this.cpuCooler = cpuCooler;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public List<Storage> getStorageDevices() {
        return storageDevices;
    }

    public void setStorageDevices(List<Storage> storageDevices) {
        this.storageDevices = storageDevices;
    }

    public void addStorage(Storage storage) {
        this.storageDevices.add(storage);
    }

    public VideoCard getVideoCard() {
        return videoCard;
    }

    public void setVideoCard(VideoCard videoCard) {
        this.videoCard = videoCard;
    }

    public Case getPcCase() {
        return pcCase;
    }

    public void setPcCase(Case pcCase) {
        this.pcCase = pcCase;
    }

    public PowerSupply getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(PowerSupply powerSupply) {
        this.powerSupply = powerSupply;
    }

    /**
     * Calculate total price of the build
     */
    public double getTotalPrice() {
        double total = 0.0;
        if (cpu != null && cpu.hasPrice()) total += cpu.getPrice();
        if (cpuCooler != null && cpuCooler.hasPrice()) total += cpuCooler.getPrice();
        if (motherboard != null && motherboard.hasPrice()) total += motherboard.getPrice();
        if (memory != null && memory.hasPrice()) total += memory.getPrice();
        if (videoCard != null && videoCard.hasPrice()) total += videoCard.getPrice();
        if (pcCase != null && pcCase.hasPrice()) total += pcCase.getPrice();
        if (powerSupply != null && powerSupply.hasPrice()) total += powerSupply.getPrice();
        for (Storage storage : storageDevices) {
            if (storage.hasPrice()) total += storage.getPrice();
        }
        return total;
    }

    /**
     * Check if build is complete (has all required components)
     */
    public boolean isComplete() {
        return cpu != null && motherboard != null && memory != null && 
               !storageDevices.isEmpty() && pcCase != null && powerSupply != null;
    }

    /**
     * Get list of missing components
     */
    public List<String> getMissingComponents() {
        List<String> missing = new ArrayList<>();
        if (cpu == null) missing.add("CPU");
        if (motherboard == null) missing.add("Motherboard");
        if (memory == null) missing.add("Memory");
        if (storageDevices.isEmpty()) missing.add("Storage");
        if (pcCase == null) missing.add("Case");
        if (powerSupply == null) missing.add("Power Supply");
        return missing;
    }

    /**
     * Get a summary of the build
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append(String.format("Build: %s\n", buildName));
        sb.append("=".repeat(60)).append("\n\n");
        
        sb.append(String.format("%-20s: %s\n", "CPU", 
            cpu != null ? cpu.getShortDescription() : "Not selected"));
        sb.append(String.format("%-20s: %s\n", "CPU Cooler", 
            cpuCooler != null ? cpuCooler.getShortDescription() : "Not selected"));
        sb.append(String.format("%-20s: %s\n", "Motherboard", 
            motherboard != null ? motherboard.getShortDescription() : "Not selected"));
        sb.append(String.format("%-20s: %s\n", "Memory", 
            memory != null ? memory.getShortDescription() : "Not selected"));
        sb.append(String.format("%-20s: %s\n", "Video Card", 
            videoCard != null ? videoCard.getShortDescription() : "Not selected (using iGPU)"));
        
        if (storageDevices.isEmpty()) {
            sb.append(String.format("%-20s: %s\n", "Storage", "Not selected"));
        } else {
            for (int i = 0; i < storageDevices.size(); i++) {
                String label = i == 0 ? "Storage" : "";
                sb.append(String.format("%-20s: %s\n", label, 
                    storageDevices.get(i).getShortDescription()));
            }
        }
        
        sb.append(String.format("%-20s: %s\n", "Case", 
            pcCase != null ? pcCase.getShortDescription() : "Not selected"));
        sb.append(String.format("%-20s: %s\n", "Power Supply", 
            powerSupply != null ? powerSupply.getShortDescription() : "Not selected"));
        
        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append(String.format("%-20s: $%.2f\n", "Total Price", getTotalPrice()));
        sb.append(String.format("%-20s: %s\n", "Build Status", 
            isComplete() ? "Complete" : "Incomplete"));
        
        if (!isComplete()) {
            sb.append(String.format("%-20s: %s\n", "Missing", 
                String.join(", ", getMissingComponents())));
        }
        sb.append("=".repeat(60)).append("\n");
        
        return sb.toString();
    }
}
