package de.ase.pcpartpicker.domain;

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

    protected Component(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}