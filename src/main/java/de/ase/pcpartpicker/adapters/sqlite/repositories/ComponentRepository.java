package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.domain.Component;
/**
 * Interface für die Repositorys der Komponenten.
 * @param <T> Der Typ der Komponente, welche von Component erbt.
 * @author Fabio
 */
public interface ComponentRepository<T extends Component> {
    
    
    
    
    /**
     * Wiedergibt alle Datensätze aus der Datenbank zurück mittels SQL-Abfrage.
     * @return <T> Liste aus Objekten des Typs T (Component-Subtypen)
     * @throws IllegalStateException, wenn die Daten nicht geladen werden können.
    */
    List<T> findAll();
}