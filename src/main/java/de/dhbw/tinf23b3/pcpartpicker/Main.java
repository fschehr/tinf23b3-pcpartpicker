package de.dhbw.tinf23b3.pcpartpicker;

import de.dhbw.tinf23b3.pcpartpicker.data.PartRepository;
import de.dhbw.tinf23b3.pcpartpicker.ui.MainMenu;

/**
 * Main entry point for the PC Part Picker application
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Initialize repository
            PartRepository repository = new PartRepository();
            
            // If no data files are found, generate sample data
            if (repository.getTotalPartCount() == 0) {
                System.out.println("No data files found. Generating sample data...");
                repository.generateSampleData();
            }
            
            // Start the main menu
            MainMenu mainMenu = new MainMenu(repository);
            mainMenu.start();
            
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
