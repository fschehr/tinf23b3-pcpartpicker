package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HDD;
import de.ase.pcpartpicker.domain.M2SSD;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.SSD;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.part_assembly.Computer;

/**
 * Repository zum Laden und Speichern von Computer-Konfigurationen.
 *
 * <p>Für das Lesen wird bewusst ein schlankes SQL auf der Tabelle {@code computer} verwendet.
 * Die referenzierten Komponenten (CPU, GPU, Mainboard, RAM, PSU, Case) werden einmalig über die
 * bestehenden Komponenten-Repositories geladen und in Maps indexiert. Dadurch bleibt die SQL-Abfrage
 * kurz und das N+1-Problem wird vermieden.</p>
 */
public class ComputerRepository extends JdbcRepository<Computer> {
    private static final String BASE_COMPUTER_SELECT = """
        SELECT id, cpu_id, gpu_id, mainboard_id, ram_id, ram_module_count, psu_id, case_id
        FROM computer
        """;

    public ComputerRepository(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    /**
     * Lädt alle gespeicherten Computer-Konfigurationen.
     *
     * @return Liste aller Computer in aufsteigender ID-Reihenfolge.
     * @throws IllegalStateException wenn die Datenbankabfrage fehlschlägt.
     */
    public List<Computer> findAll() {
        String sql = BASE_COMPUTER_SELECT + " ORDER BY id";
        ComponentIndex index = loadComponentIndex();

        return queryList(
            sql,
            resultSet -> mapComputer(resultSet, index),
            "Computer-Daten konnten nicht geladen werden."
        );
    }

    /**
     * Lädt alle gespeicherten Computer-Konfigurationen eines bestimmten Users über die Config-Tabelle.
     *
     * @param userId ID des Users.
     * @return Liste aller Computer des Users in aufsteigender ID-Reihenfolge.
     * @throws IllegalStateException wenn die Datenbankabfrage fehlschlägt.
     */
    public List<Computer> findAllByUserId(int userId) {
        String sql = """
            SELECT computer.id, computer.cpu_id, computer.gpu_id, computer.mainboard_id, computer.ram_id, computer.ram_module_count, computer.psu_id, computer.case_id
            FROM computer
            JOIN config ON computer.id = config.computer_id
            WHERE config.user_id = ? ORDER BY computer.id
            """;
        ComponentIndex index = loadComponentIndex();

        return queryList(
            sql,
            statement -> statement.setInt(1, userId),
            resultSet -> mapComputer(resultSet, index),
            "Computer-Daten konnten nicht geladen werden."
        );
    }

    /**
     * Speichert eine Computer-Konfiguration und verknüpft sie mit einem User via Config-Tabelle.
     *
     * @param userId ID des Users, dem die Konfiguration gehört.
     * @param computer zu speichernde Computer-Konfiguration.
     * @return generierte Primärschlüssel-ID der Computer-Konfiguration.
     * @throws IllegalStateException wenn der Insert fehlschlägt oder keine ID zurückgegeben wird.
     */
    public int save(int userId, Computer computer) {
        String sqlComputer = """
            INSERT INTO computer (cpu_id, gpu_id, mainboard_id, ram_id, ram_module_count, psu_id, case_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        int computerId = insertAndReturnGeneratedKey(
            sqlComputer,
            statement -> {
                statement.setInt(1, computer.getCpu().getId());
                if (computer.getGpu() != null) {
                    statement.setInt(2, computer.getGpu().getId());
                } else {
                    statement.setNull(2, Types.INTEGER);
                }
                statement.setInt(3, computer.getMainboard().getId());
                statement.setInt(4, computer.getRam().getId());
                statement.setInt(5, computer.getRamModule());
                statement.setInt(6, computer.getPsu().getId());
                statement.setInt(7, computer.getComputerCase().getId());
            },
            "Computer konnte nicht gespeichert werden."
        );

        // Erstelle Config-Eintrag für die User-Computer-Verknüpfung
        String sqlConfig = "INSERT INTO config (user_id, computer_id) VALUES (?, ?)";
        try (Connection connection = connectionFactory.createConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(sqlConfig)) {
            statement.setInt(1, userId);
            statement.setInt(2, computer.getCPU().getId());
            if (computer.getGPU() != null) {
                statement.setInt(3, computer.getGPU().getId());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            statement.setInt(4, computer.getMainboard().getId());
            statement.setInt(5, computer.getRAM().getId());
            statement.setInt(6, computer.getPSU().getId());
            statement.setInt(7, computer.getComputerCase().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Config-Eintrag konnte nicht erstellt werden.", e);
        }

        // Speichere StorageDevices in der computer_storage Tabelle
        saveStorageDevices(computerId, computer.getStorageDevices());

        return computerId;
    }

    /**
     * Baut aus einer Zeile der Tabelle {@code computer} ein vollständiges Domain-Objekt.
     * Die referenzierten Komponenten werden über den vorbereiteten {@link ComponentIndex} aufgelöst.
     *
     * @param resultSet aktuelle Zeile mit Fremdschlüssel-IDs.
     * @param index vorbereiteter Komponenten-Index.
     * @return vollständig aufgebauter {@link Computer}.
     * @throws SQLException bei JDBC-Fehlern.
     * @throws IllegalStateException wenn eine referenzierte Komponente fehlt oder der Builder fehlschlägt.
     */
    private Computer mapComputer(ResultSet resultSet, ComponentIndex index) throws SQLException {
        int computerId = resultSet.getInt("id");
        int cpuId = resultSet.getInt("cpu_id");
        CPU cpu = getRequired(index.cpus, cpuId, "CPU");

        int gpuId = resultSet.getInt("gpu_id");
        GPU gpu = resultSet.wasNull() ? null : getRequired(index.gpus, gpuId, "GPU");

        int mainboardId = resultSet.getInt("mainboard_id");
        Mainboard mainboard = getRequired(index.mainboards, mainboardId, "Mainboard");

        int ramId = resultSet.getInt("ram_id");
        RAM ram = getRequired(index.rams, ramId, "RAM");
        int ramModuleCount = resultSet.getInt("ram_module_count");

        int psuId = resultSet.getInt("psu_id");
        PSU psu = getRequired(index.psus, psuId, "PSU");

        int caseId = resultSet.getInt("case_id");
        de.ase.pcpartpicker.domain.Case pcCase = getRequired(index.cases, caseId, "Case");

        Computer.Builder builder = new Computer.Builder()
            .setCPU(cpu)
            .setGPU(gpu)
            .setMainboard(mainboard)
            .setRAM(ram, ramModuleCount)
            .setPSU(psu)
            .setComputerCase(pcCase);

        // Lade StorageDevices
        List<Storage> storageDevices = loadStorageDevices(computerId, index);
        if (!storageDevices.isEmpty()) {
            builder.setStorageDevices(storageDevices.toArray(new Storage[0]));
        }

        Computer computer = builder.build();

        if (computer == null) {
            throw new IllegalStateException("Computer konnte aus Datenbankeintrag nicht aufgebaut werden.");
        }
        return computer;
    }

    /**
     * Lädt alle verfügbaren Komponenten und indexiert sie nach ID.
     *
     * @return komponentenübergreifender Index für schnelles Auflösen von Fremdschlüsseln.
     */
    private ComponentIndex loadComponentIndex() {
        CpuRepository cpuRepository = new CpuRepository(connectionFactory);
        GpuRepository gpuRepository = new GpuRepository(connectionFactory);
        MainboardRepository mainboardRepository = new MainboardRepository(connectionFactory);
        RamRepository ramRepository = new RamRepository(connectionFactory);
        PsuRepository psuRepository = new PsuRepository(connectionFactory);
        CaseRepository caseRepository = new CaseRepository(connectionFactory);

        return new ComponentIndex(
            toMapById(cpuRepository.findAll(), CPU::getId),
            toMapById(gpuRepository.findAll(), GPU::getId),
            toMapById(mainboardRepository.findAll(), Mainboard::getId),
            toMapById(ramRepository.findAll(), RAM::getId),
            toMapById(psuRepository.findAll(), PSU::getId),
            toMapById(caseRepository.findAll(), de.ase.pcpartpicker.domain.Case::getId)
        );
    }

    /**
     * Speichert StorageDevices für einen Computer.
     *
     * @param computerId ID des Computers.
     * @param storageDevices zu speichernde Storage-Geräte.
     */
    private void saveStorageDevices(int computerId, List<Storage> storageDevices) {
        String sql = "INSERT INTO computer_storage (computer_id, storage_type, storage_id) VALUES (?, ?, ?)";
        try (Connection connection = connectionFactory.createConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Storage storage : storageDevices) {
                statement.setInt(1, computerId);
                if (storage instanceof HDD) {
                    statement.setString(2, "hdd");
                } else if (storage instanceof SSD) {
                    statement.setString(2, "ssd");
                } else if (storage instanceof M2SSD) {
                    statement.setString(2, "m2_ssd");
                }
                statement.setInt(3, storage.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("StorageDevices konnten nicht gespeichert werden.", e);
        }
    }

    /**
     * Lädt alle StorageDevices für einen Computer.
     *
     * @param computerId ID des Computers.
     * @param index vorbereiteter Komponenten-Index für Storage-Lookups.
     * @return Liste der Storage-Geräte für den Computer.
     */
    private List<Storage> loadStorageDevices(int computerId, ComponentIndex index) {
        List<Storage> storageDevices = new ArrayList<>();
        String sql = "SELECT storage_type, storage_id FROM computer_storage WHERE computer_id = ? ORDER BY id";
        try (Connection connection = connectionFactory.createConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, computerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                HddRepository hddRepository = new HddRepository(connectionFactory);
                SsdRepository ssdRepository = new SsdRepository(connectionFactory);
                M2SsdRepository m2SsdRepository = new M2SsdRepository(connectionFactory);

                Map<Integer, HDD> hdds = toMapById(hddRepository.findAll(), HDD::getId);
                Map<Integer, SSD> ssds = toMapById(ssdRepository.findAll(), SSD::getId);
                Map<Integer, M2SSD> m2ssds = toMapById(m2SsdRepository.findAll(), M2SSD::getId);

                while (resultSet.next()) {
                    String storageType = resultSet.getString("storage_type");
                    int storageId = resultSet.getInt("storage_id");

                    if ("hdd".equals(storageType)) {
                        HDD hdd = hdds.get(storageId);
                        if (hdd != null) storageDevices.add(hdd);
                    } else if ("ssd".equals(storageType)) {
                        SSD ssd = ssds.get(storageId);
                        if (ssd != null) storageDevices.add(ssd);
                    } else if ("m2_ssd".equals(storageType)) {
                        M2SSD m2ssd = m2ssds.get(storageId);
                        if (m2ssd != null) storageDevices.add(m2ssd);
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("StorageDevices konnten nicht geladen werden.", e);
        }
        return storageDevices;
    }

    /**
     * Hilfsmethode zum Umwandeln einer Liste in eine ID-basierte Map.
     * 
     * @param values Liste mit Domain-Objekten.
     * @param idExtractor Funktion zum Extrahieren der ID.
     * @param <T> Typ des Domain-Objekts.
     * @return Map mit ID als Schlüssel und Objekt als Wert.
     */
    private <T> Map<Integer, T> toMapById(List<T> values, Function<T, Integer> idExtractor) {
        return values.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
    }

    /**
     * Liefert ein Objekt aus einer ID-Map oder wirft eine Exception, falls die Referenz fehlt.
     *
     * @param map ID-basierte Map.
     * @param id referenzierte ID.
     * @param type fachlicher Typname für die Fehlermeldung.
     * @param <T> Typ des erwarteten Objekts.
     * @return gefundenes Objekt.
     * @throws IllegalStateException wenn kein Eintrag für die ID vorhanden ist.
     */
    private <T> T getRequired(Map<Integer, T> map, int id, String type) {
        T value = map.get(id);
        if (value == null) {
            throw new IllegalStateException(type + " mit ID " + id + " nicht gefunden.");
        }
        return value;
    }

    /**
     * Interner Datenträger für komponentenspezifische ID-Indexe.
     */
    private static class ComponentIndex {
        private final Map<Integer, CPU> cpus;
        private final Map<Integer, GPU> gpus;
        private final Map<Integer, Mainboard> mainboards;
        private final Map<Integer, RAM> rams;
        private final Map<Integer, PSU> psus;
        private final Map<Integer, de.ase.pcpartpicker.domain.Case> cases;

        private ComponentIndex(
            Map<Integer, CPU> cpus,
            Map<Integer, GPU> gpus,
            Map<Integer, Mainboard> mainboards,
            Map<Integer, RAM> rams,
            Map<Integer, PSU> psus,
            Map<Integer, de.ase.pcpartpicker.domain.Case> cases
        ) {
            this.cpus = cpus;
            this.gpus = gpus;
            this.mainboards = mainboards;
            this.rams = rams;
            this.psus = psus;
            this.cases = cases;
        }
    }
}
