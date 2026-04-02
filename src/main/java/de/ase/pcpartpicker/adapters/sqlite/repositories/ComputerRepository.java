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

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
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
        SELECT id, user_id, is_draft, cpu_id, gpu_id, mainboard_id, ram_id, ram_module_count, psu_id, case_id
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

    public List<Computer> findAllByUserId(int userId) {
        String sql = """
            SELECT id, user_id, is_draft, cpu_id, gpu_id, mainboard_id, ram_id, ram_module_count, psu_id, case_id
            FROM computer
            WHERE user_id = ?
            ORDER BY id
            """;
        ComponentIndex index = loadComponentIndex();

        return queryList(
            sql,
            statement -> statement.setInt(1, userId),
            resultSet -> mapComputer(resultSet, index),
            "Computer-Daten konnten nicht geladen werden."
        );
    }

    public int save(int userId, Computer computer) {
        Integer editingComputerId = computer.getId() > 0 ? computer.getId() : null;
        return upsertComputer(userId, editingComputerId, computer, false);
    }

    public int saveAsDraft(int userId, ComputerDraft draft) {
        Computer draftComputer = fromDraft(draft);
        return upsertComputer(userId, draft.getEditingComputerId(), draftComputer, true);
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
        Integer cpuId = getNullableInt(resultSet, "cpu_id");
        CPU cpu = getOptional(index.cpus, cpuId);

        Integer gpuId = getNullableInt(resultSet, "gpu_id");
        GPU gpu = getOptional(index.gpus, gpuId);

        Integer mainboardId = getNullableInt(resultSet, "mainboard_id");
        Mainboard mainboard = getOptional(index.mainboards, mainboardId);

        Integer ramId = getNullableInt(resultSet, "ram_id");
        RAM ram = getOptional(index.rams, ramId);
        int ramModuleCount = resultSet.getInt("ram_module_count");

        Integer psuId = getNullableInt(resultSet, "psu_id");
        PSU psu = getOptional(index.psus, psuId);

        Integer caseId = getNullableInt(resultSet, "case_id");
        de.ase.pcpartpicker.domain.Case pcCase = getOptional(index.cases, caseId);

        Computer.Builder builder = new Computer.Builder().setId(computerId);
        if (cpu != null) {
            builder.setCPU(cpu);
        }
        builder.setGPU(gpu);
        if (mainboard != null) {
            builder.setMainboard(mainboard);
        }
        if (ram != null) {
            builder.setRAM(ram, ramModuleCount);
        }
        if (psu != null) {
            builder.setPSU(psu);
        }
        if (pcCase != null) {
            builder.setComputerCase(pcCase);
        }

        // Lade StorageDevices
        List<Storage> storageDevices = loadStorageDevices(computerId, index);
        if (!storageDevices.isEmpty()) {
            builder.setStorageDevices(storageDevices.toArray(new Storage[0]));
        }

        return builder.buildUnchecked();
    }

    private int upsertComputer(int userId, Integer existingComputerId, Computer computer, boolean isDraft) {
        if (existingComputerId != null && existingComputerId > 0 && isOwnedByUser(existingComputerId, userId)) {
            updateComputer(existingComputerId, userId, computer, isDraft);
            replaceStorageDevices(existingComputerId, computer.getStorageDevices());
            return existingComputerId;
        }

        int createdComputerId = insertComputer(userId, computer, isDraft);
        saveStorageDevices(createdComputerId, computer.getStorageDevices());
        return createdComputerId;
    }

    private Computer fromDraft(ComputerDraft draft) {
        Computer.Builder builder = new Computer.Builder();
        if (draft.getEditingComputerId() != null) {
            builder.setId(draft.getEditingComputerId());
        }
        if (draft.getCPU() != null) {
            builder.setCPU(draft.getCPU());
        }
        builder.setGPU(draft.getGPU());
        if (draft.getMainboard() != null) {
            builder.setMainboard(draft.getMainboard());
        }
        if (draft.getRAM() != null) {
            builder.setRAM(draft.getRAM(), draft.getRamModule());
        }
        if (draft.getPSU() != null) {
            builder.setPSU(draft.getPSU());
        }
        if (draft.getComputerCase() != null) {
            builder.setComputerCase(draft.getComputerCase());
        }
        if (draft.getStorage() != null && !draft.getStorage().isEmpty()) {
            builder.setStorageDevices(draft.getStorage().toArray(new Storage[0]));
        }
        return builder.buildUnchecked();
    }

    private int insertComputer(int userId, Computer computer, boolean isDraft) {
        String sqlComputer = """
            INSERT INTO computer (user_id, is_draft, cpu_id, gpu_id, mainboard_id, ram_id, ram_module_count, psu_id, case_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        return insertAndReturnGeneratedKey(
            sqlComputer,
            statement -> {
                statement.setInt(1, userId);
                statement.setInt(2, isDraft ? 1 : 0);
                bindNullableId(statement, 3, computer.getCPU() == null ? null : computer.getCPU().getId());
                bindNullableId(statement, 4, computer.getGPU() == null ? null : computer.getGPU().getId());
                bindNullableId(statement, 5, computer.getMainboard() == null ? null : computer.getMainboard().getId());
                bindNullableId(statement, 6, computer.getRAM() == null ? null : computer.getRAM().getId());
                statement.setInt(7, computer.getRAM() == null ? 0 : computer.getRamModule());
                bindNullableId(statement, 8, computer.getPSU() == null ? null : computer.getPSU().getId());
                bindNullableId(statement, 9, computer.getComputerCase() == null ? null : computer.getComputerCase().getId());
            },
            "Computer konnte nicht gespeichert werden."
        );
    }

    private void updateComputer(int computerId, int userId, Computer computer, boolean isDraft) {
        String sql = """
            UPDATE computer
            SET is_draft = ?, cpu_id = ?, gpu_id = ?, mainboard_id = ?, ram_id = ?, ram_module_count = ?, psu_id = ?, case_id = ?
            WHERE id = ? AND user_id = ?
            """;

        executeUpdate(
            sql,
            statement -> {
                statement.setInt(1, isDraft ? 1 : 0);
                bindNullableId(statement, 2, computer.getCPU() == null ? null : computer.getCPU().getId());
                bindNullableId(statement, 3, computer.getGPU() == null ? null : computer.getGPU().getId());
                bindNullableId(statement, 4, computer.getMainboard() == null ? null : computer.getMainboard().getId());
                bindNullableId(statement, 5, computer.getRAM() == null ? null : computer.getRAM().getId());
                statement.setInt(6, computer.getRAM() == null ? 0 : computer.getRamModule());
                bindNullableId(statement, 7, computer.getPSU() == null ? null : computer.getPSU().getId());
                bindNullableId(statement, 8, computer.getComputerCase() == null ? null : computer.getComputerCase().getId());
                statement.setInt(9, computerId);
                statement.setInt(10, userId);
            },
            "Computer konnte nicht aktualisiert werden."
        );
    }

    private boolean isOwnedByUser(int computerId, int userId) {
        String sql = "SELECT 1 FROM computer WHERE id = ? AND user_id = ?";
        return queryOptional(
            sql,
            statement -> {
                statement.setInt(1, computerId);
                statement.setInt(2, userId);
            },
            resultSet -> resultSet.getInt(1),
            "Computer-Besitz konnte nicht geprüft werden."
        ).isPresent();
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
        if (storageDevices == null || storageDevices.isEmpty()) {
            return;
        }
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
                } else {
                    continue;
                }
                statement.setInt(3, storage.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("StorageDevices konnten nicht gespeichert werden.", e);
        }
    }

    private void replaceStorageDevices(int computerId, List<Storage> storageDevices) {
        executeUpdate(
            "DELETE FROM computer_storage WHERE computer_id = ?",
            statement -> statement.setInt(1, computerId),
            "StorageDevices konnten nicht aktualisiert werden."
        );
        saveStorageDevices(computerId, storageDevices);
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
    private Integer getNullableInt(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : value;
    }

    private void bindNullableId(java.sql.PreparedStatement statement, int parameterIndex, Integer value) throws SQLException {
        if (value == null) {
            statement.setNull(parameterIndex, Types.INTEGER);
        } else {
            statement.setInt(parameterIndex, value);
        }
    }

    private <T> T getOptional(Map<Integer, T> map, Integer id) {
        if (id == null) {
            return null;
        }
        return map.get(id);
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
