package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

/**
 * Allgemeines Repository-Interface für lesenden Zugriff.
 *
 * @param <T> Typ des Domain-Objekts.
 */
public interface Repository<T> {

    /**
     * Lädt alle Datensätze eines Typs.
     *
     * @return Liste aller Datensätze.
     */
    List<T> findAll();
}