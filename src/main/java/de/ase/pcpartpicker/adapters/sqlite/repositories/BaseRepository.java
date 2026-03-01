package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.util.List;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Component;

public abstract class BaseRepository<T extends Component> implements ComponentRepository<T> {
    /**
     * Abstrakte Basisklasse für Repositorys, welche das interface ComponentRepository implementiert.
     * Die Repository-Klassen sollen für jede Komponente die Datensätze ausgeben.
     * @param connectionFactory: Eine Factory-Klasse, die Verbindungen zur SQLite-Datenbank erstellt.
     * @author Fabio
    */
    protected final ConnectionFactory connectionFactory;

    protected BaseRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    public abstract List<T> findAll();
}