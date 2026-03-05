package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Abstrakte Basisklasse für Storage-Komponenten.
 * @param id Eindeutige ID des Storages
 * @param name Name des Storages
 * @param manufacturer Hersteller des Storages
 * @param capacityGB Kapazität in GB
 * @param price Preis des Storages
 * @author Fabio
 */
public abstract class Storage extends Component {

    private final int capacityGB;

    protected Storage(int id, String name, double price, Manufacturer manufacturer, int capacityGB) {
        super(id, name, price, manufacturer);
        this.capacityGB = capacityGB;
    }

    public int getCapacityGB() {
        return capacityGB;
    }
}
