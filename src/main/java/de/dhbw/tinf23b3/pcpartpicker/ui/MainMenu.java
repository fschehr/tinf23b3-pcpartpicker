package de.dhbw.tinf23b3.pcpartpicker.ui;

import de.dhbw.tinf23b3.pcpartpicker.business.CompatibilityChecker;
import de.dhbw.tinf23b3.pcpartpicker.data.PartRepository;
import de.dhbw.tinf23b3.pcpartpicker.model.PCBuild;
import de.dhbw.tinf23b3.pcpartpicker.util.ConsoleHelper;
import de.dhbw.tinf23b3.pcpartpicker.util.InputValidator;

/**
 * Main menu and application entry point UI
 */
public class MainMenu {
    private final PartRepository repository;
    private final CompatibilityChecker compatibilityChecker;
    private final InputValidator inputValidator;
    private PCBuild currentBuild;
    private boolean running;

    public MainMenu(PartRepository repository) {
        this.repository = repository;
        this.compatibilityChecker = new CompatibilityChecker();
        this.inputValidator = new InputValidator();
        this.currentBuild = new PCBuild("My PC Build");
        this.running = true;
    }

    /**
     * Start the main menu loop
     */
    public void start() {
        ConsoleHelper.clearScreen();
        printWelcomeMessage();
        
        while (running) {
            showMainMenu();
        }
        
        printGoodbyeMessage();
    }

    /**
     * Display the main menu
     */
    private void showMainMenu() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("PC PART PICKER - MAIN MENU");
        
        System.out.println("Current Build: " + currentBuild.getBuildName());
        System.out.println("Build Status: " + (currentBuild.isComplete() ? "Complete" : "Incomplete"));
        System.out.println("Total Price: " + ConsoleHelper.formatPrice(currentBuild.getTotalPrice()));
        ConsoleHelper.printSeparator();
        
        String[] options = {
            "Configure PC Build",
            "Browse Parts",
            "View Build Summary",
            "Check Compatibility",
            "New Build",
            "About"
        };
        
        System.out.println();
        ConsoleHelper.printMenu(options);
        System.out.println();
        
        int choice = inputValidator.getMenuChoice(options.length);
        handleMainMenuChoice(choice);
    }

    /**
     * Handle main menu selection
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                BuildConfiguratorUI configurator = new BuildConfiguratorUI(repository, currentBuild, inputValidator);
                configurator.start();
                break;
            case 2:
                PartBrowserUI browser = new PartBrowserUI(repository, inputValidator);
                browser.start();
                break;
            case 3:
                showBuildSummary();
                break;
            case 4:
                checkCompatibility();
                break;
            case 5:
                createNewBuild();
                break;
            case 6:
                showAbout();
                break;
            case 0:
                running = false;
                break;
            default:
                ConsoleHelper.printError("Invalid choice!");
                inputValidator.waitForEnter();
        }
    }

    /**
     * Show build summary
     */
    private void showBuildSummary() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("BUILD SUMMARY");
        System.out.println(currentBuild.getSummary());
        inputValidator.waitForEnter();
    }

    /**
     * Check build compatibility
     */
    private void checkCompatibility() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("COMPATIBILITY CHECK");
        
        var warnings = compatibilityChecker.getCompatibilityWarnings(currentBuild);
        
        if (warnings.isEmpty()) {
            ConsoleHelper.printSuccess("No compatibility issues found!");
            System.out.println("\nYour build looks good!");
        } else {
            ConsoleHelper.printWarning("Compatibility issues detected:");
            System.out.println();
            for (String warning : warnings) {
                System.out.println("• " + warning);
            }
        }
        
        System.out.println("\nEstimated Power Consumption: " + 
            compatibilityChecker.estimateTotalWattage(currentBuild) + "W");
        
        if (currentBuild.getPowerSupply() != null && currentBuild.getPowerSupply().getWattage() != null) {
            int psuWattage = currentBuild.getPowerSupply().getWattage();
            int estimated = compatibilityChecker.estimateTotalWattage(currentBuild);
            int headroom = (int)((psuWattage - estimated) / (double)estimated * 100);
            System.out.println("Power Supply Headroom: " + headroom + "%");
        }
        
        inputValidator.waitForEnter();
    }

    /**
     * Create a new build
     */
    private void createNewBuild() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("NEW BUILD");
        
        if (!currentBuild.isComplete() && currentBuild.getTotalPrice() > 0) {
            boolean confirm = inputValidator.getYesNoInput(
                "Current build will be lost. Continue?");
            if (!confirm) {
                return;
            }
        }
        
        String buildName = inputValidator.getStringInput("Enter build name: ");
        if (buildName.isEmpty()) {
            buildName = "My PC Build";
        }
        
        currentBuild = new PCBuild(buildName);
        ConsoleHelper.printSuccess("New build created: " + buildName);
        inputValidator.waitForEnter();
    }

    /**
     * Show about information
     */
    private void showAbout() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("ABOUT PC PART PICKER");
        
        System.out.println("PC Part Picker - Terminal Edition");
        System.out.println("Version 1.0");
        System.out.println();
        System.out.println("A terminal-based application for building and configuring");
        System.out.println("custom PC builds with compatibility checking.");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  • Browse thousands of PC components");
        System.out.println("  • Build custom PC configurations");
        System.out.println("  • Check component compatibility");
        System.out.println("  • Calculate total build cost");
        System.out.println("  • Power consumption estimation");
        System.out.println();
        System.out.println("Data Source: PC Part Dataset");
        System.out.println("(https://github.com/docyx/pc-part-dataset)");
        System.out.println();
        System.out.println("Developed for DHBW TINF23B3 - Advanced Software Engineering");
        
        inputValidator.waitForEnter();
    }

    /**
     * Print welcome message
     */
    private void printWelcomeMessage() {
        ConsoleHelper.printHeader("WELCOME TO PC PART PICKER");
        System.out.println("Build your dream PC with confidence!");
        System.out.println();
        System.out.println("Loading parts database...");
        System.out.println("Loaded " + repository.getTotalPartCount() + " components");
        inputValidator.waitForEnter();
    }

    /**
     * Print goodbye message
     */
    private void printGoodbyeMessage() {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("THANK YOU");
        System.out.println("Thank you for using PC Part Picker!");
        System.out.println("Happy building! 🔧💻");
        System.out.println();
    }
}
