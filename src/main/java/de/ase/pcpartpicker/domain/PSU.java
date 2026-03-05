package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;

/**
 * Klasse für die PSU-Komponente.
 * @param id Eindeutige ID der PSU
 * @param name Name der PSU
 * @param manufacturer Hersteller der PSU
 * @param wattage Leistung der PSU in Watt
 * @param formFactor Formfaktor der PSU
 * @param price Preis der PSU
 * @author Fabio
 */
public class PSU extends Component {

    private final int wattage;
    private final PSUFormFactor formFactor;

    public PSU(int id, String name, double price, Manufacturer manufacturer, int wattage, PSUFormFactor formFactor) {
        super(id, name, price, manufacturer);
        this.wattage = wattage;
        this.formFactor = formFactor;
    }

    public int getWattage() {
        return wattage;
    }

    public PSUFormFactor getFormFactor() {
        return formFactor;
    }
}