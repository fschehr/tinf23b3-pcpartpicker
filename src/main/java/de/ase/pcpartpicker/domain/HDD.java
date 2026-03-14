package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Klasse für die HDD-Komponente.
 * @param id Eindeutige ID der HDD
 * @param name Name der HDD
 * @param price Preis der HDD
 * @param manufacturer Hersteller der HDD
 * @param capacityGB Kapazität der HDD in GB
 * @author Fabio
 */
public class HDD extends Storage {

    public HDD(int id, String name, double price, Manufacturer manufacturer, int capacityGB) {
        super(id, name, price, manufacturer, capacityGB);
    }
}
