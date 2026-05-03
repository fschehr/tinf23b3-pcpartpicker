package de.ase.pcpartpicker.part_assembly;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;

public class ComputerBuilderValidationTest {

    private Manufacturer m;
    private Socket socketAM4;
    private Socket socketLGA1700;
    private MotherboardFormFactor atxForm;
    private PSUFormFactor atxPsuForm;

    @BeforeEach
    public void setup() {
        m = new Manufacturer(1, "TestBrand");
        socketAM4 = new Socket(1, "AM4");
        socketLGA1700 = new Socket(2, "LGA1700");
        atxForm = new MotherboardFormFactor(1, "ATX");
        atxPsuForm = new PSUFormFactor(1, "ATX");
    }

    @Test
    public void testIncompatibleSocket() {
        CPU cpuAM4 = new CPU(1, "Ryzen", 200, m, socketAM4, 3.5, false, 65);
        Mainboard mbLGA1700 = new Mainboard(1, "IntelBoard", 150, m, socketLGA1700, atxForm, 4, 2, 4, 2);

        Computer.Builder builder = new Computer.Builder()
                .setCPU(cpuAM4)
                .setMainboard(mbLGA1700);

        String warning = builder.validate(mbLGA1700);
        assertNotNull(warning, "Sollte eine Warnung wegen inkompatiblem Sockel ausgeben.");
        assertTrue(warning.contains("nicht mit der CPU kompatibel"), "Fehlermeldung sollte auf CPU-Kompatibilität hinweisen.");
        
        // Vollständiger Build-Versuch sollte null zurückgeben
        assertNull(builder.build(), "Build sollte bei inkompatiblem Sockel fehlschlagen.");
    }

    @Test
    public void testPsuTooWeak() {
        CPU powerHungryCpu = new CPU(1, "i9", 500, m, socketLGA1700, 4.0, false, 250);
        PSU weakPsu = new PSU(1, "WeakPSU", 40, m, 150, atxPsuForm); // Nur 150W

        Computer.Builder builder = new Computer.Builder()
                .setCPU(powerHungryCpu)
                .setPSU(weakPsu);

        String warning = builder.validate(weakPsu);
        assertNotNull(warning, "Sollte eine Warnung wegen zu schwachem Netzteil ausgeben.");
        assertTrue(warning.contains("nicht genug Leistung"), "Fehlermeldung sollte fehlende Leistung erwähnen.");
    }

    @Test
    public void testNotEnoughRamSlots() {
        Mainboard mb = new Mainboard(1, "Board", 100, m, socketAM4, atxForm, 2, 2, 4, 2); // Nur 2 RAM-Slots
        RAM ram = new RAM(1, "RAM", 50, m, 8, 3200);

        Computer.Builder builder = new Computer.Builder()
                .setMainboard(mb)
                .setRAM(ram, 4); // 4 Module in 2 Slots

        String warning = builder.validate(mb);
        assertNotNull(warning, "Sollte warnen, wenn zu viele RAM-Module für das Mainboard gewählt wurden.");
        assertTrue(warning.contains("nicht genug RAM-Slots"), "Fehlermeldung sollte RAM-Slots erwähnen.");
    }
}