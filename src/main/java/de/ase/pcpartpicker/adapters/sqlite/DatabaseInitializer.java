package de.ase.pcpartpicker.adapters.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasse zur Initialisierung der SQLite-Datenbank.
 * Erstellt die notwendigen Tabellen, falls sie nicht bereits existieren.
 * @see DatabaseInitializer#initialize()
 * @author Fabio
 */
public class DatabaseInitializer {

    private final ConnectionFactory connectionFactory;

    public DatabaseInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Initialisiert die Datenbank, indem sie die erforderlichen Tabellen erstellt, falls diese nicht bereits existieren.
     * @throws IllegalStateException, wenn die Initialisierung fehlschlägt.
     */
    public void initialize() {
        //4 String Variablen für die SQL-Befehle zur Erstellung der Tabellen für CPU, GPU, Mainboard und RAM.
        String cpuString = """
            CREATE TABLE IF NOT EXISTS cpu (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                socket TEXT NOT NULL,
                speed_ghz INTEGER NOT NULL,
                price REAL NOT NULL
            )
            """;
        String gpuString = """
            CREATE TABLE IF NOT EXISTS gpu (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                vram_gb INTEGER NOT NULL,
                price REAL NOT NULL
            )
            """;
        String mainboardString = """
            CREATE TABLE IF NOT EXISTS mainboard (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                socket TEXT NOT NULL,
                form_factor TEXT NOT NULL,
                price REAL NOT NULL
            )
            """;
        String ramString = """
            CREATE TABLE IF NOT EXISTS ram (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                capacity_gb INTEGER NOT NULL,
                speed_mhz INTEGER NOT NULL,
                price REAL NOT NULL
            )
            """;
        try (Connection connection = connectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
                
            statement.executeUpdate(cpuString);
            statement.executeUpdate(gpuString);
            statement.executeUpdate(mainboardString);
            statement.executeUpdate(ramString);

            statement.executeUpdate("""
                INSERT INTO cpu (id, name, socket, speed_ghz, price)
                SELECT 1, 'Intel Tuluhan i9-13900K', 'LGA1700', 3, 589.00
                WHERE NOT EXISTS (SELECT 1 FROM cpu WHERE id = 1)
                """);

            statement.executeUpdate("""
                INSERT INTO cpu (id, name, socket, speed_ghz, price)
                SELECT 2, 'AMD Schröder 7 7800X3D', 'AM5', 4, 399.00
                WHERE NOT EXISTS (SELECT 1 FROM cpu WHERE id = 2)
                """);

            statement.executeUpdate("""
                INSERT INTO gpu (id, name, vram_gb, price)
                SELECT 1, 'NVIDIA Benjamin RTX 4070', 12, 599.00
                WHERE NOT EXISTS (SELECT 1 FROM gpu WHERE id = 1)
                """);

            statement.executeUpdate("""
                INSERT INTO gpu (id, name, vram_gb, price)
                SELECT 2, 'AMD Nuts RX 7800 XT', 16, 539.00
                WHERE NOT EXISTS (SELECT 1 FROM gpu WHERE id = 2)
                """);

            statement.executeUpdate("""
                INSERT INTO ram (id, name, capacity_gb, speed_mhz, price)
                SELECT 1, 'Corsair Vegeratisches DDR5', 32, 6000, 13900.00
                WHERE NOT EXISTS (SELECT 1 FROM ram WHERE id = 1)
                """);

            statement.executeUpdate("""
                INSERT INTO ram (id, name, capacity_gb, speed_mhz, price)
                SELECT 2, 'G.Skill.Issue MachenSeMal DDR4', 16, 3600, 67000.67
                WHERE NOT EXISTS (SELECT 1 FROM ram WHERE id = 2)
                """);

            statement.executeUpdate("""
                INSERT INTO mainboard (id, name, socket, form_factor, price)
                SELECT 1, 'MSI MAG B650 CurlyFries', 'AM5', 'ATX', 219.00
                WHERE NOT EXISTS (SELECT 1 FROM mainboard WHERE id = 1)
                """);

            statement.executeUpdate("""
                INSERT INTO mainboard (id, name, socket, form_factor, price)
                SELECT 2, 'Lausen TUF B760M-PLUS', 'LGA1700', 'Micro-ATX', 169.00
                WHERE NOT EXISTS (SELECT 1 FROM mainboard WHERE id = 2)
                """);
       } catch (SQLException e) {
            throw new IllegalStateException("SQLite-Initialisierung fehlgeschlagen.", e);
        }
    }
}