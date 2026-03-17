package de.ase.pcpartpicker.adapters.sqlite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.adapters.sqlite.repositories.*;
import de.ase.pcpartpicker.domain.*;
import de.ase.pcpartpicker.domain.HelperClasses.*;
import de.ase.pcpartpicker.part_assembly.*;

class DatabaseIntegrationTest {

    private ConnectionFactory connectionFactory;

    @BeforeEach
    void setupFreshDatabase() {
        connectionFactory = new ConnectionFactory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);
        databaseInitializer.initialize();
    }

    @Test
    void initializerSeedsUsersAndComponents() {
        UserRepository userRepository = new UserRepository(connectionFactory);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());

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

        User savedUser = userRepository.save("Integration Test User");
        assertTrue(savedUser.getId() > 0);

        User loadedUser = userRepository.findById(savedUser.getId());
        assertNotNull(loadedUser);
        assertEquals(savedUser.getId(), loadedUser.getId());
        assertEquals("Integration Test User", loadedUser.getName());
    }

    @Test
    void computerRepositoryCanSaveAndLoadByUserId() {
        UserRepository userRepository = new UserRepository(connectionFactory);
        ComputerRepository computerRepository = new ComputerRepository(connectionFactory);

        User user = userRepository.save("Computer Repo Test User");
        Computer computer = createCompatibleComputer(connectionFactory);

        int savedComputerId = computerRepository.save(user.getId(), computer);
        assertTrue(savedComputerId > 0);

        List<Computer> userComputers = computerRepository.findAllByUserId(user.getId());
        assertEquals(1, userComputers.size());

        Computer loadedComputer = userComputers.get(0);
        assertEquals(computer.getCpu().getId(), loadedComputer.getCpu().getId());
        assertEquals(computer.getMainboard().getId(), loadedComputer.getMainboard().getId());
        assertEquals(computer.getRam().getId(), loadedComputer.getRam().getId());
        assertEquals(computer.getPsu().getId(), loadedComputer.getPsu().getId());
        assertEquals(computer.getComputerCase().getId(), loadedComputer.getComputerCase().getId());
    }

    private Computer createCompatibleComputer(ConnectionFactory connectionFactory) {
        CpuRepository cpuRepository = new CpuRepository(connectionFactory);
        MainboardRepository mainboardRepository = new MainboardRepository(connectionFactory);
        RamRepository ramRepository = new RamRepository(connectionFactory);
        PsuRepository psuRepository = new PsuRepository(connectionFactory);
        CaseRepository caseRepository = new CaseRepository(connectionFactory);
        GpuRepository gpuRepository = new GpuRepository(connectionFactory);

        List<CPU> cpus = cpuRepository.findAll();
        List<Mainboard> mainboards = mainboardRepository.findAll();
        List<PSU> psus = psuRepository.findAll();

        RAM ram = ramRepository.findAll().get(0);
        GPU gpu = gpuRepository.findAll().get(0);

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
                .setRAM(ram,0)// ACHTUNG: hier auch noch implementieren von Anzahl RAM Module
                .setPSU(compatiblePsu)
                .setComputerCase(compatibleCase)
                .build();

            if (computer != null) {
                return computer;
            }
        }

        throw new IllegalStateException("Keine kompatible Test-Konfiguration gefunden.");
    }
}
