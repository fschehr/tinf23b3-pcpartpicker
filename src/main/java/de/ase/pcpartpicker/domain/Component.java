package de.ase.pcpartpicker.domain;

import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;

/**
 * Abstrakte Basisklasse für alle Komponenten.
 * @param id Eindeutige ID der Komponente
 * @param name Name der Komponente
 * @param price Preis der Komponente
 * @param manufacturer Hersteller der Komponente
 * @param powerConsumptionW Maximaler Stromverbrauch der Komponente in Watt
 * @author Fabio
 */
public abstract class Component {

    private final int id;
    private final String name;
    private final double price;
    private final Manufacturer manufacturer;
    private final int powerConsumptionW; // Maximaler Stromverbrauch in Watt

    protected Component(int id, String name, double price, Manufacturer manufacturer, int powerConsumptionW) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
        this.powerConsumptionW = powerConsumptionW;
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

    public int getPowerConsumptionW() {
        return powerConsumptionW;
    }

    @Override
    public String toString() {
        String manufacturerName = manufacturer != null ? manufacturer.getName() : "-";
        return String.format("%s (ID: %d, Typ: %s, Preis: %.2f, Hersteller: %s, Stromverbrauch: %d W)", name, id, this.getClass().getSimpleName(), price, manufacturerName, powerConsumptionW);
    }
}