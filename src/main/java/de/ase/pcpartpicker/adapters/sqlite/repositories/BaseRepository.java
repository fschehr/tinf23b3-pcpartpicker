package de.ase.pcpartpicker.adapters.sqlite.repositories;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.Component;

public abstract class BaseRepository<T extends Component> extends JdbcRepository<T> implements ComponentRepository<T> {
    /**
     * Abstrakte Basisklasse für Repositorys, welche das interface ComponentRepository implementiert.
     * Die Repository-Klassen sollen für jede Komponente die Datensätze ausgeben.
     * @param connectionFactory: Eine Factory-Klasse, die Verbindungen zur SQLite-Datenbank erstellt.
     * @author Fabio
    */
    protected BaseRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }
}