package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die SSD-Komponente.
 * @param id Eindeutige ID der SSD
 * @param name Name der SSD
 * @param manufacturer Hersteller der SSD
 * @param capacityGB Kapazität der SSD in GB
 * @param price Preis der SSD
 * @author Fabio
 */
public class SSD extends Storage {

    public SSD(int id, String name, double price, Manufacturer manufacturer, int capacityGB) {
        super(id, name, price, manufacturer, capacityGB);
    }
}
