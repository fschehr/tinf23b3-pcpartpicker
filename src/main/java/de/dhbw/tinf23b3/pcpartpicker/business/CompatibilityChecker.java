package de.dhbw.tinf23b3.pcpartpicker.business;

import de.dhbw.tinf23b3.pcpartpicker.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks compatibility between PC components
 */
public class CompatibilityChecker {
    
    /**
     * Check if a motherboard is compatible with a CPU
     */
    public boolean isCPUCompatible(CPU cpu, Motherboard motherboard) {
        if (cpu == null || motherboard == null) {
            return false;
        }
        
        // Check socket compatibility
        String cpuSocket = determineCPUSocket(cpu);
        String mbSocket = motherboard.getSocket();
        
        return cpuSocket != null && mbSocket != null && cpuSocket.equals(mbSocket);
    }

    /**
     * Check if memory is compatible with motherboard
     */
    public boolean isMemoryCompatible(Memory memory, Motherboard motherboard) {
        if (memory == null || motherboard == null) {
            return true; // Can't check compatibility without parts
        }
        
        // Check total capacity
        int totalCapacity = memory.getTotalCapacity();
        Integer maxMemory = motherboard.getMaxMemory();
        
        if (maxMemory != null && totalCapacity > maxMemory) {
            return false;
        }
        
        // Check number of modules
        int[] modules = memory.getModules();
        Integer memorySlots = motherboard.getMemorySlots();
        
        if (modules != null && memorySlots != null && modules.length >= 1) {
            return modules[0] <= memorySlots;
        }
        
        return true;
    }

    /**
     * Check if case is compatible with motherboard
     */
    public boolean isCaseCompatible(Case pcCase, Motherboard motherboard) {
        if (pcCase == null || motherboard == null) {
            return true;
        }
        
        String caseType = pcCase.getCaseType();
        String mbFormFactor = motherboard.getFormFactor();
        
        if (caseType == null || mbFormFactor == null) {
            return true;
        }
        
        // ATX cases support ATX, mATX, and Mini-ITX
        // mATX cases support mATX and Mini-ITX
        // Mini-ITX cases only support Mini-ITX
        
        if (caseType.contains("ATX") && !caseType.contains("Mini")) {
            return mbFormFactor.contains("ATX") || mbFormFactor.contains("ITX");
        } else if (caseType.contains("Micro") || caseType.contains("mATX")) {
            return mbFormFactor.contains("Micro") || mbFormFactor.contains("Mini");
        } else if (caseType.contains("Mini") || caseType.contains("ITX")) {
            return mbFormFactor.contains("Mini") || mbFormFactor.contains("ITX");
        }
        
        return true;
    }

    /**
     * Check if power supply has enough wattage
     */
    public boolean isPowerSupplyAdequate(PowerSupply psu, PCBuild build) {
        if (psu == null || psu.getWattage() == null) {
            return false;
        }
        
        int estimatedWattage = estimateTotalWattage(build);
        int psuWattage = psu.getWattage();
        
        // PSU should have at least 20% headroom
        return psuWattage >= (estimatedWattage * 1.2);
    }

    /**
     * Estimate total wattage of a build
     */
    public int estimateTotalWattage(PCBuild build) {
        int totalWattage = 0;
        
        // CPU
        if (build.getCpu() != null && build.getCpu().getTdp() != null) {
            totalWattage += build.getCpu().getTdp();
        } else {
            totalWattage += 125; // Default estimate
        }
        
        // Video Card (rough estimate)
        if (build.getVideoCard() != null) {
            totalWattage += 250; // Average GPU power consumption
        }
        
        // Motherboard, memory, storage, etc.
        totalWattage += 50; // Base system components
        
        // Add 10W per storage device
        totalWattage += build.getStorageDevices().size() * 10;
        
        return totalWattage;
    }

    /**
     * Get compatibility warnings for a build
     */
    public List<String> getCompatibilityWarnings(PCBuild build) {
        List<String> warnings = new ArrayList<>();
        
        // Check CPU and Motherboard compatibility
        if (build.getCpu() != null && build.getMotherboard() != null) {
            if (!isCPUCompatible(build.getCpu(), build.getMotherboard())) {
                warnings.add("WARNING: CPU socket does not match motherboard socket!");
            }
        }
        
        // Check Memory compatibility
        if (build.getMemory() != null && build.getMotherboard() != null) {
            if (!isMemoryCompatible(build.getMemory(), build.getMotherboard())) {
                warnings.add("WARNING: Memory configuration may not be compatible with motherboard!");
            }
        }
        
        // Check Case compatibility
        if (build.getPcCase() != null && build.getMotherboard() != null) {
            if (!isCaseCompatible(build.getPcCase(), build.getMotherboard())) {
                warnings.add("WARNING: Case may not fit motherboard form factor!");
            }
        }
        
        // Check PSU adequacy
        if (build.getPowerSupply() != null) {
            if (!isPowerSupplyAdequate(build.getPowerSupply(), build)) {
                int required = (int)(estimateTotalWattage(build) * 1.2);
                warnings.add(String.format("WARNING: Power supply may be insufficient! Recommended: %dW+", required));
            }
        }
        
        // Check for missing video card when CPU has no iGPU
        if (build.getVideoCard() == null && build.getCpu() != null) {
            if (!build.getCpu().hasIntegratedGraphics()) {
                warnings.add("WARNING: No video card selected and CPU has no integrated graphics!");
            }
        }
        
        return warnings;
    }

    /**
     * Determine CPU socket type based on CPU name and specifications
     */
    private String determineCPUSocket(CPU cpu) {
        String name = cpu.getName().toUpperCase();
        
        // AMD socket determination
        if (name.contains("RYZEN")) {
            if (name.contains("7000") || name.contains("9000") || 
                name.contains("7700") || name.contains("7800") || name.contains("7900") || name.contains("7950")) {
                return "AM5";
            } else if (name.contains("5000") || name.contains("5600") || name.contains("5700") || 
                       name.contains("5800") || name.contains("5900") || name.contains("5950")) {
                return "AM4";
            } else {
                return "AM4"; // Default for older Ryzen
            }
        }
        
        // Intel socket determination
        if (name.contains("INTEL")) {
            if (name.contains("14") || name.contains("13") || name.contains("12")) {
                return "LGA1700";
            } else if (name.contains("11") || name.contains("10")) {
                return "LGA1200";
            } else {
                return "LGA1151"; // Older Intel
            }
        }
        
        return null;
    }
}
