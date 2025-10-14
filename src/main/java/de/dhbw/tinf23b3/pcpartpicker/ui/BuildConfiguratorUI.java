package de.dhbw.tinf23b3.pcpartpicker.ui;

import de.dhbw.tinf23b3.pcpartpicker.data.PartRepository;
import de.dhbw.tinf23b3.pcpartpicker.model.*;
import de.dhbw.tinf23b3.pcpartpicker.util.ConsoleHelper;
import de.dhbw.tinf23b3.pcpartpicker.util.InputValidator;

import java.util.List;

/**
 * UI for configuring PC builds
 */
public class BuildConfiguratorUI {
    private final PartRepository repository;
    private final PCBuild build;
    private final InputValidator inputValidator;
    private static final int ITEMS_PER_PAGE = 10;

    public BuildConfiguratorUI(PartRepository repository, PCBuild build, InputValidator inputValidator) {
        this.repository = repository;
        this.build = build;
        this.inputValidator = inputValidator;
    }

    /**
     * Start the build configurator
     */
    public void start() {
        boolean configuring = true;
        
        while (configuring) {
            ConsoleHelper.clearScreen();
            ConsoleHelper.printHeader("BUILD CONFIGURATOR");
            
            System.out.println("Current Build: " + build.getBuildName());
            System.out.println("Total Price: " + ConsoleHelper.formatPrice(build.getTotalPrice()));
            ConsoleHelper.printSeparator();
            
            displayBuildComponents();
            
            String[] options = {
                "Select CPU",
                "Select Motherboard",
                "Select Memory",
                "Select Storage",
                "Select Video Card",
                "Select Case",
                "Select Power Supply",
                "Select CPU Cooler",
                "Remove Component"
            };
            
            System.out.println();
            ConsoleHelper.printMenu(options);
            System.out.println();
            
            int choice = inputValidator.getMenuChoice(options.length);
            
            if (choice == 0) {
                configuring = false;
            } else {
                handleConfiguratorChoice(choice);
            }
        }
    }

    /**
     * Display current build components
     */
    private void displayBuildComponents() {
        System.out.printf("%-15s: %s\n", "CPU", 
            build.getCpu() != null ? build.getCpu().getName() : "Not selected");
        System.out.printf("%-15s: %s\n", "Motherboard", 
            build.getMotherboard() != null ? build.getMotherboard().getName() : "Not selected");
        System.out.printf("%-15s: %s\n", "Memory", 
            build.getMemory() != null ? build.getMemory().getName() : "Not selected");
        System.out.printf("%-15s: %s\n", "Video Card", 
            build.getVideoCard() != null ? build.getVideoCard().getName() : "Not selected");
        
        if (build.getStorageDevices().isEmpty()) {
            System.out.printf("%-15s: %s\n", "Storage", "Not selected");
        } else {
            for (int i = 0; i < build.getStorageDevices().size(); i++) {
                String label = i == 0 ? "Storage" : "";
                System.out.printf("%-15s: %s\n", label, build.getStorageDevices().get(i).getName());
            }
        }
        
        System.out.printf("%-15s: %s\n", "Case", 
            build.getPcCase() != null ? build.getPcCase().getName() : "Not selected");
        System.out.printf("%-15s: %s\n", "Power Supply", 
            build.getPowerSupply() != null ? build.getPowerSupply().getName() : "Not selected");
        System.out.printf("%-15s: %s\n", "CPU Cooler", 
            build.getCpuCooler() != null ? build.getCpuCooler().getName() : "Not selected");
    }

    /**
     * Handle configurator menu choice
     */
    private void handleConfiguratorChoice(int choice) {
        switch (choice) {
            case 1:
                selectCPU();
                break;
            case 2:
                selectMotherboard();
                break;
            case 3:
                selectMemory();
                break;
            case 4:
                selectStorage();
                break;
            case 5:
                selectVideoCard();
                break;
            case 6:
                selectCase();
                break;
            case 7:
                selectPowerSupply();
                break;
            case 8:
                selectCPUCooler();
                break;
            case 9:
                removeComponent();
                break;
        }
    }

