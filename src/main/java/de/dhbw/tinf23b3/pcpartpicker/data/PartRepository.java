package de.dhbw.tinf23b3.pcpartpicker.data;

import de.dhbw.tinf23b3.pcpartpicker.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for managing PC parts data
 */
public class PartRepository {
    private List<CPU> cpus;
    private List<CPUCooler> cpuCoolers;
    private List<Motherboard> motherboards;
    private List<Memory> memoryModules;
    private List<Storage> storageDevices;
    private List<VideoCard> videoCards;
    private List<Case> cases;
    private List<PowerSupply> powerSupplies;

    private final DataLoader dataLoader;

    public PartRepository() {
        this.dataLoader = new DataLoader();
        loadAllParts();
    }

    /**
     * Load all parts from data files
     */
    public void loadAllParts() {
        System.out.println("Loading parts database...");
        cpus = dataLoader.loadCPUs();
        cpuCoolers = dataLoader.loadCPUCoolers();
        motherboards = dataLoader.loadMotherboards();
        memoryModules = dataLoader.loadMemory();
        storageDevices = dataLoader.loadStorage();
        videoCards = dataLoader.loadVideoCards();
        cases = dataLoader.loadCases();
        powerSupplies = dataLoader.loadPowerSupplies();
        
        System.out.println("Loaded " + getTotalPartCount() + " parts");
    }

    /**
     * Get total count of all parts
     */
    public int getTotalPartCount() {
        return cpus.size() + cpuCoolers.size() + motherboards.size() + 
               memoryModules.size() + storageDevices.size() + videoCards.size() + 
               cases.size() + powerSupplies.size();
    }

    // Getters
    public List<CPU> getCpus() {
        return new ArrayList<>(cpus);
    }

    public List<CPUCooler> getCpuCoolers() {
        return new ArrayList<>(cpuCoolers);
    }

    public List<Motherboard> getMotherboards() {
        return new ArrayList<>(motherboards);
    }

    public List<Memory> getMemoryModules() {
        return new ArrayList<>(memoryModules);
    }

    public List<Storage> getStorageDevices() {
        return new ArrayList<>(storageDevices);
    }

    public List<VideoCard> getVideoCards() {
        return new ArrayList<>(videoCards);
    }

    public List<Case> getCases() {
        return new ArrayList<>(cases);
    }

    public List<PowerSupply> getPowerSupplies() {
        return new ArrayList<>(powerSupplies);
    }

    /**
     * Get CPUs filtered by price range
     */
    public List<CPU> getCpusByPriceRange(double minPrice, double maxPrice) {
        return cpus.stream()
            .filter(cpu -> cpu.hasPrice() && cpu.getPrice() >= minPrice && cpu.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }

    /**
     * Get motherboards compatible with a CPU socket
     */
    public List<Motherboard> getMotherboardsBySocket(String socket) {
        if (socket == null) return new ArrayList<>();
        return motherboards.stream()
            .filter(mb -> socket.equals(mb.getSocket()))
            .collect(Collectors.toList());
    }

    /**
     * Search parts by name (case-insensitive)
     */
    public <T extends PCPart> List<T> searchByName(List<T> parts, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(parts);
        }
        String lowerQuery = query.toLowerCase();
        return parts.stream()
            .filter(part -> part.getName().toLowerCase().contains(lowerQuery))
            .collect(Collectors.toList());
    }

