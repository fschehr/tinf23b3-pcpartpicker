package de.dhbw.tinf23b3.pcpartpicker.util;

import de.dhbw.tinf23b3.pcpartpicker.model.PCPart;

import java.util.List;

/**
 * Utility class for formatted console output
 */
public class ConsoleHelper {
    
    /**
     * Clear the console (works on most terminals)
     */
    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Print a header with decoration
     */
    public static void printHeader(String title) {
        int width = 60;
        System.out.println("\n" + "=".repeat(width));
        System.out.println(centerText(title, width));
        System.out.println("=".repeat(width) + "\n");
    }

    /**
     * Print a section header
     */
    public static void printSectionHeader(String title) {
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
    }

    /**
     * Center text within a given width
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    /**
     * Print a list of parts with numbering
     */
    public static <T extends PCPart> void printPartList(List<T> parts, int startIndex, int endIndex) {
        if (parts.isEmpty()) {
            System.out.println("No parts available.");
            return;
        }

        endIndex = Math.min(endIndex, parts.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            System.out.printf("%3d. %s\n", i + 1, parts.get(i).getShortDescription());
        }
    }

    /**
     * Print pagination info
     */
    public static void printPaginationInfo(int currentPage, int totalPages, int itemsPerPage, int totalItems) {
        int startItem = (currentPage - 1) * itemsPerPage + 1;
        int endItem = Math.min(currentPage * itemsPerPage, totalItems);
        
        System.out.println("\n" + "-".repeat(60));
        System.out.printf("Showing %d-%d of %d items | Page %d of %d\n", 
            startItem, endItem, totalItems, currentPage, totalPages);
        System.out.println("-".repeat(60));
    }

    /**
     * Print menu options
     */
    public static void printMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s\n", i + 1, options[i]);
        }
        System.out.println("0. " + (options.length > 1 ? "Back" : "Exit"));
    }

    /**
     * Print error message
     */
    public static void printError(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    /**
     * Print success message
     */
    public static void printSuccess(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    /**
     * Print warning message
     */
    public static void printWarning(String message) {
        System.out.println("\n[WARNING] " + message);
    }

    /**
     * Print info message
     */
    public static void printInfo(String message) {
        System.out.println("\n[INFO] " + message);
    }

    /**
     * Print a box around text
     */
    public static void printBox(String text) {
        int length = text.length() + 4;
        System.out.println("+" + "-".repeat(length) + "+");
        System.out.println("|  " + text + "  |");
        System.out.println("+" + "-".repeat(length) + "+");
    }

    /**
     * Format price with currency
     */
    public static String formatPrice(double price) {
        return String.format("$%.2f", price);
    }

    /**
     * Print a separator line
     */
    public static void printSeparator() {
        System.out.println("-".repeat(60));
    }

    /**
     * Print empty lines for spacing
     */
    public static void printEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }
}
