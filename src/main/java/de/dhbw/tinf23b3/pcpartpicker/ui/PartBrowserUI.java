package de.dhbw.tinf23b3.pcpartpicker.ui;

import de.dhbw.tinf23b3.pcpartpicker.data.PartRepository;
import de.dhbw.tinf23b3.pcpartpicker.model.*;
import de.dhbw.tinf23b3.pcpartpicker.util.ConsoleHelper;
import de.dhbw.tinf23b3.pcpartpicker.util.InputValidator;

import java.util.List;

/**
 * UI for browsing PC parts
 */
public class PartBrowserUI {
    private final PartRepository repository;
    private final InputValidator inputValidator;
    private static final int ITEMS_PER_PAGE = 15;

    public PartBrowserUI(PartRepository repository, InputValidator inputValidator) {
        this.repository = repository;
        this.inputValidator = inputValidator;
    }

    /**
     * Start the part browser
     */
    public void start() {
        boolean browsing = true;
        
        while (browsing) {
            ConsoleHelper.clearScreen();
            ConsoleHelper.printHeader("PART BROWSER");
            
            String[] categories = {
                "CPUs (" + repository.getCpus().size() + ")",
                "Motherboards (" + repository.getMotherboards().size() + ")",
                "Memory (" + repository.getMemoryModules().size() + ")",
                "Storage (" + repository.getStorageDevices().size() + ")",
                "Video Cards (" + repository.getVideoCards().size() + ")",
                "Cases (" + repository.getCases().size() + ")",
                "Power Supplies (" + repository.getPowerSupplies().size() + ")",
                "CPU Coolers (" + repository.getCpuCoolers().size() + ")"
            };
            
            System.out.println();
            ConsoleHelper.printMenu(categories);
            System.out.println();
            
            int choice = inputValidator.getMenuChoice(categories.length);
            
            if (choice == 0) {
                browsing = false;
            } else {
                browseCategory(choice);
            }
        }
    }

    /**
     * Browse a specific category
     */
    private void browseCategory(int category) {
        switch (category) {
            case 1:
                browseParts("CPUs", repository.getCpus());
                break;
            case 2:
                browseParts("Motherboards", repository.getMotherboards());
                break;
            case 3:
                browseParts("Memory", repository.getMemoryModules());
                break;
            case 4:
                browseParts("Storage", repository.getStorageDevices());
                break;
            case 5:
                browseParts("Video Cards", repository.getVideoCards());
                break;
            case 6:
                browseParts("Cases", repository.getCases());
                break;
            case 7:
                browseParts("Power Supplies", repository.getPowerSupplies());
                break;
            case 8:
                browseParts("CPU Coolers", repository.getCpuCoolers());
                break;
        }
    }

    /**
     * Browse a list of parts with pagination
     */
    private <T extends PCPart> void browseParts(String categoryName, List<T> parts) {
        if (parts.isEmpty()) {
            ConsoleHelper.printError("No parts available in this category.");
            inputValidator.waitForEnter();
            return;
        }

        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) parts.size() / ITEMS_PER_PAGE);
        boolean viewing = true;

        while (viewing) {
            ConsoleHelper.clearScreen();
            ConsoleHelper.printHeader(categoryName.toUpperCase());

            int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, parts.size());

            ConsoleHelper.printPartList(parts, startIndex, endIndex);
            ConsoleHelper.printPaginationInfo(currentPage, totalPages, ITEMS_PER_PAGE, parts.size());

            System.out.println("\nOptions:");
            System.out.println("N - Next page");
            System.out.println("P - Previous page");
            System.out.println("V [number] - View details");
            System.out.println("S - Search");
            System.out.println("F - Filter by price");
            System.out.println("B - Back");
            System.out.print("\nEnter choice: ");

            String choice = inputValidator.getStringInput("").toLowerCase();

            switch (choice) {
                case "n":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        ConsoleHelper.printInfo("Already on last page");
                        inputValidator.waitForEnter();
                    }
                    break;
                case "p":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        ConsoleHelper.printInfo("Already on first page");
                        inputValidator.waitForEnter();
                    }
                    break;
                case "s":
                    searchParts(categoryName, parts);
                    currentPage = 1;
                    break;
                case "f":
                    filterPartsByPrice(categoryName, parts);
                    currentPage = 1;
                    break;
                case "b":
                    viewing = false;
                    break;
                default:
                    if (choice.startsWith("v ")) {
                        try {
                            int partNumber = Integer.parseInt(choice.substring(2).trim());
                            if (partNumber > 0 && partNumber <= parts.size()) {
                                viewPartDetails(parts.get(partNumber - 1));
                            } else {
                                ConsoleHelper.printError("Invalid part number");
                                inputValidator.waitForEnter();
                            }
                        } catch (NumberFormatException e) {
                            ConsoleHelper.printError("Invalid command");
                            inputValidator.waitForEnter();
                        }
                    } else {
                        ConsoleHelper.printError("Invalid choice");
                        inputValidator.waitForEnter();
                    }
            }
        }
    }

    /**
     * View detailed information about a part
     */
    private <T extends PCPart> void viewPartDetails(T part) {
        ConsoleHelper.clearScreen();
        ConsoleHelper.printHeader("PART DETAILS");
        System.out.println(part.getDetailedDescription());
        inputValidator.waitForEnter();
    }

    /**
     * Search parts by name
     */
    private <T extends PCPart> void searchParts(String categoryName, List<T> parts) {
        String query = inputValidator.getStringInput("\nEnter search term: ");
        List<T> results = repository.searchByName(parts, query);
        
        if (results.isEmpty()) {
            ConsoleHelper.printInfo("No parts found matching '" + query + "'");
            inputValidator.waitForEnter();
        } else {
            browseParts(categoryName + " - Search: " + query, results);
        }
    }

    /**
     * Filter parts by price range
     */
    private <T extends PCPart> void filterPartsByPrice(String categoryName, List<T> parts) {
        System.out.println("\nEnter price range:");
        double minPrice = inputValidator.getDoubleInput("Minimum price: $", 0, 100000);
        double maxPrice = inputValidator.getDoubleInput("Maximum price: $", minPrice, 100000);
        
        List<T> filtered = repository.filterByPrice(parts, minPrice, maxPrice);
        
        if (filtered.isEmpty()) {
            ConsoleHelper.printInfo("No parts found in price range $" + minPrice + " - $" + maxPrice);
            inputValidator.waitForEnter();
        } else {
            browseParts(categoryName + String.format(" - $%.0f-$%.0f", minPrice, maxPrice), filtered);
        }
    }
}
