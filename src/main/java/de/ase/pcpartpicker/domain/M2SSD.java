package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die M.2-SSD-Komponente.
 * @param id Eindeutige ID der M.2 SSD
 * @param name Name der M.2 SSD
 * @param manufacturer Hersteller der M.2 SSD
 * @param capacityGB Kapazität der M.2 SSD in GB
 * @param price Preis der M.2 SSD
 * @author Fabio
 */
public class M2SSD extends Storage {

    public M2SSD(int id, String name, double price, Manufacturer manufacturer, int capacityGB) {
        super(id, name, price, manufacturer, capacityGB);
    }
}
