package de.ase.pcpartpicker.domain;

/**
 * Klasse für die RAM-Komponente.
 * @param id Eindeutige ID des RAMs
 * @param name Name des RAMs
 * @param capacityGb Kapazität des RAMs in GB
 * @param speedMhz Geschwindigkeit des RAMs in MHz
 * @param price Preis des RAMs
 * @author Fabio
 */
public class Ram extends Component {

    private final int capacityGb;
    private final int speedMhz;

    public Ram(int id, String name, int capacityGb, int speedMhz, double price) {
        super(id, name, price);
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