package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherBoardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PsuFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

public class Case extends Component {

    private final MotherBoardFormFactor motherBoardFormFactor;
    private final PsuFormFactor psuFormFactor;
    private final boolean hasWindow;
    private final int fanSlots;

    public Case(int id, Type type, String name, double price, Manufacturer manufacturer, MotherBoardFormFactor motherBoardFormFactor, PsuFormFactor psuFormFactor, boolean hasWindow, int fanSlots) {
        super(id, type, name, price, manufacturer);
        this.motherBoardFormFactor = motherBoardFormFactor;
        this.psuFormFactor = psuFormFactor;
        this.hasWindow = hasWindow;
        this.fanSlots = fanSlots;
    }

    public MotherBoardFormFactor getMotherBoardFormFactor() {
        return motherBoardFormFactor;
    }
    
    public PsuFormFactor getPsuFormFactor() {
        return psuFormFactor;
    }
    
    public boolean hasWindow() {
        return hasWindow;
    }

    public int getFanSlots() {
        return fanSlots;
    }
    
}
