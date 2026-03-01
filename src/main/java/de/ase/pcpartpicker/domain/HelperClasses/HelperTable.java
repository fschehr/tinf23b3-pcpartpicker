package de.ase.pcpartpicker.domain.HelperClasses;

/**
 * Abstrakte Klasse für Hilfstabellen wie Hersteller, Sockeltypen, etc.
 * Diese Klassen haben nur eine ID und einen Namen.
 * @author Fabio
 */
public abstract class HelperTable {
    private final int id;
    private final String name;
    /**
     * Konstruktor für die HelperTable.
     * @param id
     * @param name
     */
    public HelperTable(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
}
