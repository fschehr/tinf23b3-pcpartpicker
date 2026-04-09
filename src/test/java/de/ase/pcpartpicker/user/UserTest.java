package de.ase.pcpartpicker.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.MenuItem;
import de.ase.pcpartpicker.adapters.cli.SessionManager;
import de.ase.pcpartpicker.adapters.cli.commands.BackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.FinishComputerCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LoginCommand;
import de.ase.pcpartpicker.adapters.cli.commands.LogoutCommand;
import de.ase.pcpartpicker.adapters.cli.commands.NewUserCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.SelectComponentCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.MotherboardFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.PSUFormFactor;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.HelperClasses.User;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.SSD;
import de.ase.pcpartpicker.part_assembly.Computer;

public class UserTest {

    private InputStream originalIn;
    private PrintStream originalOut;

    private AppContext context; 

    private InMemoryUserRepository userRepository;
    private InMemoryComputerRepository computerRepository;

    private CPU cpu;
    private GPU gpu;
    private Mainboard mainboard;
    private RAM ram;
    private PSU psu;
    private Case computerCase;
    private SSD ssd;

    @BeforeAll
    public static void setup() {
        System.out.println("Starting User Tests...");
    }

    @BeforeEach
    public void setupEach() {
        originalIn = System.in;
        originalOut = System.out;

        userRepository = new InMemoryUserRepository();
        computerRepository = new InMemoryComputerRepository();
        
        SessionManager.setCurrentUser(null);
        createCompatibleTestComponents();
    }

