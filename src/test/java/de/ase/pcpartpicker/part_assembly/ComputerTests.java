package de.ase.pcpartpicker.part_assembly;

import de.ase.pcpartpicker.domain.*;
import de.ase.pcpartpicker.domain.HelperClasses.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.*;

public class ComputerTests {

    @BeforeAll
    public static void setup() {
        System.out.println("Starting Computer Tests...");
    }

    @Test
    public void testWrongComputer() {
        Computer computer = new Computer.Builder()
            .setCPU(null)
            .setGPU(null)
            .setMainboard(null)
            .setRAM(null, 0)
            .setPSU(null)
            .setComputerCase(null)
            .setStorageDevices(new Storage[] {})
            .build();

        assertNull(computer);
    }

    @Test
    public void testComputerCreation() {

        PSUFormFactor psuFormFactor = new PSUFormFactor(0, "SFTX");
        MotherboardFormFactor motherboardFormFactor = new MotherboardFormFactor(34, "ATX");
        Socket socket = new Socket(1, "AM4");
        
        Computer computer = new Computer.Builder()
            .setCPU(new CPU(1, "Test CPU", 199.99, new Manufacturer(1, "Test Manufacturer"), socket, 8, false, 95))
            .setGPU(new GPU(1, "Test GPU", 299.99, new Manufacturer(1, "Test Manufacturer"), 16, 150))
            .setMainboard(new Mainboard(1, "Test Mainboard", 149.99, new Manufacturer(1, "Test Manufacturer"), socket, motherboardFormFactor, 4, 3, 6, 2))
            .setRAM(new RAM(1, "Test RAM", 79.99, new Manufacturer(1, "Test Manufacturer"), 16, 15), 2)
            .setPSU(new PSU(1, "Test PSU", 89.99, new Manufacturer(1, "Test Manufacturer"), 650, psuFormFactor))
            .setComputerCase(new Case(1, "Test Case", 59.99, new Manufacturer(1, "Test Manufacturer"), motherboardFormFactor, psuFormFactor, true, 9))
            .setStorageDevices(new Storage[] {
                new SSD(1, "Test SSD", 129.99, new Manufacturer(1, "Test Manufacturer"), 512),
                new HDD(2, "Test HDD", 79.99, new Manufacturer(1, "Test Manufacturer"), 1024)
            })
            .build();

        assertNotNull(computer);
        //computer.printConfiguration();
    }
    
}
