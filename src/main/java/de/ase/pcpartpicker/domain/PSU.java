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
        super(id, name, price, manufacturer, calculatePowerConsumption(wattage));
        this.wattage = wattage;
        this.formFactor = formFactor;
    }

    // Berechnet den Stromverbrauch der PSU selbst basierend auf der Leistung und einem angenommenen Wirkungsgrad von 80 PLUS (20% Verlust)
    private static int calculatePowerConsumption(int wattage) {
        double steckdoseVerbrauch = wattage * 1.2; // Annahme: 20% Verlust bei der Stromumwandlung bei 80 PLUS Zertifizierung
        return (int) steckdoseVerbrauch - wattage;
    }

    public int getWattage() {
        return wattage;
    }

    public PSUFormFactor getFormFactor() {
        return formFactor;
    }
}