    @AfterEach
    public void tearDownEach() {
        SessionManager.setCurrentUser(null);
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    
    @Test
    public void registrationTest() {
        String username = "new-user";

        String flowInput = String.join("\n",
            "3",     // Nutzerverwaltung
            "1",     // Neuen Nutzer anlegen
            username, // Name
            "",      // waitForEnter
            "0",     // Zurueck aus Nutzerverwaltung
            "0"      // Beenden Hauptmenue
        ) + "\n";

        String output = runTestFlow(flowInput);

        assertTrue(userRepository.findAll().stream().anyMatch(u -> u.getName().equals(username)));
        assertTrue(output.contains("Nutzer erfolgreich angelegt!"));
    }

    @Test
    public void loginTest() {
        String username = "login-user";

        String flowInput = String.join("\n",
            "3", "1", username, "", "0", // Nutzer anlegen
            "4", "1", username, "", "0", // Login -> Starten -> Username -> Enter -> Zurück
            "0"                          // Beenden Hauptmenü
        ) + "\n";

        String output = runTestFlow(flowInput);

        assertTrue(SessionManager.isLoggedIn());
        assertEquals(username, SessionManager.getcurrentUser().getName());
        assertTrue(output.contains("Willkommen zurück"));
    }

    @Test
    public void displayTestUser() {
        String username = "display-user";

        String flowInput = String.join("\n",
            "3", "1", username, "", // Nutzer anlegen
            "2", "0",               // Nutzerliste anzeigen (Paging), dann Action.BACK (0)
            "0",                    // Nutzerverwaltung zurueck
            "0"                     // Beenden
        ) + "\n";

        String output = runTestFlow(flowInput);

        assertTrue(output.contains("Nutzer"));
        assertTrue(output.contains(username));
    }

    @Test
    public void createCorrectComputer() {
        String username = "builder-user";

        String flowInput = String.join("\n",
            "3", "1", username, "", "0", // Nutzer anlegen
            "4", "1", username, "", "0", // Login
            "2", "1",                    // Computerverwaltung -> Neu anlegen
            "1", "1", "", "0",           // CPU -> ID 1 -> Enter -> Zurück Paging
            "2", "1", "", "0",           // GPU -> ID 1 -> Enter -> Zurück Paging
            "3", "1", "", "0",           // Mainboard -> ID 1 -> Enter -> Zurück Paging
            "4", "1", "4", "", "0",      // RAM -> ID 1 -> 4 Module -> Enter -> Zurück Paging
            "5", "1", "", "0",           // PSU -> ID 1 -> Enter -> Zurück Paging
            "6", "1", "", "0",           // Case -> ID 1 -> Enter -> Zurück Paging
            "7", "1", "", "0",           // SSD -> ID 1 -> Enter -> Zurück Paging
            "8", "",                     // Finish + waitForEnter
            "0",                         // Zurueck aus Konfigurator
            "0",                         // Zurueck aus Computerverwaltung
            "0"                          // Beenden
        ) + "\n";

        String output = runTestFlow(flowInput);

        User currentUser = SessionManager.getcurrentUser();
        assertEquals(1, computerRepository.findAllByUserId(currentUser.getId()).size());
        assertTrue(output.contains("ERFOLG"));
    }

    @Test
    public void createWrongComputer() {
        String username = "wrong-build-user";

        String flowInput = String.join("\n",
            "3", "1", username, "", "0", // Nutzer anlegen
            "4", "1", username, "", "0", // Login
            "2", "1",                    // Computerverwaltung -> Neu anlegen
            "8", "",                     // Versuch speichern ohne Komponenten -> Print Error & Enter
            "0",                         // Zurueck Konfigurator
            "0",                         // Zurueck Computerverwaltung
            "0"                          // Beenden
        ) + "\n";

        String output = runTestFlow(flowInput);

        User currentUser = SessionManager.getcurrentUser();
        assertTrue(computerRepository.findAllByUserId(currentUser.getId()).isEmpty());
        assertTrue(output.contains("FEHLER"));
    }

    @Test
    public void logoutTest() {
        String username = "logout-user";

        String flowInput = String.join("\n",
            "3", "1", username, "", "0", // Nutzer anlegen
            "4", "1", username, "",      // Login -> Starten -> Username -> Enter
            "2",                         // Logout (im Login Menu)
            "0",                         // Zurueck Login-Menue
            "0"                          // Beenden Hauptmenue
        ) + "\n";

        runTestFlow(flowInput);

        assertFalse(SessionManager.isLoggedIn());
        assertNull(SessionManager.getcurrentUser());
    }

    private void createCompatibleTestComponents() {
        Manufacturer manufacturer = new Manufacturer(1, "Test Manufacturer");
        Socket socket = new Socket(1, "AM4");
        MotherboardFormFactor motherboardFormFactor = new MotherboardFormFactor(1, "ATX");
        PSUFormFactor psuFormFactor = new PSUFormFactor(1, "SFX");

        cpu = new CPU(1, "Test CPU", 199.99, manufacturer, socket, 3.8, false, 65);
        gpu = new GPU(1, "Test GPU", 299.99, manufacturer, 8, 150);
        mainboard = new Mainboard(1, "Test Mainboard", 149.99, manufacturer, socket, motherboardFormFactor, 4, 2, 4, 1);
        ram = new RAM(1, "Test RAM", 79.99, manufacturer, 16, 3200);
        psu = new PSU(1, "Test PSU", 89.99, manufacturer, 650, psuFormFactor);
        computerCase = new Case(1, "Test Case", 59.99, manufacturer, motherboardFormFactor, psuFormFactor, true, 10);
        ssd = new SSD(1, "Test SSD", 69.99, manufacturer, 512);
    }

    private Menu createConfiguratorMenu(ComputerDraft draft, InputReader inputReader) {
        Menu menu = new Menu("PC Konfigurator (Entwurf)", inputReader);
        menu.setZeroComponent(new BackCommand(() -> menu.setRunning(false)));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("CPU auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("CPUs", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(cpu)), c -> new String[] {
                String.valueOf(c.getId()), c.getName(), c.getPrice() + ""
            }),
            (d, c) -> d.setCpu(c)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("GPU auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("GPUs", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(gpu)), g -> new String[] {
                String.valueOf(g.getId()), g.getName(), g.getPrice() + ""
            }),
            (d, g) -> d.setGpu(g)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("Mainboard auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("Mainboards", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(mainboard)), m -> new String[] {
                String.valueOf(m.getId()), m.getName(), m.getPrice() + ""
            }),
            (d, m) -> d.setMainboard(m)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("RAM auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("RAM", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(ram)), r -> new String[] {
                String.valueOf(r.getId()), r.getName(), r.getPrice() + ""
            }),
            (d, r) -> {
                int amount = inputReader.readInt("Wie viele dieser Module willst du verbauen?", 1, d.getMainboardRamSlots());
                d.setRam(r, amount);
            }
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("PSU auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("Netzteile", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(psu)), p -> new String[] {
                String.valueOf(p.getId()), p.getName(), p.getPrice() + ""
            }),
            (d, p) -> d.setPsu(p)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("Case auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("Cases", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(computerCase)), c -> new String[] {
                String.valueOf(c.getId()), c.getName(), c.getPrice() + ""
            }),
            (d, c) -> d.setComputerCase(c)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem("SSD auswaehlen", new SelectComponentCommand<>(
            inputReader,
            draft,
            listConfig("SSDs", new String[] { "#", "Name", "Preis" }, fixedRepo(List.of(ssd)), s -> new String[] {
                String.valueOf(s.getId()), s.getName(), s.getPrice() + ""
            }),
            (d, s) -> d.addStorage(s)
        )));

        menu.add(new de.ase.pcpartpicker.adapters.cli.MenuItem(
            "Computer pruefen & speichern",
            new FinishComputerCommand(context)
        ));

        return menu;
    }


