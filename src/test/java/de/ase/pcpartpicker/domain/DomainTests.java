package de.ase.pcpartpicker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

public class DomainTests {
    
    @BeforeAll
    public static void setup() {
        System.out.println("Starting Domain Tests...");
    }

    @Test
    public void testCPUCreation() {
        CPU cpu = new CPU(1, "Test CPU", 199.99, new Manufacturer(1, "Test Manufacturer"), new Socket(1, "Test Socket"), 3.8, 5.2, false, 95);
        assertNotNull(cpu);
        assertEquals("Test CPU", cpu.getName());
        assertEquals(199.99, cpu.getPrice());
        assertEquals("Test Manufacturer", cpu.getManufacturer().getName());
        assertEquals("Test Socket", cpu.getSocket().getName());
        assertEquals(3.8, cpu.getSpeedGHz());
        assertEquals(5.2, cpu.getBoostClockGHz());
        assertFalse(cpu.hasIntegratedGraphics());
        assertEquals(95, cpu.getPowerConsumptionW());
    }

    @Test
    public void testGPUCreation() {
        GPU gpu = new GPU(1, "Test GPU", 299.99, new Manufacturer(1, "Test Manufacturer"), 2220, 3290.0, 16, 190);
        assertNotNull(gpu);
        assertEquals("Test GPU", gpu.getName());
        assertEquals(299.99, gpu.getPrice());
        assertEquals("Test Manufacturer", gpu.getManufacturer().getName());
        assertEquals(2220, gpu.getCoreClockMHz());
        assertEquals(3290.0, gpu.getBoostClockMHz());
        assertEquals(16, gpu.getVramGB());
        assertEquals(190, gpu.getPowerConsumptionW());
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


    @Test
    public void testHelperTableEqualsAndHashCode() {
        Manufacturer m1 = new Manufacturer(1, "Intel");
        Manufacturer m2 = new Manufacturer(1, "Intel");
        Manufacturer m3 = new Manufacturer(2, "AMD");

        // Teste Gleichheit und HashCode
        assertEquals(m1, m2, "Hersteller mit gleicher ID und Name sollten gleich sein.");
        assertEquals(m1.hashCode(), m2.hashCode(), "HashCodes sollten übereinstimmen.");
        
        // Teste Ungleichheit
        assertNotEquals(m1, m3, "Unterschiedliche Hersteller sollten nicht gleich sein.");
        assertNotEquals(m1, null, "Sollte nicht gleich null sein.");
        assertNotEquals(m1, new Object(), "Sollte nicht gleich einem komplett anderen Objekt sein.");
    }

    @Test
    public void testComponentToStringFormatting() {
        Manufacturer m = new Manufacturer(1, "TestBrand");
        Socket s = new Socket(1, "AM4");
        MotherboardFormFactor mff = new MotherboardFormFactor(1, "ATX");
        PSUFormFactor pff = new PSUFormFactor(1, "ATX");

        // CPU toString Test
        CPU cpu = new CPU(1, "TestCPU", 100.0, m, s, 3.0, 4, 4.0, true, 65);
        String cpuString = cpu.toString();
        assertTrue(cpuString.contains("TestCPU"), "Name fehlt in CPU toString");
        assertTrue(cpuString.contains("4,00 GHz"), "Boost Clock fehlt in CPU toString");

        // GPU toString Test
        GPU gpu = new GPU(1, "TestGPU", 200.0, m, 1000, 1500.0, 8, 150);
        String gpuString = gpu.toString();
        assertTrue(gpuString.contains("1500 MHz"), "Boost Clock fehlt in GPU toString");
        assertTrue(gpuString.contains("8 GB VRAM"), "VRAM fehlt in GPU toString");

        // RAM toString Test
        RAM ram = new RAM(1, "TestRAM", 50.0, m, 16, 3200);
        assertTrue(ram.toString().contains("16 GB"), "Kapazität fehlt in RAM toString");

        // Mainboard toString Test
        Mainboard mb = new Mainboard(1, "TestMB", 100.0, m, s, mff, 4, 2, 4, 2);
        assertTrue(mb.toString().contains("4 RAM-Slots"), "RAM-Slots fehlen in Mainboard toString");

        // PSU toString Test
        PSU psu = new PSU(1, "TestPSU", 80.0, m, 500, pff);
        assertTrue(psu.toString().contains("500 W"), "Wattzahl fehlt in PSU toString");

        // Case toString Test
        Case pcCase = new Case(1, "TestCase", 60.0, m, mff, pff, true, 3);
        assertTrue(pcCase.toString().contains("ja"), "Fenster-Info fehlt in Case toString");

        // Storage toString Test
        SSD ssd = new SSD(1, "TestSSD", 50.0, m, 500);
        assertTrue(ssd.toString().contains("500 GB"), "Kapazität fehlt in SSD toString");
    }
}