    /**
     * Get parts within a price range
     */
    public <T extends PCPart> List<T> filterByPrice(List<T> parts, double minPrice, double maxPrice) {
        return parts.stream()
            .filter(part -> part.hasPrice() && part.getPrice() >= minPrice && part.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }

    /**
     * Sort parts by price (ascending)
     */
    public <T extends PCPart> List<T> sortByPrice(List<T> parts, boolean ascending) {
        return parts.stream()
            .filter(PCPart::hasPrice)
            .sorted((p1, p2) -> ascending ? 
                Double.compare(p1.getPrice(), p2.getPrice()) :
                Double.compare(p2.getPrice(), p1.getPrice()))
            .collect(Collectors.toList());
    }

    /**
     * Get sample parts for testing
     */
    public void generateSampleData() {
        System.out.println("Generating sample data...");
        
        // Sample CPUs
        cpus = new ArrayList<>();
        cpus.add(createSampleCPU("AMD Ryzen 7 7800X3D", 340.05, 8, 4.2, 5.0, "Zen 4", 120));
        cpus.add(createSampleCPU("Intel Core i5-14600K", 189.99, 14, 3.5, 5.3, "Raptor Lake", 125));
        cpus.add(createSampleCPU("AMD Ryzen 5 7600X", 170.49, 6, 4.7, 5.3, "Zen 4", 105));
        
        // Sample Motherboards
        motherboards = new ArrayList<>();
        motherboards.add(createSampleMotherboard("ASUS ROG Strix B650", 199.99, "AM5", "ATX", 128, 4));
        motherboards.add(createSampleMotherboard("MSI MAG Z790 Tomahawk", 289.99, "LGA1700", "ATX", 128, 4));
        
        // Sample Memory
        memoryModules = new ArrayList<>();
        memoryModules.add(createSampleMemory("Corsair Vengeance 32GB DDR5", 119.99, 32, 5600));
        memoryModules.add(createSampleMemory("G.Skill Trident Z5 32GB DDR5", 129.99, 32, 6000));
        
        // Sample Storage
        storageDevices = new ArrayList<>();
        storageDevices.add(createSampleStorage("Samsung 980 Pro 1TB", 99.99, 1000, "SSD", "M.2"));
        storageDevices.add(createSampleStorage("WD Black SN850X 2TB", 169.99, 2000, "SSD", "M.2"));
        
        // Sample Video Cards
        videoCards = new ArrayList<>();
        videoCards.add(createSampleVideoCard("NVIDIA RTX 4070 Ti", 799.99, "RTX 4070 Ti", 12));
        videoCards.add(createSampleVideoCard("AMD RX 7800 XT", 499.99, "RX 7800 XT", 16));
        
        // Sample Cases
        cases = new ArrayList<>();
        cases.add(createSampleCase("NZXT H510", 79.99, "ATX Mid Tower", "Black"));
        cases.add(createSampleCase("Fractal Design Meshify C", 89.99, "ATX Mid Tower", "White"));
        
        // Sample Power Supplies
        powerSupplies = new ArrayList<>();
        powerSupplies.add(createSamplePowerSupply("Corsair RM850x", 129.99, 850, "80+ Gold"));
        powerSupplies.add(createSamplePowerSupply("EVGA SuperNOVA 750 G6", 119.99, 750, "80+ Gold"));
        
        // Sample CPU Coolers
        cpuCoolers = new ArrayList<>();
        cpuCoolers.add(createSampleCPUCooler("Noctua NH-D15", 109.99));
        cpuCoolers.add(createSampleCPUCooler("be quiet! Dark Rock Pro 4", 89.99));
        
        System.out.println("Sample data generated: " + getTotalPartCount() + " parts");
    }

    // Helper methods to create sample parts
    private CPU createSampleCPU(String name, double price, int cores, double baseClock, double boostClock, String arch, int tdp) {
        CPU cpu = new CPU(name, price);
        cpu.setCoreCount(cores);
        cpu.setCoreClock(baseClock);
        cpu.setBoostClock(boostClock);
        cpu.setMicroarchitecture(arch);
        cpu.setTdp(tdp);
        return cpu;
    }

    private Motherboard createSampleMotherboard(String name, double price, String socket, String formFactor, int maxMem, int memSlots) {
        Motherboard mb = new Motherboard(name, price);
        mb.setSocket(socket);
        mb.setFormFactor(formFactor);
        mb.setMaxMemory(maxMem);
        mb.setMemorySlots(memSlots);
        return mb;
    }

    private Memory createSampleMemory(String name, double price, int capacity, int speed) {
        Memory mem = new Memory(name, price);
        mem.setSpeed(new int[]{5, speed});
        mem.setModules(new int[]{2, capacity / 2});
        return mem;
    }

    private Storage createSampleStorage(String name, double price, int capacity, String type, String formFactor) {
        Storage storage = new Storage(name, price);
        storage.setCapacity(capacity);
        storage.setStorageType(type);
        storage.setFormFactor(formFactor);
        return storage;
    }

    private VideoCard createSampleVideoCard(String name, double price, String chipset, int memory) {
        VideoCard card = new VideoCard(name, price);
        card.setChipset(chipset);
        card.setMemory(memory);
        return card;
    }

    private Case createSampleCase(String name, double price, String type, String color) {
        Case c = new Case(name, price);
        c.setCaseType(type);
        c.setColor(color);
        return c;
    }

    private PowerSupply createSamplePowerSupply(String name, double price, int wattage, String efficiency) {
        PowerSupply psu = new PowerSupply(name, price);
        psu.setWattage(wattage);
        psu.setEfficiencyRating(efficiency);
        return psu;
    }

    private CPUCooler createSampleCPUCooler(String name, double price) {
        return new CPUCooler(name, price);
    }
}