private String runTestFlow(String flowInput) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(flowInput.getBytes(StandardCharsets.UTF_8)));

        context = new AppContext(); 

        try {
            java.lang.reflect.Field userRepoField = AppContext.class.getDeclaredField("userRepository");
            userRepoField.setAccessible(true);
            userRepoField.set(context, userRepository);

            java.lang.reflect.Field compRepoField = AppContext.class.getDeclaredField("computerRepository");
            compRepoField.setAccessible(true);
            compRepoField.set(context, computerRepository);

            // WICHTIG: Sicherstellen, dass computerDraft im Context existiert
            java.lang.reflect.Field draftField = AppContext.class.getDeclaredField("computerDraft");
            draftField.setAccessible(true);
            if (draftField.get(context) == null) {
                draftField.set(context, new ComputerDraft());
            }
            
            // Sicherstellen, dass inputReader da ist
            java.lang.reflect.Field readerField = AppContext.class.getDeclaredField("inputReader");
            readerField.setAccessible(true);
            if (readerField.get(context) == null) {
                readerField.set(context, new InputReader());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputReader currentReader = null;
        try {
            java.lang.reflect.Field readerField = AppContext.class.getDeclaredField("inputReader");
            readerField.setAccessible(true);
            currentReader = (InputReader) readerField.get(context);
        } catch(Exception e) {}

        createMainMenu(currentReader).execute();
        
        return outContent.toString(StandardCharsets.UTF_8);
    }

    private Menu createMainMenu(InputReader inputReader) {
        Menu mainMenu = new Menu("PC Part Picker - Hauptmenue", inputReader);
        Menu computerMenu = new Menu("Computerverwaltung", inputReader);
        Menu userMenu = new Menu("Nutzerverwaltung", inputReader);
        Menu loginMenu = new Menu("Login", inputReader);

        mainMenu.setZeroComponent(new BackCommand(() -> mainMenu.setRunning(false)));
        computerMenu.setZeroComponent(new BackCommand(() -> computerMenu.setRunning(false)));
        userMenu.setZeroComponent(new BackCommand(() -> userMenu.setRunning(false)));
        loginMenu.setZeroComponent(new BackCommand(() -> loginMenu.setRunning(false)));

        // FIX: Wir holen uns exakt den Draft, den der AppContext (und damit der FinishCommand) benutzt!
        ComputerDraft draft = null;
        try {
            java.lang.reflect.Field draftField = AppContext.class.getDeclaredField("computerDraft");
            draftField.setAccessible(true);
            draft = (ComputerDraft) draftField.get(context);
        } catch (Exception e) {
            e.printStackTrace();
            draft = new ComputerDraft(); // Fallback
        }

        Menu configuratorMenu = createConfiguratorMenu(draft, inputReader);

        computerMenu.add(new MenuItem("Neuen Computer anlegen", () -> {
            // Hier greifen wir auf den gemappten Draft zu
            try {
                java.lang.reflect.Field draftField = AppContext.class.getDeclaredField("computerDraft");
                draftField.setAccessible(true);
                ComputerDraft currentDraft = (ComputerDraft) draftField.get(context);
                currentDraft.startNewDraft();
            } catch (Exception e) {}
            configuratorMenu.execute();
        }));
        computerMenu.add(new MenuItem("Meine Computer anzeigen", () -> {}));

        userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(inputReader, userRepository)));

        ShowListCommand<User> showUserList = new ShowListCommand<>(
            listConfig("Nutzer", new String[] { "ID", "Name" }, userRepository, user -> new String[] {
                String.valueOf(user.getId()), user.getName()
            }),
            inputReader
        );
        userMenu.add(new MenuItem("Zeige alle Nutzer", () -> {
            showUserList.render("Nutzer");
        }));
        loginMenu.add(new MenuItem("Login starten", new LoginCommand(inputReader, userRepository)));
        loginMenu.add(new MenuItem("Logout", new LogoutCommand()));

        mainMenu.add(new MenuItem("Teile ansehen", new OpenMenuCommand(new Menu("Leer", inputReader))));
        mainMenu.add(new MenuItem("Computerverwaltung", new OpenMenuCommand(computerMenu)));
        mainMenu.add(new MenuItem("Nutzerverwaltung", new OpenMenuCommand(userMenu)));
        mainMenu.add(new MenuItem("Login", new OpenMenuCommand(loginMenu)));

        return mainMenu;
    }
    // private Menu createMainMenu(InputReader inputReader) {
    //     Menu mainMenu = new Menu("PC Part Picker - Hauptmenue", inputReader);
    //     Menu computerMenu = new Menu("Computerverwaltung", inputReader);
    //     Menu userMenu = new Menu("Nutzerverwaltung", inputReader);
    //     Menu loginMenu = new Menu("Login", inputReader);

    //     mainMenu.setZeroComponent(new BackCommand(() -> mainMenu.setRunning(false)));
    //     computerMenu.setZeroComponent(new BackCommand(() -> computerMenu.setRunning(false)));
    //     userMenu.setZeroComponent(new BackCommand(() -> userMenu.setRunning(false)));
    //     loginMenu.setZeroComponent(new BackCommand(() -> loginMenu.setRunning(false)));

    //     ComputerDraft draft = new ComputerDraft();
    //     Menu configuratorMenu = createConfiguratorMenu(draft, inputReader);

    //     computerMenu.add(new MenuItem("Neuen Computer anlegen", () -> {
    //         draft.startNewDraft();
    //         configuratorMenu.execute();
    //     }));
    //     computerMenu.add(new MenuItem("Meine Computer anzeigen", () -> {}));

    //     userMenu.add(new MenuItem("Neuen Nutzer anlegen", new NewUserCommand(inputReader, userRepository)));

    //     ShowListCommand<User> showUserList = new ShowListCommand<>(
    //         listConfig("Nutzer", new String[] { "ID", "Name" }, userRepository, user -> new String[] {
    //             String.valueOf(user.getId()), user.getName()
    //         }),
    //         inputReader
    //     );
    //     userMenu.add(new MenuItem("Zeige alle Nutzer", () -> {
    //         showUserList.render("Nutzer");
    //     }));
    //     loginMenu.add(new MenuItem("Login starten", new LoginCommand(inputReader, userRepository)));
    //     loginMenu.add(new MenuItem("Logout", new LogoutCommand()));

    //     mainMenu.add(new MenuItem("Teile ansehen", new OpenMenuCommand(new Menu("Leer", inputReader))));
    //     mainMenu.add(new MenuItem("Computerverwaltung", new OpenMenuCommand(computerMenu)));
    //     mainMenu.add(new MenuItem("Nutzerverwaltung", new OpenMenuCommand(userMenu)));
    //     mainMenu.add(new MenuItem("Login", new OpenMenuCommand(loginMenu)));

    //     return mainMenu;
    // }

    private <T> de.ase.pcpartpicker.adapters.cli.rListConfiguration<T> listConfig(
        String title,
        String[] headers,
        Repository<T> repository,
        java.util.function.Function<T, String[]> rowMapper
    ) {
        return new de.ase.pcpartpicker.adapters.cli.rListConfiguration<>(title, headers, repository, rowMapper);
    }

    private <T> Repository<T> fixedRepo(List<T> items) {
        return new FixedRepository<>(items);
    }

    private static final class FixedRepository<T> implements Repository<T> {
        private final List<T> items;

        private FixedRepository(List<T> items) {
            this.items = items;
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(items);
        }
    }

    private static final class InMemoryUserRepository extends UserRepository {
        private final List<User> users = new ArrayList<>();
        private int nextId = 1;

        private InMemoryUserRepository() {
            super(null);
            save("test user");
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(users);
        }

        @Override
        public User findById(int id) {
            return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
        }

        @Override
        public User save(String name) {
            User user = new User(nextId++, name);
            users.add(user);
            return user;
        }
    }

    private static final class InMemoryComputerRepository extends ComputerRepository {
        private final List<Computer> allComputers = new ArrayList<>();
        private final Map<Integer, List<Computer>> byUser = new HashMap<>();
        private int nextId = 1;

        private InMemoryComputerRepository() {
            super(null);
        }

        @Override
        public List<Computer> findAll() {
            return new ArrayList<>(allComputers);
        }

        @Override
        public List<Computer> findAllByUserId(int userId) {
            return new ArrayList<>(byUser.getOrDefault(userId, List.of()));
        }

        @Override
        public int save(int userId, Computer computer) {
            int id = nextId++;
            allComputers.add(computer);
            byUser.computeIfAbsent(userId, ignored -> new ArrayList<>()).add(computer);
            return id;
        }
    }
}