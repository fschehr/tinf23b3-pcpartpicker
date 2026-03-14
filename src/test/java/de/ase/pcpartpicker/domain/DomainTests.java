package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class DomainTests {
    
    @BeforeAll
    public static void setup() {
        System.out.println("Starting Domain Tests...");
    }

    @Test
    public void testCPUCreation() {
        CPU cpu = new CPU(1, "Test CPU", 199.99, new Manufacturer(1, "Test Manufacturer"), new Socket(1, "Test Socket"), 8, false, 95);
        assertNotNull(cpu);
        assertEquals("Test CPU", cpu.getName());
        assertEquals(199.99, cpu.getPrice());
        assertEquals("Test Manufacturer", cpu.getManufacturer().getName());
        assertEquals("Test Socket", cpu.getSocket().getName());
        assertEquals(8, cpu.getspeedGHz());
        assertFalse(cpu.hasIntegratedGraphics());
        assertEquals(95, cpu.getPowerConsumptionW());
    }

    @Test
    public void testGPUCreation() {
        GPU gpu = new GPU(1, "Test GPU", 299.99, new Manufacturer(1, "Test Manufacturer"), 16, 150);
        assertNotNull(gpu);
        assertEquals("Test GPU", gpu.getName());
        assertEquals(299.99, gpu.getPrice());
        assertEquals("Test Manufacturer", gpu.getManufacturer().getName());
        assertEquals(16, gpu.getVramGB());
        assertEquals(150, gpu.getPowerConsumptionW());
    }

    @Test
    public void testRAMCreation() {
        RAM ram = new RAM(1, "Test RAM", 79.99, new Manufacturer(1, "Test Manufacturer"), 16, 10);
        assertNotNull(ram);
        assertEquals("Test RAM", ram.getName());
        assertEquals(79.99, ram.getPrice());
        assertEquals("Test Manufacturer", ram.getManufacturer().getName());
        assertEquals(16, ram.getCapacityGB());
        assertEquals(15, ram.getPowerConsumptionW());
    }

    @Test
    public void testHDDCreation() {
        Storage storage = new HDD(1, "Test SSD", 129.99, new Manufacturer(1, "Test Manufacturer"), 512);
        assertNotNull(storage);
        assertEquals("Test SSD", storage.getName());
        assertEquals(129.99, storage.getPrice());
        assertEquals("Test Manufacturer", storage.getManufacturer().getName());
        assertEquals(512, storage.getCapacityGB());
        assertEquals(10, storage.getPowerConsumptionW());
    }

    @Test
    public void testSSDCreation() {
        Storage storage = new SSD(1, "Test HDD", 89.99, new Manufacturer(1, "Test Manufacturer"), 1024);
        assertNotNull(storage);
        assertEquals("Test HDD", storage.getName());
        assertEquals(89.99, storage.getPrice());
        assertEquals("Test Manufacturer", storage.getManufacturer().getName());
        assertEquals(1024, storage.getCapacityGB());
        assertEquals(10, storage.getPowerConsumptionW());
    }

    @Test
    public void testM2Creation() {
        Storage storage = new M2SSD(1, "Test M.2", 149.99, new Manufacturer(1, "Test Manufacturer"), 2048);
        assertNotNull(storage);
        assertEquals("Test M.2", storage.getName());
        assertEquals(149.99, storage.getPrice());
        assertEquals("Test Manufacturer", storage.getManufacturer().getName());
        assertEquals(2048, storage.getCapacityGB());
        assertEquals(10, storage.getPowerConsumptionW());
    }

    @Test
    public void testCaseCreation() {
        Case computerCase = new Case(1, "Test Case", 59.99, new Manufacturer(1, "Test Manufacturer"), new MotherboardFormFactor(1, "ATX"), new PSUFormFactor(6, "SFTX"), true, 10);
        assertNotNull(computerCase);
        assertEquals("Test Case", computerCase.getName());
        assertEquals(59.99, computerCase.getPrice());
        assertEquals("Test Manufacturer", computerCase.getManufacturer().getName());
        assertEquals("ATX", computerCase.getMotherboardFormFactor().getName());
        assertEquals(10*5, computerCase.getPowerConsumptionW());
        assertTrue(computerCase.hasWindow());
        assertEquals("SFTX", computerCase.getPSUFormFactor().getName());
        assertEquals(10, computerCase.getFanSlots());
    }

    @Test
    public void testPSUCreation() {
        PSU psu = new PSU(1, "Test PSU", 149.99, new Manufacturer(1, "Test Manufacturer"), 650, new PSUFormFactor(69, "ATX"));
        assertNotNull(psu);
        assertEquals("Test PSU", psu.getName());
        assertEquals(149.99, psu.getPrice());
        assertEquals("Test Manufacturer", psu.getManufacturer().getName());
        assertEquals(650, psu.getWattage());
        assertEquals("ATX", psu.getFormFactor().getName());
        assertEquals(psu.getPowerConsumptionW(), psu.getWattage() * 1.2 - psu.getWattage());
    }

    @Test
    public void testMainboardCreation() {
        Mainboard mainboard = new Mainboard(1, "Test Mainboard", 199.99, new Manufacturer(1, "Test Manufacturer"), new Socket(1, "Test Socket"), new MotherboardFormFactor(1, "ATX"), 4, 2, 4, 2);
        assertNotNull(mainboard);
        assertEquals("Test Mainboard", mainboard.getName());
        assertEquals(199.99, mainboard.getPrice());
        assertEquals("Test Manufacturer", mainboard.getManufacturer().getName());
        assertEquals("Test Socket", mainboard.getSocket().getName());
        assertEquals("ATX", mainboard.getFormFactor().getName());
        assertEquals(4, mainboard.getRamSlots());
        assertEquals(2, mainboard.getPcieSlots());
        assertEquals(4, mainboard.getSataSlots());
    }

}
