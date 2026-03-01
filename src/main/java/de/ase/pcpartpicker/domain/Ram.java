package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Type;

/**
 * Klasse für die RAM-Komponente.
 * @param id Eindeutige ID des RAMs
 * @param name Name des RAMs
 * @param manufacturer Hersteller des RAMs
 * @param capacityGb Kapazität des RAMs in GB
 * @param speedMhz Geschwindigkeit des RAMs in MHz
 * @param price Preis des RAMs
 * @author Fabio
 */
public class Ram extends Component {

    private final int capacityGb;
    private final int speedMhz;

    public Ram(int id, Type type, String name, double price, Manufacturer manufacturer, int capacityGb, int speedMhz) {
        super(id, type, name, price, manufacturer);
        this.capacityGb = capacityGb;
        this.speedMhz = speedMhz;
    }

    public int getCapacityGb() {
        return capacityGb;
    }

    public int getSpeedMhz() {
        return speedMhz;
    }
}