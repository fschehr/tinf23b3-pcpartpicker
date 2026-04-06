package de.ase.pcpartpicker.adapters.sqlite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Klasse zur Initialisierung der SQLite-Datenbank.
 * Erstellt die notwendigen Tabellen, falls sie nicht bereits existieren.
 * @see DatabaseInitializer#initialize()
 * @author Fabio
 */
public class DatabaseInitializer {

    private final ConnectionFactory connectionFactory;
    private final Path dataDirectory;

    public DatabaseInitializer(ConnectionFactory connectionFactory) {
        this(connectionFactory, null);
    }

    public DatabaseInitializer(ConnectionFactory connectionFactory, Path dataDirectory) {
        this.connectionFactory = connectionFactory;
        this.dataDirectory = dataDirectory;
    }

    /**
     * Initialisiert die Datenbank, indem sie die erforderlichen Tabellen erstellt, falls diese nicht bereits existieren.
     * @throws IllegalStateException, wenn die Initialisierung fehlschlägt.
     */
    public void initialize() {
        try (Connection connection = connectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");

            createSchema(statement);

            if (areAllComponentTablesEmpty(connection)) {
                seedFromJsonl(connection);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("SQLite-Initialisierung fehlgeschlagen.", e);
        }
    }

    /**
     * Setzt die Datenbank vollständig zurück (Drop + Neuaufbau + Reimport aus JSONL).
     * @throws IllegalStateException, wenn der Reset fehlschlägt.
     */
    public void resetDatabase() {
        SQLException lastException = null;

        for (int attempt = 1; attempt <= 10; attempt++) {
            try (Connection connection = connectionFactory.createConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
                System.out.println("DB-Reset läuft. (Versuch " + attempt + ") ...");
                System.out.println("Lösche Existierende Tabelle ...");
                dropSchema(statement);
                System.out.println("Datenbankschema gelöscht. Erstelle neues Schema ...");
                createSchema(statement);
                System.out.println("Neues Datenbankschema erstellt. Importiere Daten ...");
                seedFromJsonl(connection);
                return;
            } catch (SQLException e) {
                lastException = e;
                if (!isDatabaseLocked(e) || attempt == 10) {
                    throw new IllegalStateException("SQLite-Reset fehlgeschlagen.", e);
                }

                try {
                    Thread.sleep(200L);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("SQLite-Reset unterbrochen.", interruptedException);
                }
            }
        }

        throw new IllegalStateException("SQLite-Reset fehlgeschlagen.", lastException);
    }

    private boolean isDatabaseLocked(SQLException exception) {
        String message = exception.getMessage();
        if (message == null) {
            return false;
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        return normalized.contains("database is locked") || normalized.contains("sqlite_busy");
    }

    private void dropSchema(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS computer_storage");
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
    }

    private void createSchema(Statement statement) throws SQLException {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS type (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS manufacturer (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS sockets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS motherboard_form_factor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS psu_form_factor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cpu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    socket_id INTEGER NOT NULL,
                    speed_ghz REAL NOT NULL,
                    boost_clock REAL,
                    hasIntegratedGraphics BOOLEAN NOT NULL DEFAULT 0,
                    power_consumption_w INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id),
                    FOREIGN KEY (socket_id) REFERENCES sockets(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS gpu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    manufacturer_id INTEGER NOT NULL,
                    type_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    core_clock REAL NOT NULL DEFAULT 0,
                    boost_clock REAL,
                    vram_gb INTEGER NOT NULL,
                    power_consumption_w INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id),
                    FOREIGN KEY (type_id) REFERENCES type(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ram (
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
                CREATE TABLE IF NOT EXISTS hdd (
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
                CREATE TABLE IF NOT EXISTS ssd (
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
                CREATE TABLE IF NOT EXISTS m2_ssd (
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
                CREATE TABLE IF NOT EXISTS mainboard (
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
                CREATE TABLE IF NOT EXISTS psu (
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
                CREATE TABLE IF NOT EXISTS pc_case (
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
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS computer (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    is_draft INTEGER NOT NULL DEFAULT 0,
                    cpu_id INTEGER,
                    gpu_id INTEGER,
                    mainboard_id INTEGER,
                    ram_id INTEGER,
                    ram_module_count INTEGER NOT NULL DEFAULT 0,
                    psu_id INTEGER,
                    case_id INTEGER,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (cpu_id) REFERENCES cpu(id),
                    FOREIGN KEY (gpu_id) REFERENCES gpu(id),
                    FOREIGN KEY (mainboard_id) REFERENCES mainboard(id),
                    FOREIGN KEY (ram_id) REFERENCES ram(id),
                    FOREIGN KEY (psu_id) REFERENCES psu(id),
                    FOREIGN KEY (case_id) REFERENCES pc_case(id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS computer_storage (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    computer_id INTEGER NOT NULL,
                    storage_type TEXT NOT NULL,
                    storage_id INTEGER NOT NULL,
                    FOREIGN KEY (computer_id) REFERENCES computer(id) ON DELETE CASCADE
                )
                """);

            statement.executeUpdate("""
                INSERT INTO users (name)
                SELECT 'test user'
                WHERE NOT EXISTS (
                    SELECT 1 FROM users WHERE name = 'test user'
                )
            """);
    }

    private boolean areAllComponentTablesEmpty(Connection connection) throws SQLException {
        return isTableEmpty(connection, "cpu")
            && isTableEmpty(connection, "gpu")
            && isTableEmpty(connection, "ram")
            && isTableEmpty(connection, "hdd")
            && isTableEmpty(connection, "ssd")
            && isTableEmpty(connection, "m2_ssd")
            && isTableEmpty(connection, "mainboard")
            && isTableEmpty(connection, "psu")
            && isTableEmpty(connection, "pc_case");
    }

    private boolean isTableEmpty(Connection connection, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() && resultSet.getInt(1) == 0;
        }
    }

    private void seedFromJsonl(Connection connection) {
        try {
            int cpuTypeId = ensureNamedId(connection, "type", "CPU");
            int gpuTypeId = ensureNamedId(connection, "type", "GPU");
            int ramTypeId = ensureNamedId(connection, "type", "RAM");
            int mainboardTypeId = ensureNamedId(connection, "type", "Mainboard");
            int psuTypeId = ensureNamedId(connection, "type", "PSU");
            int caseTypeId = ensureNamedId(connection, "type", "Case");
            int hddTypeId = ensureNamedId(connection, "type", "HDD");
            int ssdTypeId = ensureNamedId(connection, "type", "SSD");
            int m2SsdTypeId = ensureNamedId(connection, "type", "M2SSD");

            System.out.println("Erstelle CPUs ...");
            importCpu(connection, cpuTypeId);
            System.out.println("Erstelle GPUs ...");
            importGpu(connection, gpuTypeId);
            System.out.println("Erstelle RAM ...");
            importRam(connection, ramTypeId);
            System.out.println("Erstelle Mainboards ...");
            importMainboard(connection, mainboardTypeId);
            System.out.println("Erstelle PSUs ...");
            importPsu(connection, psuTypeId);
            System.out.println("Erstelle Cases ...");
            importCase(connection, caseTypeId);
            System.out.println("Erstelle Speicher ...");
            importStorage(connection, hddTypeId, ssdTypeId, m2SsdTypeId);
            System.out.println("Import abgeschlossen.");
        } catch (SQLException e) {
            throw new IllegalStateException("Seed aus JSONL fehlgeschlagen.", e);
        }
    }

    private void importCpu(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO cpu (manufacturer_id, type_id, name, price, socket_id, speed_ghz, boost_clock, hasIntegratedGraphics, power_consumption_w)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "cpu.jsonl", sql, (row, ps) -> {
            String name = getString(row, "name");
            Double price = getDouble(row, "price");
            Double speed = getDouble(row, "core_clock");
            Double boost = getDouble(row, "boost_clock");
            Integer tdp = getInt(row, "tdp");
            if (anyNull(name, price, speed, tdp)) {
                return false;
            }

            String manufacturer = extractManufacturer(name);
            String socket = inferCpuSocket(name, getString(row, "microarchitecture"));
            boolean hasIgpu = !isJsonNullOrMissing(row, "graphics");

            int manufacturerId = ensureNamedId(connection, "manufacturer", manufacturer);
            int socketId = ensureNamedId(connection, "sockets", socket);

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, name);
            ps.setDouble(4, price);
            ps.setInt(5, socketId);
            ps.setDouble(6, speed);
            if (boost == null) {
                ps.setNull(7, Types.REAL);
            } else {
                ps.setDouble(7, boost);
            }
            ps.setBoolean(8, hasIgpu);
            ps.setInt(9, tdp);
            return true;
        });
    }

    private void importGpu(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO gpu (manufacturer_id, type_id, name, price, core_clock, boost_clock, vram_gb, power_consumption_w)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "gpu.jsonl", sql, (row, ps) -> {
            String name = getString(row, "chipset");
            String modelName = getString(row, "name");
            Double price = getDouble(row, "price");
            Double coreClock = getDouble(row, "core_clock");
            Double boostClock = getDouble(row, "boost_clock");
            Integer vram = getInt(row, "memory");
            Integer tdp = getInt(row, "tdp");
            if (anyNull(name, price, coreClock, vram, tdp)) {
                return false;
            }

            String manufacturerSource = modelName != null && !modelName.isBlank() ? modelName : name;
            int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(manufacturerSource));

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, name);
            ps.setDouble(4, price);
            ps.setDouble(5, coreClock);
            if (boostClock == null) {
                ps.setNull(6, Types.REAL);
            } else {
                ps.setDouble(6, boostClock);
            }
            ps.setInt(7, vram);
            ps.setInt(8, tdp);
            return true;
        });
    }

    private void importRam(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO ram (manufacturer_id, type_id, name, price, capacity_gb, speed_mhz)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "ram.jsonl", sql, (row, ps) -> {
            String name = getString(row, "name");
            Double price = getDouble(row, "price");
            Integer capacity = readModulesCapacity(row);
            Integer speed = readSpeedMhz(row);
            if (anyNull(name, price, capacity, speed)) {
                return false;
            }

            int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(name));
            String cleanName = stripManufacturerPrefix(name);

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, cleanName);
            ps.setDouble(4, price);
            ps.setInt(5, capacity);
            ps.setInt(6, speed);
            return true;
        });
    }

    private void importMainboard(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO mainboard (manufacturer_id, type_id, name, price, socket_id, form_factor_id, ram_slots, pcie_slots, sata_slots, m2_slots)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "mobo.jsonl", sql, (row, ps) -> {
            String name = getString(row, "name");
            Double price = getDouble(row, "price");
            String socket = getString(row, "socket");
            String formFactor = normalizeMotherboardFormFactor(getString(row, "form_factor"));
            Integer ramSlots = getInt(row, "memory_slots");
            if (anyNull(name, price, socket, formFactor, ramSlots)) {
                return false;
            }

            int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(name));
            int socketId = ensureNamedId(connection, "sockets", socket);
            int formFactorId = ensureNamedId(connection, "motherboard_form_factor", formFactor);

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, name);
            ps.setDouble(4, price);
            ps.setInt(5, socketId);
            ps.setInt(6, formFactorId);
            ps.setInt(7, ramSlots);
            ps.setInt(8, 1);
            ps.setInt(9, 1);
            ps.setInt(10, 1);
            return true;
        });
    }

    private void importPsu(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO psu (manufacturer_id, type_id, name, price, wattage, form_factor_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "psu.jsonl", sql, (row, ps) -> {
            String name = getString(row, "name");
            Double price = getDouble(row, "price");
            Integer wattage = getInt(row, "wattage");
            String formFactor = normalizePsuFormFactor(getString(row, "type"));
            if (anyNull(name, price, wattage, formFactor)) {
                return false;
            }

            int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(name));
            int formFactorId = ensureNamedId(connection, "psu_form_factor", formFactor);

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, name);
            ps.setDouble(4, price);
            ps.setInt(5, wattage);
            ps.setInt(6, formFactorId);
            return true;
        });
    }

    private void importCase(Connection connection, int typeId) throws SQLException {
        String sql = """
            INSERT INTO pc_case (manufacturer_id, type_id, name, price, motherboard_form_factor_id, psu_form_factor_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        importRows(connection, "case.jsonl", sql, (row, ps) -> {
            String name = getString(row, "name");
            Double price = getDouble(row, "price");
            String caseType = getString(row, "type");
            if (anyNull(name, price, caseType)) {
                return false;
            }

            String mbFormFactor = inferCaseMotherboardFormFactor(caseType);
            String psuFormFactor = normalizePsuFormFactor(getString(row, "psu"));
            if (psuFormFactor == null) {
                psuFormFactor = "ATX";
            }

            int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(name));
            int mbFormFactorId = ensureNamedId(connection, "motherboard_form_factor", mbFormFactor);
            int psuFormFactorId = ensureNamedId(connection, "psu_form_factor", psuFormFactor);

            ps.setInt(1, manufacturerId);
            ps.setInt(2, typeId);
            ps.setString(3, name);
            ps.setDouble(4, price);
            ps.setInt(5, mbFormFactorId);
            ps.setInt(6, psuFormFactorId);
            return true;
        });
    }

    private void importStorage(Connection connection, int hddTypeId, int ssdTypeId, int m2SsdTypeId) throws SQLException {
        String hddSql = """
            INSERT INTO hdd (manufacturer_id, type_id, name, price, capacity_gb)
            VALUES (?, ?, ?, ?, ?)
            """;
        String ssdSql = """
            INSERT INTO ssd (manufacturer_id, type_id, name, price, capacity_gb)
            VALUES (?, ?, ?, ?, ?)
            """;
        String m2Sql = """
            INSERT INTO m2_ssd (manufacturer_id, type_id, name, price, capacity_gb)
            VALUES (?, ?, ?, ?, ?)
            """;

        List<JsonObject> rows = readJsonl("memory.jsonl");
        int totalRows = rows.size();
        int importedRows = 0;
        int processedRows = 0;
        try (PreparedStatement hddPs = connection.prepareStatement(hddSql);
             PreparedStatement ssdPs = connection.prepareStatement(ssdSql);
             PreparedStatement m2Ps = connection.prepareStatement(m2Sql)) {
            for (JsonObject row : rows) {
                processedRows++;
                String name = getString(row, "name");
                Double price = getDouble(row, "price");
                Integer capacity = getInt(row, "capacity");
                if (name == null || price == null || capacity == null) {
                    printImportProgress("memory.jsonl", processedRows, totalRows, importedRows);
                    continue;
                }

                int manufacturerId = ensureNamedId(connection, "manufacturer", extractManufacturer(name));
                StorageTarget target = classifyStorage(row);

                PreparedStatement ps;
                int typeId;
                if (target == StorageTarget.HDD) {
                    ps = hddPs;
                    typeId = hddTypeId;
                } else if (target == StorageTarget.M2SSD) {
                    ps = m2Ps;
                    typeId = m2SsdTypeId;
                } else {
                    ps = ssdPs;
                    typeId = ssdTypeId;
                }

                ps.setInt(1, manufacturerId);
                ps.setInt(2, typeId);
                ps.setString(3, name);
                ps.setDouble(4, price);
                ps.setInt(5, capacity);
                ps.addBatch();
                importedRows++;
                printImportProgress("memory.jsonl", processedRows, totalRows, importedRows);
            }

            hddPs.executeBatch();
            ssdPs.executeBatch();
            m2Ps.executeBatch();
        }

        finishImportProgress("memory.jsonl", totalRows, importedRows);
    }

    private void importRows(Connection connection, String fileName, String sql, RowMapper rowMapper) throws SQLException {
        List<JsonObject> rows = readJsonl(fileName);
        int totalRows = rows.size();
        int importedRows = 0;
        int processedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (JsonObject row : rows) {
                processedRows++;
                if (rowMapper.map(row, ps)) {
                    ps.addBatch();
                    importedRows++;
                }
                printImportProgress(fileName, processedRows, totalRows, importedRows);
            }
            ps.executeBatch();
        }

        finishImportProgress(fileName, totalRows, importedRows);
    }

    private void printImportProgress(String fileName, int processedRows, int totalRows, int importedRows) {
        System.out.print("\r" + fileName + ":" + (processedRows * 100 / totalRows) + "%" + " importiert " + importedRows);
        System.out.flush();
    }

    private void finishImportProgress(String fileName, int totalRows, int importedRows) {
        System.out.print("\r" + fileName + ":" + 100 + "%" + " importiert mit " + importedRows + " validen Einträgen.");
        System.out.println();
    }

    private boolean anyNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    private interface RowMapper {
        boolean map(JsonObject row, PreparedStatement statement) throws SQLException;
    }

    private int ensureNamedId(Connection connection, String tableName, String value) throws SQLException {
        String insertSql = "INSERT OR IGNORE INTO " + tableName + " (name) VALUES (?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
            insert.setString(1, value);
            insert.executeUpdate();
        }

        String selectSql = "SELECT id FROM " + tableName + " WHERE name = ?";
        try (PreparedStatement select = connection.prepareStatement(selectSql)) {
            select.setString(1, value);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Konnte ID nicht auflösen: " + tableName + " -> " + value);
    }

    private List<JsonObject> readJsonl(String fileName) {
        Path path = resolveDataPath(fileName);
        try {
            List<JsonObject> rows = new ArrayList<>();
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                JsonElement element = JsonParser.parseString(trimmed);
                if (element.isJsonObject()) {
                    rows.add(element.getAsJsonObject());
                }
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalStateException("JSONL-Datei konnte nicht gelesen werden: " + path, e);
        }
    }

    private Path resolveDataPath(String fileName) {
        if (dataDirectory != null) {
            Path configuredPath = dataDirectory.resolve(fileName);
            if (Files.exists(configuredPath)) {
                return configuredPath;
            }
            throw new IllegalStateException("JSONL-Datei nicht gefunden: " + configuredPath);
        }

        Path direct = Paths.get("data", fileName);
        if (Files.exists(direct)) {
            return direct;
        }

        Path nested = Paths.get(".").toAbsolutePath().normalize().resolve("data").resolve(fileName);
        if (Files.exists(nested)) {
            return nested;
        }

        throw new IllegalStateException("JSONL-Datei nicht gefunden: data/" + fileName);
    }

    private String getString(JsonObject object, String key) {
        if (isJsonNullOrMissing(object, key)) {
            return null;
        }
        return object.get(key).getAsString();
    }

    private Double getDouble(JsonObject object, String key) {
        if (isJsonNullOrMissing(object, key)) {
            return null;
        }
        try {
            return object.get(key).getAsDouble();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInt(JsonObject object, String key) {
        if (isJsonNullOrMissing(object, key)) {
            return null;
        }
        try {
            return object.get(key).getAsInt();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isJsonNullOrMissing(JsonObject object, String key) {
        return !object.has(key) || object.get(key).isJsonNull();
    }

    private Integer readModulesCapacity(JsonObject row) {
        if (isJsonNullOrMissing(row, "modules") || !row.get("modules").isJsonArray()) {
            return null;
        }

        try {
            return row.getAsJsonArray("modules").get(0).getAsInt() * row.getAsJsonArray("modules").get(1).getAsInt();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer readSpeedMhz(JsonObject row) {
        if (isJsonNullOrMissing(row, "speed") || !row.get("speed").isJsonArray()) {
            return null;
        }

        try {
            return row.getAsJsonArray("speed").get(1).getAsInt();
        } catch (Exception e) {
            return null;
        }
    }

    private String inferCpuSocket(String name, String microarchitecture) {
        String normalizedName = name == null ? "" : name.toLowerCase(Locale.ROOT);
        String architecture = microarchitecture == null ? "" : microarchitecture.toLowerCase(Locale.ROOT);

        if (normalizedName.contains("intel")) {
            return inferIntelSocket(architecture, normalizedName);
        }

        if (normalizedName.contains("amd")) {
            return inferAmdSocket(architecture, normalizedName);
        }

        if (architecture.contains("zen 4") || architecture.contains("zen 5")) {
            return "AM5";
        }
        if (architecture.contains("zen")) {
            return "AM4";
        }

        return "AM5";
    }

    private String inferIntelSocket(String architecture, String normalizedName) {
        if (architecture.contains("arrow lake")) {
            return "LGA1851";
        }
        if (architecture.contains("raptor") || architecture.contains("alder")) {
            return "LGA1700";
        }
        if (architecture.contains("rocket") || architecture.contains("comet")) {
            return "LGA1200";
        }
        if (architecture.contains("coffee") || architecture.contains("kaby") || architecture.contains("skylake")) {
            return "LGA1151";
        }
        if (architecture.contains("haswell") || architecture.contains("broadwell")) {
            return "LGA1150";
        }
        if (architecture.contains("ivy") || architecture.contains("sandy")) {
            return "LGA1155";
        }
        if (architecture.contains("nehalem") || architecture.contains("westmere")) {
            return "LGA1366";
        }
        if (architecture.contains("cascade lake")) {
            return "LGA3647";
        }
        if (architecture.contains("wolfdale") || architecture.contains("yorkfield") || architecture.equals("core")) {
            return "LGA775";
        }

        if (normalizedName.matches(".*\\bi[3579]-1[234]\\d{3}.*")) {
            return "LGA1700";
        }
        if (normalizedName.matches(".*\\bi[3579]-1[01]\\d{3}.*")) {
            return "LGA1200";
        }

        return "LGA1700";
    }

    private String inferAmdSocket(String architecture, String normalizedName) {
        if (normalizedName.contains("threadripper")) {
            if (architecture.contains("zen 4") || architecture.contains("zen 5")) {
                return "sTR5";
            }
            if (architecture.contains("zen 2")) {
                return "sTRX4";
            }
            return "TR4";
        }

        if (architecture.contains("zen 4") || architecture.contains("zen 5")) {
            return "AM5";
        }
        if (architecture.contains("zen")) {
            return "AM4";
        }
        if (architecture.contains("bulldozer") || architecture.contains("piledriver")
            || architecture.contains("steamroller") || architecture.contains("excavator")) {
            return "AM3+";
        }
        if (architecture.contains("jaguar") || architecture.contains("puma") || architecture.contains("lynx")) {
            return "FM2+";
        }
        if (architecture.contains("k10")) {
            return "AM3";
        }

        return "AM4";
    }

    private String normalizeMotherboardFormFactor(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }

        String upper = rawValue.toUpperCase(Locale.ROOT);
        if (upper.contains("MICRO")) {
            return "Micro-ATX";
        }
        if (upper.contains("MINI") || upper.contains("ITX")) {
            return "Mini-ITX";
        }
        if (upper.contains("ATX")) {
            return "ATX";
        }

        return rawValue.trim();
    }

    private String normalizePsuFormFactor(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }

        String upper = rawValue.toUpperCase(Locale.ROOT);
        if (upper.contains("SFX")) {
            return "SFX";
        }
        if (upper.contains("ATX")) {
            return "ATX";
        }

        return rawValue.trim();
    }

    private String inferCaseMotherboardFormFactor(String caseType) {
        String upper = caseType.toUpperCase(Locale.ROOT);
        if (upper.contains("MICRO")) {
            return "Micro-ATX";
        }
        if (upper.contains("MINI") || upper.contains("ITX")) {
            return "Mini-ITX";
        }
        return "ATX";
    }

    private StorageTarget classifyStorage(JsonObject row) {
        if (!isJsonNullOrMissing(row, "type") && row.get("type").isJsonPrimitive() && row.get("type").getAsJsonPrimitive().isNumber()) {
            return StorageTarget.HDD;
        }

        String formFactor = getString(row, "form_factor");
        if (formFactor != null && formFactor.toUpperCase(Locale.ROOT).contains("M.2")) {
            return StorageTarget.M2SSD;
        }

        return StorageTarget.SSD;
    }

    private String extractManufacturer(String productName) {
        if (productName == null || productName.isBlank()) {
            return "Unknown";
        }

        String[] knownManufacturers = {
            "ASRock", "Acer", "Antec", "Apevia", "Asus", "Cooler Master", "Corsair", "Crucial",
            "Fractal Design", "G.Skill", "Gigabyte", "HYTE", "Intel", "Kingston", "Klevv", "Lexar",
            "Lian Li", "MSI", "Montech", "Mushkin", "NVIDIA", "NZXT", "Okinos", "PNY", "Patriot",
            "Phanteks", "PowerColor", "Sabrent", "SK Hynix", "Samsung", "Sapphire", "Seagate",
            "Silicon Power", "TEAMGROUP", "Thermaltake", "Western Digital", "XFX", "be quiet!"
        };

        for (String manufacturer : knownManufacturers) {
            if (productName.toLowerCase(Locale.ROOT).startsWith(manufacturer.toLowerCase(Locale.ROOT))) {
                return manufacturer;
            }
        }

        String[] parts = productName.split("\\s+");
        return parts.length > 0 ? parts[0] : "Unknown";
    }

    private String stripManufacturerPrefix(String productName) {
        if (productName == null || productName.isBlank()) {
            return productName;
        }

        String manufacturer = extractManufacturer(productName);
        if (manufacturer == null || manufacturer.equals("Unknown")) {
            return productName.trim();
        }

        String trimmed = productName.trim();
        if (trimmed.regionMatches(true, 0, manufacturer, 0, manufacturer.length())) {
            String remainder = trimmed.substring(manufacturer.length()).trim();
            return remainder.isEmpty() ? trimmed : remainder;
        }

        return trimmed;
    }

    private enum StorageTarget {
        HDD,
        SSD,
        M2SSD
    }
}