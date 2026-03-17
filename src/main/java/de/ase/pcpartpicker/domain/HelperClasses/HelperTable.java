package de.ase.pcpartpicker.domain.HelperClasses;

import java.util.Objects;

/**
 * Abstrakte Klasse für Hilfstabellen wie Hersteller, Sockeltypen, etc.
 * Diese Klassen haben nur eine ID und einen Namen.
 * @param id Eindeutige ID des Eintrags
 * @param name Name des Eintrags
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        HelperTable that = (HelperTable) other;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id, name);
    }
    
}
