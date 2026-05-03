package de.ase.pcpartpicker.part_assembly;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;

public class BottleneckTest {

    private Manufacturer m;
    private Socket s;

    @BeforeEach
    public void setup() {
        m = new Manufacturer(1, "TestBrand");
        s = new Socket(1, "AM4");
    }

    @Test
    public void testCalculateBottleneck_CpuWeak() {
        // Extrem schwache CPU, aber extrem starke GPU
        CPU weakCpu = new CPU(1, "Weak CPU", 50.0, m, s, 1.2, 2, 1.5, false, 35);
        GPU strongGpu = new GPU(1, "Strong GPU", 800.0, m, 2000, 2500.0, 16, 300);
        RAM ram = new RAM(1, "RAM", 100.0, m, 16, 3200);
        M2SSD storage = new M2SSD(1, "Fast SSD", 100.0, m, 1000);

        Computer pc = new Computer.Builder()
                .setCPU(weakCpu)
                .setGPU(strongGpu)
                .setRAM(ram, 2)
                .setStorageDevices(new Storage[]{storage})
                .buildUnchecked();

        BottleneckResult result = Bottleneck.calculateBottleneck(pc);
        
        assertNotNull(result, "Das Resultat darf nicht null sein");
        assertTrue(result.hasBottleneck(), "Ein Bottleneck sollte bei dieser Unwucht erkannt werden");
    }
    
    @Test
    public void testCalculateBottleneck_NoGpuAndHdd() {
        // Starker PC, aber keine GPU und nur eine alte HDD (zieht den Storage Score massiv runter)
        CPU cpu = new CPU(1, "Strong CPU", 300.0, m, s, 3.8, 8, 4.5, true, 105);
        RAM ram = new RAM(1, "RAM", 100.0, m, 16, 3200);
        HDD hdd = new HDD(1, "Old HDD", 30.0, m, 1000);

        Computer pc = new Computer.Builder()
                .setCPU(cpu)
                .setRAM(ram, 2)
                .setStorageDevices(new Storage[]{hdd})
                .buildUnchecked();

        BottleneckResult result = Bottleneck.calculateBottleneck(pc);
        
        assertNotNull(result);
    }
}