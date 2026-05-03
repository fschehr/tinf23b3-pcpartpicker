package de.ase.pcpartpicker.part_assembly;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.RAM;

public class PerformanceTest {

    private Computer testComputer;
    private Computer testComputerNoGpu;

    @BeforeEach
    public void setup() {
        Manufacturer m = new Manufacturer(1, "TestManufacturer");
        Socket s = new Socket(1, "AM4");
        
        // Starke CPU und GPU für den normalen Test
        CPU cpu = new CPU(1, "Test CPU", 300.0, m, s, 3.5, 8, 4.5, true, 105);
        GPU gpu = new GPU(1, "Test GPU", 500.0, m, 1500.0, 2000.0, 16, 250);
        RAM ram = new RAM(1, "Test RAM", 100.0, m, 16, 3200);

        // Wir nutzen buildUnchecked(), da wir für den reinen Mathe-Test Case, PSU etc. weglassen können
        testComputer = new Computer.Builder()
                .setCPU(cpu)
                .setGPU(gpu)
                .setRAM(ram, 2)
                .buildUnchecked();

        testComputerNoGpu = new Computer.Builder()
                .setCPU(cpu)
                .setRAM(ram, 2)
                .buildUnchecked();
    }

    @Test
    public void testCalculateFPSWithGPU() {
        int[] fps = Performance.calculateFPS(testComputer);
        assertNotNull(fps);
        assertTrue(fps.length == 3, "Sollte 3 Auflösungen zurückgeben (1080p, 1440p, 4K)");
        
        // Da es eine stärkere GPU ist, sollten die 1080p FPS logischerweise höher sein als 4K
        assertTrue(fps[0] > fps[2], "1080p FPS sollten höher sein als 4K FPS");
        assertTrue(fps[0] > 0, "FPS sollten positiv sein");
    }

    @Test
    public void testCalculateFPSWithoutGPU() {
        int[] fps = Performance.calculateFPS(testComputerNoGpu);
        assertNotNull(fps);
        assertTrue(fps.length == 3);
        // Ohne GPU ist die Leistung extrem eingeschränkt
        assertTrue(fps[0] > fps[2]);
    }

    @Test
    public void testSchehrBenchmarkScore() {
        double score = Performance.schehrBenchmarkScore(testComputer);
        assertTrue(score > 0, "Score sollte größer als 0 sein");
        assertTrue(score <= 100.0, "Score darf 100 nicht überschreiten");
    }

    @Test
    public void testWeberBenchmarkScore() {
        double score = Performance.weberBenchmarkScore(testComputer);
        assertTrue(score > 0, "Score sollte größer als 0 sein");
        assertTrue(score <= 100.0, "Score darf 100 nicht überschreiten");
    }

    @Test
    public void testEnginBenchmarkScore() {
        double score = Performance.enginBenchmarkScore(testComputer);
        assertTrue(score > 0, "Score sollte größer als 0 sein");
        assertTrue(score <= 100.0, "Score darf 100 nicht überschreiten");
    }
}