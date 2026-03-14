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
        try (Connection connection = connectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");

            statement.executeUpdate("DROP TABLE IF EXISTS computer");
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            statement.executeUpdate("DROP TABLE IF EXISTS cpu");
            statement.executeUpdate("DROP TABLE IF EXISTS gpu");
            statement.executeUpdate("DROP TABLE IF EXISTS ram");
            statement.executeUpdate("DROP TABLE IF EXISTS hdd");
            statement.executeUpdate("DROP TABLE IF EXISTS ssd");
            statement.executeUpdate("DROP TABLE IF EXISTS m2_ssd");
            statement.executeUpdate("DROP TABLE IF EXISTS mainboard");
            statement.executeUpdate("DROP TABLE IF EXISTS psu");
            statement.executeUpdate("DROP TABLE IF EXISTS pc_case");
            statement.executeUpdate("DROP TABLE IF EXISTS sockets");
            statement.executeUpdate("DROP TABLE IF EXISTS motherboard_form_factor");
            statement.executeUpdate("DROP TABLE IF EXISTS psu_form_factor");
            statement.executeUpdate("DROP TABLE IF EXISTS manufacturer");
            statement.executeUpdate("DROP TABLE IF EXISTS type");

            statement.executeUpdate("""
                CREATE TABLE type (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE manufacturer (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE sockets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE motherboard_form_factor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE psu_form_factor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE cpu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    socket_id INTEGER NOT NULL,
                    speed_ghz REAL NOT NULL,
                    hasIntegratedGraphics BOOLEAN NOT NULL DEFAULT 0,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id),
                    FOREIGN KEY (socket_id) REFERENCES sockets(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE gpu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    vram_gb INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE ram (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    capacity_gb INTEGER NOT NULL,
                    speed_mhz INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE hdd (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    capacity_gb INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE ssd (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    capacity_gb INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE m2_ssd (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    capacity_gb INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE mainboard (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    socket_id INTEGER NOT NULL,
                    form_factor_id INTEGER NOT NULL,
                    ram_slots INTEGER NOT NULL,
                    pcie_slots INTEGER NOT NULL,
                    sata_slots INTEGER NOT NULL,
                    m2_slots INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id),
                    FOREIGN KEY (socket_id) REFERENCES sockets(id),
                    FOREIGN KEY (form_factor_id) REFERENCES motherboard_form_factor(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE psu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    wattage INTEGER NOT NULL,
                    form_factor_id INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id),
                    FOREIGN KEY (form_factor_id) REFERENCES psu_form_factor(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE pc_case (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    motherboard_form_factor_id INTEGER NOT NULL,
                    psu_form_factor_id INTEGER NOT NULL,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id),
                    FOREIGN KEY (motherboard_form_factor_id) REFERENCES motherboard_form_factor(id),
                    FOREIGN KEY (psu_form_factor_id) REFERENCES psu_form_factor(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE computer (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    cpu_id INTEGER NOT NULL,
                    gpu_id INTEGER,
                    mainboard_id INTEGER NOT NULL,
                    ram_id INTEGER NOT NULL,
                    psu_id INTEGER NOT NULL,
                    case_id INTEGER NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (cpu_id) REFERENCES cpu(id),
                    FOREIGN KEY (gpu_id) REFERENCES gpu(id),
                    FOREIGN KEY (mainboard_id) REFERENCES mainboard(id),
                    FOREIGN KEY (ram_id) REFERENCES ram(id),
                    FOREIGN KEY (psu_id) REFERENCES psu(id),
                    FOREIGN KEY (case_id) REFERENCES pc_case(id)
                )
                """);

            statement.executeUpdate("INSERT INTO type (name) VALUES ('CPU'), ('GPU'), ('RAM'), ('Mainboard'), ('PSU'), ('Case'), ('HDD'), ('SSD'), ('M2SSD')");
            statement.executeUpdate("INSERT INTO manufacturer (name) VALUES ('Intel'), ('AMD'), ('NVIDIA'), ('Corsair'), ('G.Skill'), ('MSI'), ('ASUS'), ('be quiet!'), ('Fractal Design'), ('Samsung'), ('Western Digital'), ('Seagate')");
            statement.executeUpdate("INSERT INTO sockets (name) VALUES ('LGA1700'), ('AM5')");
            statement.executeUpdate("INSERT INTO motherboard_form_factor (name) VALUES ('ATX'), ('Micro-ATX')");
            statement.executeUpdate("INSERT INTO psu_form_factor (name) VALUES ('ATX')");

            statement.executeUpdate("""
                INSERT INTO cpu (manufacturer_id, type_id, name, price, socket_id, speed_ghz)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Intel'),
                    (SELECT id FROM type WHERE name = 'CPU'),
                    'Intel Tuluhan i9-13900K',
                    589.00,
                    (SELECT id FROM sockets WHERE name = 'LGA1700'),
                    3.0
                ),
                (
                    (SELECT id FROM manufacturer WHERE name = 'AMD'),
                    (SELECT id FROM type WHERE name = 'CPU'),
                    'AMD Schröder 7 7800X3D',
                    399.00,
                    (SELECT id FROM sockets WHERE name = 'AM5'),
                    4.0
                )
                """);

            statement.executeUpdate("""
                INSERT INTO gpu (manufacturer_id, type_id, name, price, vram_gb)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'NVIDIA'),
                    (SELECT id FROM type WHERE name = 'GPU'),
                    'NVIDIA Benjamin RTX 4070',
                    599.00,
                    12
                ),
                (
                    (SELECT id FROM manufacturer WHERE name = 'AMD'),
                    (SELECT id FROM type WHERE name = 'GPU'),
                    'AMD Radeon RX 7800 XT',
                    539.00,
                    16
                )
                """);

            statement.executeUpdate("""
                INSERT INTO ram (manufacturer_id, type_id, name, price, capacity_gb, speed_mhz)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Corsair'),
                    (SELECT id FROM type WHERE name = 'RAM'),
                    'Corsair Vegetarisches DDR5',
                    139.00,
                    32,
                    6000
                ),
                (
                    (SELECT id FROM manufacturer WHERE name = 'G.Skill'),
                    (SELECT id FROM type WHERE name = 'RAM'),
                    'G.Skill Ripjaws DDR4',
                    69.00,
                    16,
                    3600
                )
                """);

            statement.executeUpdate("""
                INSERT INTO hdd (manufacturer_id, type_id, name, price, capacity_gb)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Seagate'),
                    (SELECT id FROM type WHERE name = 'HDD'),
                    'Seagate Barracuda 2TB',
                    59.00,
                    2000
                )
                """);

            statement.executeUpdate("""
                INSERT INTO ssd (manufacturer_id, type_id, name, price, capacity_gb)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Western Digital'),
                    (SELECT id FROM type WHERE name = 'SSD'),
                    'WD Blue SATA SSD 1TB',
                    79.00,
                    1000
                )
                """);

            statement.executeUpdate("""
                INSERT INTO m2_ssd (manufacturer_id, type_id, name, price, capacity_gb)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Samsung'),
                    (SELECT id FROM type WHERE name = 'M2SSD'),
                    'Samsung 990 EVO 1TB',
                    99.00,
                    1000
                )
                """);

            statement.executeUpdate("""
                INSERT INTO mainboard (manufacturer_id, type_id, name, price, socket_id, form_factor_id, ram_slots, pcie_slots, sata_slots, m2_slots)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'MSI'),
                    (SELECT id FROM type WHERE name = 'Mainboard'),
                    'MSI LAUSEN B650 Tomahawk',
                    219.00,
                    (SELECT id FROM sockets WHERE name = 'AM5'),
                    (SELECT id FROM motherboard_form_factor WHERE name = 'ATX'),
                    4,
                    3,
                    6,
                    2
                ),
                (
                    (SELECT id FROM manufacturer WHERE name = 'ASUS'),
                    (SELECT id FROM type WHERE name = 'Mainboard'),
                    'ASUS TUF B760M-PLUS',
                    169.00,
                    (SELECT id FROM sockets WHERE name = 'LGA1700'),
                    (SELECT id FROM motherboard_form_factor WHERE name = 'Micro-ATX'),
                    4,
                    2,
                    4,
                    2
                )
                """);

            statement.executeUpdate("""
                INSERT INTO psu (manufacturer_id, type_id, name, price, wattage, form_factor_id)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'be quiet!'),
                    (SELECT id FROM type WHERE name = 'PSU'),
                    'be quiet! Pure Nuts 12M',
                    139.00,
                    750,
                    (SELECT id FROM psu_form_factor WHERE name = 'ATX')
                )
                """);

            statement.executeUpdate("""
                INSERT INTO pc_case (manufacturer_id, type_id, name, price, motherboard_form_factor_id, psu_form_factor_id)
                VALUES (
                    (SELECT id FROM manufacturer WHERE name = 'Fractal Design'),
                    (SELECT id FROM type WHERE name = 'Case'),
                    'Fractal Design ElektrotechnikHolzhand',
                    149.00,
                    (SELECT id FROM motherboard_form_factor WHERE name = 'ATX'),
                    (SELECT id FROM psu_form_factor WHERE name = 'ATX')
                )
                """);

            statement.executeUpdate("""
                INSERT INTO users (name)
                VALUES ('Test User')
                """);

            statement.executeUpdate("""
                INSERT INTO computer (user_id, cpu_id, gpu_id, mainboard_id, ram_id, psu_id, case_id)
                VALUES (
                    (SELECT id FROM users WHERE name = 'Test User'),
                    (SELECT id FROM cpu ORDER BY id LIMIT 1),
                    (SELECT id FROM gpu ORDER BY id LIMIT 1),
                    (SELECT id FROM mainboard ORDER BY id LIMIT 1),
                    (SELECT id FROM ram ORDER BY id LIMIT 1),
                    (SELECT id FROM psu ORDER BY id LIMIT 1),
                    (SELECT id FROM pc_case ORDER BY id LIMIT 1)
                )
                """);
       } catch (SQLException e) {
            throw new IllegalStateException("SQLite-Initialisierung fehlgeschlagen.", e);
        }
    }
}