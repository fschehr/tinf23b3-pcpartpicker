package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Abstrakte Basisklasse für alle Komponenten.
 * @param id Eindeutige ID der Komponente
 * @param name Name der Komponente
 * @param price Preis der Komponente
 * @author Fabio
 */
public abstract class Component {

    private final int id;
    private final String name;
    private final double price;
    private final Manufacturer manufacturer;

    protected Component(int id, String name, double price, Manufacturer manufacturer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String manufacturerName = manufacturer != null ? manufacturer.getName() : "-";
        return String.format("%s (ID: %d, Typ: %s, Preis: %.2f, Hersteller: %s)", name, id, this.getClass().getSimpleName(), price, manufacturerName);
    }
}