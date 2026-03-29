package de.ase.pcpartpicker.adapters.sqlite.repositories;

import de.ase.pcpartpicker.domain.Component;

/**
 * Spezialisierung des allgemeinen {@link Repository} für Komponenten.
 *
 * @param <T> Der Typ der Komponente.
 */
public interface ComponentRepository<T extends Component> extends Repository<T> {
}