    /**
     * Select CPU
     */
    private void selectCPU() {
        List<CPU> cpus = repository.getCpus();
        CPU selected = selectPart("Select CPU", cpus);
        if (selected != null) {
            build.setCpu(selected);
            ConsoleHelper.printSuccess("CPU selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Motherboard
     */
    private void selectMotherboard() {
        List<Motherboard> motherboards = repository.getMotherboards();
        Motherboard selected = selectPart("Select Motherboard", motherboards);
        if (selected != null) {
            build.setMotherboard(selected);
            ConsoleHelper.printSuccess("Motherboard selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Memory
     */
    private void selectMemory() {
        List<Memory> memory = repository.getMemoryModules();
        Memory selected = selectPart("Select Memory", memory);
        if (selected != null) {
            build.setMemory(selected);
            ConsoleHelper.printSuccess("Memory selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Storage
     */
    private void selectStorage() {
        List<Storage> storage = repository.getStorageDevices();
        Storage selected = selectPart("Select Storage", storage);
        if (selected != null) {
            build.addStorage(selected);
            ConsoleHelper.printSuccess("Storage added: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Video Card
     */
    private void selectVideoCard() {
        List<VideoCard> videoCards = repository.getVideoCards();
        VideoCard selected = selectPart("Select Video Card", videoCards);
        if (selected != null) {
            build.setVideoCard(selected);
            ConsoleHelper.printSuccess("Video Card selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Case
     */
    private void selectCase() {
        List<Case> cases = repository.getCases();
        Case selected = selectPart("Select Case", cases);
        if (selected != null) {
            build.setPcCase(selected);
            ConsoleHelper.printSuccess("Case selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select Power Supply
     */
    private void selectPowerSupply() {
        List<PowerSupply> psus = repository.getPowerSupplies();
        PowerSupply selected = selectPart("Select Power Supply", psus);
        if (selected != null) {
            build.setPowerSupply(selected);
            ConsoleHelper.printSuccess("Power Supply selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Select CPU Cooler
     */
    private void selectCPUCooler() {
        List<CPUCooler> coolers = repository.getCpuCoolers();
        CPUCooler selected = selectPart("Select CPU Cooler", coolers);
        if (selected != null) {
            build.setCpuCooler(selected);
            ConsoleHelper.printSuccess("CPU Cooler selected: " + selected.getName());
            inputValidator.waitForEnter();
        }
    }

    /**
     * Generic method to select a part from a list
     */
    private <T extends PCPart> T selectPart(String title, List<T> parts) {
        if (parts.isEmpty()) {
            ConsoleHelper.printError("No parts available");
            inputValidator.waitForEnter();
            return null;
        }

        // Sort by price for easier selection
        parts = repository.sortByPrice(parts, true);

        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) parts.size() / ITEMS_PER_PAGE);

        while (true) {
            ConsoleHelper.clearScreen();
            ConsoleHelper.printHeader(title);

            int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, parts.size());

            ConsoleHelper.printPartList(parts, startIndex, endIndex);
            ConsoleHelper.printPaginationInfo(currentPage, totalPages, ITEMS_PER_PAGE, parts.size());

            System.out.println("\nOptions:");
            System.out.println("[number] - Select part");
            System.out.println("N - Next page");
            System.out.println("P - Previous page");
            System.out.println("B - Back");
            System.out.print("\nEnter choice: ");

            String choice = inputValidator.getStringInput("").toLowerCase();

            if (choice.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else if (choice.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (choice.equals("b")) {
                return null;
            } else {
                try {
                    int partNumber = Integer.parseInt(choice);
                    if (partNumber > 0 && partNumber <= parts.size()) {
                        return parts.get(partNumber - 1);
                    } else {
                        ConsoleHelper.printError("Invalid part number");
                        inputValidator.waitForEnter();
                    }
                } catch (NumberFormatException e) {
                    ConsoleHelper.printError("Invalid input");
                    inputValidator.waitForEnter();
                }
            }
        }
    }

    /**
     * Remove a component from the build
     */
    private void removeComponent() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("REMOVE COMPONENT");
        
        String[] options = {
            "CPU",
            "Motherboard",
            "Memory",
            "Video Card",
            "Storage (All)",
            "Case",
            "Power Supply",
            "CPU Cooler"
        };
        
        ConsoleHelper.printMenu(options);
        System.out.println();
        
        int choice = inputValidator.getMenuChoice(options.length);
        
        switch (choice) {
            case 1:
                build.setCpu(null);
                ConsoleHelper.printSuccess("CPU removed");
                break;
            case 2:
                build.setMotherboard(null);
                ConsoleHelper.printSuccess("Motherboard removed");
                break;
            case 3:
                build.setMemory(null);
                ConsoleHelper.printSuccess("Memory removed");
                break;
            case 4:
                build.setVideoCard(null);
                ConsoleHelper.printSuccess("Video Card removed");
                break;
            case 5:
                build.getStorageDevices().clear();
                ConsoleHelper.printSuccess("All storage removed");
                break;
            case 6:
                build.setPcCase(null);
                ConsoleHelper.printSuccess("Case removed");
                break;
            case 7:
                build.setPowerSupply(null);
                ConsoleHelper.printSuccess("Power Supply removed");
                break;
            case 8:
                build.setCpuCooler(null);
                ConsoleHelper.printSuccess("CPU Cooler removed");
                break;
            case 0:
                return;
        }
        
        inputValidator.waitForEnter();
    }
}
