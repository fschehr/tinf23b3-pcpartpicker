package de.ase.pcpartpicker.adapters.sqlite.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.part_assembly.Computer;

/**
 * Repository zum Laden und Speichern von Computer-Konfigurationen.
 *
 * <p>Für das Lesen wird bewusst ein schlankes SQL auf der Tabelle {@code computer} verwendet.
 * Die referenzierten Komponenten (CPU, GPU, Mainboard, RAM, PSU, Case) werden einmalig über die
 * bestehenden Komponenten-Repositories geladen und in Maps indexiert. Dadurch bleibt die SQL-Abfrage
 * kurz und das N+1-Problem wird vermieden.</p>
 */
public class ComputerRepository {

    private final ConnectionFactory connectionFactory;
    private static final String BASE_COMPUTER_SELECT = """
        SELECT id, user_id, cpu_id, gpu_id, mainboard_id, ram_id, psu_id, case_id
        FROM computer
        """;

    public ComputerRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
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
        List<Computer> computers = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                computers.add(mapComputer(resultSet, index));
            }
            return computers;

        } catch (SQLException e) {
            throw new IllegalStateException("Computer-Daten konnten nicht geladen werden.", e);
        }
    }

    /**
     * Lädt alle gespeicherten Computer-Konfigurationen eines bestimmten Users.
     *
     * @param userId ID des Users.
     * @return Liste aller Computer des Users in aufsteigender ID-Reihenfolge.
     * @throws IllegalStateException wenn die Datenbankabfrage fehlschlägt.
     */
    public List<Computer> findAllByUserId(int userId) {
        String sql = BASE_COMPUTER_SELECT + " WHERE user_id = ? ORDER BY id";
        ComponentIndex index = loadComponentIndex();
        List<Computer> computers = new ArrayList<>();

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    computers.add(mapComputer(resultSet, index));
                }
            }
            return computers;

        } catch (SQLException e) {
            throw new IllegalStateException("Computer-Daten konnten nicht geladen werden.", e);
        }
    }

    /**
     * Speichert eine Computer-Konfiguration für einen User.
     *
     * @param userId ID des Users, dem die Konfiguration gehört.
     * @param computer zu speichernde Computer-Konfiguration.
     * @return generierte Primärschlüssel-ID des gespeicherten Eintrags.
     * @throws IllegalStateException wenn der Insert fehlschlägt oder keine ID zurückgegeben wird.
     */
    public int save(int userId, Computer computer) {
        String sql = """
            INSERT INTO computer (user_id, cpu_id, gpu_id, mainboard_id, ram_id, psu_id, case_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            throw new IllegalStateException("Computer konnte nicht gespeichert werden: Keine ID zurückgegeben.");

        } catch (SQLException e) {
            throw new IllegalStateException("Computer konnte nicht gespeichert werden.", e);
        }
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
        int cpuId = resultSet.getInt("cpu_id");
        CPU cpu = getRequired(index.cpus, cpuId, "CPU");

        int gpuId = resultSet.getInt("gpu_id");
        GPU gpu = resultSet.wasNull() ? null : getRequired(index.gpus, gpuId, "GPU");

        int mainboardId = resultSet.getInt("mainboard_id");
        Mainboard mainboard = getRequired(index.mainboards, mainboardId, "Mainboard");

        int ramId = resultSet.getInt("ram_id");
        RAM ram = getRequired(index.rams, ramId, "RAM");

        int psuId = resultSet.getInt("psu_id");
        PSU psu = getRequired(index.psus, psuId, "PSU");

        int caseId = resultSet.getInt("case_id");
        de.ase.pcpartpicker.domain.Case pcCase = getRequired(index.cases, caseId, "Case");

        Computer computer = new Computer.Builder()
            .setCPU(cpu)
            .setGPU(gpu)
            .setMainboard(mainboard)
            .setRAM(ram, 0) // ACHTUNG: 0 ist Platzhalter Anzahl Module muss noch implementiert werden
            .setPSU(psu)
            .setComputerCase(pcCase)
            .build();

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
     * Hilfsmethode zum Umwandeln einer Liste in eine ID-basierte Map.
     * 
     * @param values Liste mit Domain-Objekten.
     * @param idExtractor Funktion zum Extrahieren der ID.
     * @param <T> Typ des Domain-Objekts.
     * @return Map mit ID als Schlüssel und Objekt als Wert.
     */
    private <T> Map<Integer, T> toMapById(List<T> values, Function<T, Integer> idExtractor) {
        return values.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
        //ich halte das nicht mehr lange aus
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
