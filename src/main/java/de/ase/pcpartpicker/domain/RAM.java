package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die RAM-Komponente.
 * @param id Eindeutige ID des RAMs
 * @param name Name des RAMs
 * @param price Preis des RAMs
 * @param manufacturer Hersteller des RAMs
 * @param capacityGB Kapazität des RAMs in GB
 * @param speedMHz Geschwindigkeit des RAMs in MHz
 * @author Fabio
 */
public class RAM extends Component {

    private final int capacityGB;
    private final int speedMHz;

    public RAM(int id, String name, double price, Manufacturer manufacturer, int capacityGB, int speedMHz) {
        super(id, name, price, manufacturer, 15); // Ein RAM-Modul hat einen angenommenen Stromverbrauch von 15 Watt
        this.capacityGB = capacityGB;
        this.speedMHz = speedMHz;
    }

    public int getCapacityGB() {
        return capacityGB;
    }

    public int getSpeedMHz() {
        return speedMHz;
    }
}