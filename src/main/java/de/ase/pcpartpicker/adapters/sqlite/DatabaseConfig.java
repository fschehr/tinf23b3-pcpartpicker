package de.ase.pcpartpicker.adapters.sqlite;

/**
 * Klasse zur Konfiguration der Datenbankverbindung.
 * Stellt die JDBC-URL für die SQLite-Datenbank bereit.
 * @author Fabio
 */
public final class DatabaseConfig {

    private static final String JDBC_URL = "jdbc:sqlite:pcpartpicker.db";

    private DatabaseConfig() {
    }

    public static String jdbcUrl() {
        return JDBC_URL;
    }
}