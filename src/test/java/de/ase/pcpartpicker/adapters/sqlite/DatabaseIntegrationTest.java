package de.ase.pcpartpicker.adapters.sqlite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.sqlite.repositories.*;
import de.ase.pcpartpicker.domain.*;
import de.ase.pcpartpicker.domain.HelperClasses.*;
import de.ase.pcpartpicker.part_assembly.*;

@Execution(ExecutionMode.SAME_THREAD)
class DatabaseIntegrationTest {

    private static final int TEST_IMPORT_LIMIT = 100;
    private static final String[] JSONL_FILES = {
        "cpu.jsonl",
        "gpu.jsonl",
        "ram.jsonl",
        "mobo.jsonl",
        "psu.jsonl",
        "case.jsonl",
        "memory.jsonl"
    };

    private ConnectionFactory connectionFactory;

    @BeforeEach
    void setupFreshDatabase() {
        Path reducedDataDir;
        try {
            Path testDbDir = Path.of("target", "test-dbs");
            Files.createDirectories(testDbDir);
            String testDbUrl = "jdbc:sqlite:" + testDbDir.resolve("integration-" + System.nanoTime() + ".db");
            connectionFactory = new ConnectionFactory(testDbUrl);

            reducedDataDir = testDbDir.resolve("data-" + System.nanoTime());
            createReducedJsonlDataset(reducedDataDir, TEST_IMPORT_LIMIT);
        } catch (Exception e) {
            throw new IllegalStateException("Test-Datenbank konnte nicht vorbereitet werden.", e);
        }

        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory, reducedDataDir);
        databaseInitializer.initialize();
    }

    private void createReducedJsonlDataset(Path targetDataDir, int maxLinesPerFile) throws Exception {
        Files.createDirectories(targetDataDir);
        Path sourceDataDir = Path.of("data");

        for (String fileName : JSONL_FILES) {
            Path source = sourceDataDir.resolve(fileName);
            Path target = targetDataDir.resolve(fileName);

            if (!Files.exists(source)) {
                throw new IllegalStateException("Test-JSONL-Datei fehlt: " + source);
            }

            List<String> lines;
            try (Stream<String> stream = Files.lines(source)) {
                lines = stream.limit(maxLinesPerFile).collect(Collectors.toList());
            }

            Files.write(target, lines);
        }
    }

    @Test
    void initializerSeedsComponentsFromJsonl() {
        assertFalse(new CpuRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new GpuRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new RamRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new HddRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new SsdRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new M2SsdRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new MainboardRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new PsuRepository(connectionFactory).findAll().isEmpty());
        assertFalse(new CaseRepository(connectionFactory).findAll().isEmpty());
    }

    @Test
    void userRepositoryCanSaveAndLoadUser() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        String userName = "Integration Test User " + System.nanoTime();

        User savedUser = userRepository.save(userName);
        assertTrue(savedUser.getId() > 0);

        User loadedUser = userRepository.findById(savedUser.getId());
        assertNotNull(loadedUser);
        assertEquals(savedUser.getId(), loadedUser.getId());
        assertEquals(userName, loadedUser.getName());
    }

    @Test
    void computerRepositoryCanSaveAndLoadByUserId() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User user = userRepository.save("Computer Repo Test User " + System.nanoTime());
        Computer computer = createCompatibleComputer(connectionFactory);

        int savedComputerId = computerRepository.save(user.getId(), computer);
        assertTrue(savedComputerId > 0);

        List<Computer> userComputers = computerRepository.findAllByUserId(user.getId());
        assertEquals(1, userComputers.size());

        Computer loadedComputer = userComputers.get(0);
        assertEquals(computer.getCPU().getId(), loadedComputer.getCPU().getId());
        assertEquals(computer.getMainboard().getId(), loadedComputer.getMainboard().getId());
        assertEquals(computer.getRAM().getId(), loadedComputer.getRAM().getId());
        assertEquals(computer.getPSU().getId(), loadedComputer.getPSU().getId());
        assertEquals(computer.getComputerCase().getId(), loadedComputer.getComputerCase().getId());
    }

    @Test
    void saveAsDraftWorksWithOnlyCpuSelected() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User user = userRepository.save("Draft User " + System.nanoTime());
        CPU cpu = new CpuRepository(connectionFactory).findAll().get(0);

        ComputerDraft draft = new ComputerDraft();
        draft.startNewDraft();
        draft.setCpu(cpu);

        int draftId = computerRepository.saveAsDraft(user.getId(), draft);
        assertTrue(draftId > 0);

        List<Computer> userComputers = computerRepository.findAllByUserId(user.getId());
        assertEquals(1, userComputers.size());
        Computer loaded = userComputers.get(0);

        assertNotNull(loaded.getCPU());
        assertEquals(cpu.getId(), loaded.getCPU().getId());
        assertNull(loaded.getMainboard());
        assertNull(loaded.getRAM());
        assertNull(loaded.getPSU());
        assertNull(loaded.getComputerCase());
    }

    @Test
    void saveAsDraftUpdatesExistingDraftForOwner() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User user = userRepository.save("Draft Edit User " + System.nanoTime());
        CPU cpu = new CpuRepository(connectionFactory).findAll().get(0);
        GPU gpu = new GpuRepository(connectionFactory).findAll().get(0);

        ComputerDraft draft = new ComputerDraft();
        draft.startNewDraft();
        draft.setCpu(cpu);
        int firstDraftId = computerRepository.saveAsDraft(user.getId(), draft);

        Computer persistedDraft = computerRepository.findAllByUserId(user.getId()).get(0);
        draft.loadFromComputer(persistedDraft);
        draft.setGpu(gpu);

        int updatedDraftId = computerRepository.saveAsDraft(user.getId(), draft);
        assertEquals(firstDraftId, updatedDraftId);

        List<Computer> userComputers = computerRepository.findAllByUserId(user.getId());
        assertEquals(1, userComputers.size());
        assertNotNull(userComputers.get(0).getGPU());
        assertEquals(gpu.getId(), userComputers.get(0).getGPU().getId());
    }

    @Test
    void saveAsDraftDoesNotOverwriteOtherUsersComputer() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User owner = userRepository.save("Owner " + System.nanoTime());
        User otherUser = userRepository.save("Other " + System.nanoTime());
        CPU cpu = new CpuRepository(connectionFactory).findAll().get(0);

        ComputerDraft ownerDraft = new ComputerDraft();
        ownerDraft.startNewDraft();
        ownerDraft.setCpu(cpu);
        int ownersDraftId = computerRepository.saveAsDraft(owner.getId(), ownerDraft);

        Computer ownerComputer = computerRepository.findAllByUserId(owner.getId()).get(0);
        ComputerDraft foreignEditAttempt = new ComputerDraft();
        foreignEditAttempt.loadFromComputer(ownerComputer);

        int newDraftId = computerRepository.saveAsDraft(otherUser.getId(), foreignEditAttempt);
        assertTrue(newDraftId > 0);
        assertTrue(newDraftId != ownersDraftId);

        assertEquals(1, computerRepository.findAllByUserId(owner.getId()).size());
        assertEquals(1, computerRepository.findAllByUserId(otherUser.getId()).size());
    }

    @Test
    void saveComputerAllowsDuplicateStorageDevices() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User user = userRepository.save("Duplicate Storage User " + System.nanoTime());
        Computer baseComputer = createCompatibleComputer(connectionFactory);
        Storage duplicatedStorage = baseComputer.getStorageDevices().get(0);

        Computer computerWithDuplicateStorage = new Computer.Builder()
            .setCPU(baseComputer.getCPU())
            .setGPU(baseComputer.getGPU())
            .setMainboard(baseComputer.getMainboard())
            .setRAM(baseComputer.getRAM(), baseComputer.getRamModule())
            .setPSU(baseComputer.getPSU())
            .setComputerCase(baseComputer.getComputerCase())
            .setStorageDevices(new Storage[] { duplicatedStorage, duplicatedStorage })
            .build();

        assertNotNull(computerWithDuplicateStorage);

        int savedComputerId = computerRepository.save(user.getId(), computerWithDuplicateStorage);
        assertTrue(savedComputerId > 0);

        List<Computer> userComputers = computerRepository.findAllByUserId(user.getId());
        assertEquals(1, userComputers.size());
        assertEquals(2, userComputers.get(0).getStorageDevices().size());
    }

    @Test
    void deleteByIdForUserDeletesOwnedComputer() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User owner = userRepository.save("Delete Owner " + System.nanoTime());
        Computer computer = createCompatibleComputer(connectionFactory);
        int computerId = computerRepository.save(owner.getId(), computer);

        assertTrue(computerRepository.deleteByIdForUser(computerId, owner.getId()));
        assertTrue(computerRepository.findAllByUserId(owner.getId()).isEmpty());
    }

    @Test
    void deleteByIdForUserDoesNotDeleteOtherUsersComputer() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User owner = userRepository.save("Delete Owner " + System.nanoTime());
        User attacker = userRepository.save("Delete Attacker " + System.nanoTime());
        Computer computer = createCompatibleComputer(connectionFactory);
        int computerId = computerRepository.save(owner.getId(), computer);

        assertFalse(computerRepository.deleteByIdForUser(computerId, attacker.getId()));
        assertEquals(1, computerRepository.findAllByUserId(owner.getId()).size());
    }

    private Computer createCompatibleComputer(ConnectionFactory connectionFactory) {
        CpuRepository cpuRepository = new CpuRepository(connectionFactory);
        MainboardRepository mainboardRepository = new MainboardRepository(connectionFactory);
        RamRepository ramRepository = new RamRepository(connectionFactory);
        PsuRepository psuRepository = new PsuRepository(connectionFactory);
        CaseRepository caseRepository = new CaseRepository(connectionFactory);
        GpuRepository gpuRepository = new GpuRepository(connectionFactory);
        SsdRepository ssdRepository = new SsdRepository(connectionFactory);

        List<CPU> cpus = cpuRepository.findAll();
        List<Mainboard> mainboards = mainboardRepository.findAll();
        List<PSU> psus = psuRepository.findAll();

        RAM ram = ramRepository.findAll().get(0);
        GPU gpu = gpuRepository.findAll().get(0);
        Storage storage = ssdRepository.findAll().get(0); // Get first available SSD

        for (Mainboard mainboard : mainboards) {
            CPU compatibleCpu = cpus.stream()
                .filter(cpu -> cpu.getSocket().equals(mainboard.getSocket()))
                .findFirst()
                .orElse(null);

            if (compatibleCpu == null) {
                continue;
            }

            Case compatibleCase = caseRepository.findAll().stream()
                .filter(pcCase -> pcCase.getMotherboardFormFactor().equals(mainboard.getFormFactor()))
                .findFirst()
                .orElse(null);

            if (compatibleCase == null) {
                continue;
            }

            PSU compatiblePsu = psus.stream()
                .filter(psu -> psu.getFormFactor().equals(compatibleCase.getPSUFormFactor()))
                .findFirst()
                .orElse(null);

            if (compatiblePsu == null) {
                continue;
            }

            Computer computer = new Computer.Builder()
                .setCPU(compatibleCpu)
                .setGPU(gpu)
                .setMainboard(mainboard)
                .setRAM(ram, 2) // 2 RAM modules
                .setPSU(compatiblePsu)
                .setComputerCase(compatibleCase)
                .setStorageDevices(new Storage[] {storage})
                .build();

            if (computer != null) {
                return computer;
            }
        }

        throw new IllegalStateException("Keine kompatible Test-Konfiguration gefunden.");
    }
}
