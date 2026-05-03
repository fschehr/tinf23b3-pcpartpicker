package de.ase.pcpartpicker.adapters.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;

public class ComputerDraftTest {

    private ComputerDraft draft;
    private Manufacturer manufacturer;

    @BeforeEach
    public void setup() {
        draft = new ComputerDraft();
        draft.startNewDraft();
        manufacturer = new Manufacturer(1, "TestManufacturer");
    }

    @Test
    public void testInitialState() {
        assertFalse(draft.hasUnsavedChanges(), "Ein neuer Entwurf sollte keine ungespeicherten Änderungen haben.");
        assertNull(draft.getCPU(), "CPU sollte anfangs null sein.");
        assertEquals(0.0, draft.getTotalPrice(), "Gesamtpreis sollte 0 sein.");
    }

    @Test
    public void testSetCpuChangesState() {
        Socket socket = new Socket(1, "AM4");
        CPU cpu = new CPU(1, "Ryzen", 250.0, manufacturer, socket, 3.5, false, 65);
        
        draft.setCpu(cpu);
        
        assertTrue(draft.hasUnsavedChanges(), "Das Flag für ungespeicherte Änderungen sollte true sein.");
        assertEquals(cpu, draft.getCPU());
        assertEquals(250.0, draft.getTotalPrice(), 0.01, "Der Preis sollte der CPU entsprechen.");
    }

    @Test
    public void testClearDraft() {
        Socket socket = new Socket(1, "AM4");
        CPU cpu = new CPU(1, "Ryzen", 250.0, manufacturer, socket, 3.5, false, 65);
        draft.setCpu(cpu);
        
        draft.clear();
        
        assertNull(draft.getBuilder(), "Builder sollte nach clear null sein.");
        assertNull(draft.getEditingComputerId(), "Editing ID sollte null sein.");
    }

    @Test
    public void testTotalPowerConsumption() {
        Socket socket = new Socket(1, "AM4");
        MotherboardFormFactor mbff = new MotherboardFormFactor(1, "ATX");
        PSUFormFactor psuff = new PSUFormFactor(1, "ATX");

        CPU cpu = new CPU(1, "CPU", 100.0, manufacturer, socket, 3.5, false, 100);
        Case pcCase = new Case(1, "Case", 50.0, manufacturer, mbff, psuff, true, 3); // 3 * 5W = 15W

        draft.setCpu(cpu);
        draft.setComputerCase(pcCase);

        int expectedPower = 100 + 15;
        assertEquals(expectedPower, draft.getTotalPowerConsumption(), "Die Gesamtleistung muss der Summe der Komponenten entsprechen.");
    }
}