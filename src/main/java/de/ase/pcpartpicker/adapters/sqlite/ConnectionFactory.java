package de.ase.pcpartpicker.adapters.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory-Klasse zur Erstellung von Datenbankverbindungen.
 * Stellt Methode createConnection() bereit, um Verbindungen zur SQLite-Datenbank herzustellen.
 * @see ConnectionFactory#createConnection()
 * @author Fabio
 */
public class ConnectionFactory {

    /**
     * Erstellt neue Verbindung
     * @return Connection-Objekt, das die Verbindung zur Datenbank repräsentiert.
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.jdbcUrl());
    }
}