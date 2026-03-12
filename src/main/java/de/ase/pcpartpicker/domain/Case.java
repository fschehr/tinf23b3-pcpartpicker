package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;

/**
 * Klasse für die Case-Komponente.
 * @param id Eindeutige ID des Cases
 * @param name Name des Cases
 * @param manufacturer Hersteller des Cases
 * @param motherBoardFormFactor Unterstütztes Motherboard-Formfaktor des Cases
 * @param psuFormFactor Unterstütztes PSU-Formfaktor des Cases
 * @param hasWindow Gibt an, ob das Case ein Fenster hat
 * @param fanSlots Anzahl der Lüfterplätze im Case
 * @param price Preis des Cases
 * @author Tuluhan
 */
public class Case extends Component {

    private final MotherboardFormFactor motherboardFormFactor;
    private final PSUFormFactor psuFormFactor;
    private final boolean hasWindow;
    private final int fanSlots;

    public Case(int id, String name, double price, Manufacturer manufacturer, MotherboardFormFactor motherboardFormFactor, PSUFormFactor psuFormFactor, boolean hasWindow, int fanSlots) {
        super(id, name, price, manufacturer, fanSlots * 5); // Für jeden Lüfter (der am Gehäuse angebracht werden kann) wird ein zusätzlicher Stromverbrauch von 5 Watt angenommen
        this.motherboardFormFactor = motherboardFormFactor;
        this.psuFormFactor = psuFormFactor;
        this.hasWindow = hasWindow;
        this.fanSlots = fanSlots;
    }

    public MotherboardFormFactor getMotherboardFormFactor() {
        return motherboardFormFactor;
    }
    
    public PSUFormFactor getPSUFormFactor() {
        return psuFormFactor;
    }
    
    public boolean hasWindow() {
        return hasWindow;
    }

    public int getFanSlots() {
        return fanSlots;
    }
}
