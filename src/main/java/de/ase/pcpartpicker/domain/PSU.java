package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.PsuFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

/**
 * Klasse für die PSU-Komponente.
 * @param id Eindeutige ID der PSU
 * @param name Name der PSU
 * @param manufacturer Hersteller der PSU
 * @param capacityGb Kapazität der PSU in GB
 * @param speedMhz Geschwindigkeit der PSU in MHz
 * @param price Preis der PSU
 * @author Fabio
 */
public class PSU extends Component {

    private final int wattage;
    private final PsuFormFactor formFactor;

    public PSU(int id, Type type, String name, double price, Manufacturer manufacturer, int wattage, PsuFormFactor formFactor) {
        super(id, type, name, price, manufacturer);
        this.wattage = wattage;
        this.formFactor = formFactor;
    }

    public int getWattage() {
        return wattage;
    }

    public PsuFormFactor getFormFactor() {
        return formFactor;
    }
}