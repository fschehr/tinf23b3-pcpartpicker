package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.PsuFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

public class Case extends Component {

    private final PsuFormFactor psuFormFactor;

    public Case(int id, Type type, String name, double price, Manufacturer manufacturer, PsuFormFactor psuFormFactor) {
        super(id, type, name, price, manufacturer);
        this.psuFormFactor = psuFormFactor;
    }

    public PsuFormFactor getPsuFormFactor() {
        return psuFormFactor;
    }
    
}
