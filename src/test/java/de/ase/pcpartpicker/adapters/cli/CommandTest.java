package de.ase.pcpartpicker.adapters.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import de.ase.pcpartpicker.adapters.cli.commands.*;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComponentRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComputerRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.UserRepository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.domain.HelperClasses.Manufacturer;
import de.ase.pcpartpicker.domain.HelperClasses.Socket;
import de.ase.pcpartpicker.domain.HelperClasses.User;

class DummyMenu extends Menu {
    boolean opened = false;
    public DummyMenu(String title, InputReader reader) {
        super(title, reader);
    }
    @Override
    public void execute() {
        opened = true;
        setRunning(false); // WICHTIG: Schleife sofort beenden
    }
}

public class CommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    
    class DummyUserRepository extends UserRepository {
        List<User> users = new ArrayList<>();

        public DummyUserRepository() {
            super(null);
        }

        @Override
        public List<User> findAll() {
            return users;
        }

        @Override
        public User save(String name) {
            User u = new User(users.size() + 1, name);
            users.add(u);
            return u;
        }
    }

    class DummyComputerRepository extends ComputerRepository {
        public DummyComputerRepository() {
            super(new ConnectionFactory("jdbc:sqlite::memory:"));
        }

        @Override
        public int saveAsDraft(int userId, ComputerDraft draft) {
            return 99; 
        }
    }

    @BeforeAll
    public static void setup() {
        System.out.println("Starting Command Tests...");
    }

    @BeforeEach
    public void setupEach() {
        SessionManager.setCurrentUser(null); 
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void teardown() {
        SessionManager.setCurrentUser(null);
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


    private InputReader provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        return new InputReader(); // Erstellt einen Scanner, der an den NEUEN testIn gebunden ist
    }

@Test
    @DisplayName("Testet, ob StartCommand das Hauptmenü startet und ausgibt")
    void startCommandExecutesMenu() {

        provideInput("");

        StartCommand startCommand = new StartCommand();

        try {
            startCommand.execute();
        } catch (java.util.NoSuchElementException e) {
        }

        String output = outContent.toString();
        assertTrue(output.contains("PC Part Picker - Hauptmenü"), "Das Hauptmenü sollte ausgegeben werden.");
    }

    @Test
    @DisplayName("Testet, ob ein Menü geöffnet werden kann")
    void openMenuCommandOpensMenu() {
        InputReader reader = provideInput("0\n");
        DummyMenu menu = new DummyMenu("TestMenu", reader);
        OpenMenuCommand command = new OpenMenuCommand(menu);
        command.execute();
        assertTrue(menu.opened, "Das Menü sollte geöffnet worden sein.");
    }

    @Test
    @DisplayName("Testet, ob ShowListCommand eine Liste ausgibt")
    void showListCommandPrintsList() {
        InputReader reader = provideInput("0\n");

        ComponentRepository<Component> dummyRepo = new ComponentRepository<Component>() {
            @Override
            public List<Component> findAll() {
                return Collections.emptyList();
            }
        };

        rListConfiguration<Component> config = new rListConfiguration<>(
            "Test-Komponenten",
            new String[] { "Header1", "Header2" },
            dummyRepo,
            c -> new String[] { "dummy" }
        );

        ShowListCommand<Component> command = new ShowListCommand<>(config, reader);
        command.render("Simulation");

        String output = outContent.toString();
        assertTrue(output.contains("Test-Komponenten"), "Die Komponententitel sollten ausgegeben werden.");
    }

    @Test
    @DisplayName("Testet, ob BackCommand ausführbar ist")
    void backCommandExecutes() {
        final boolean[] wasRun = { false }; 
        Runnable onBack = () -> wasRun[0] = true;

        BackCommand backCommand = new BackCommand(onBack);
        backCommand.execute();

        assertTrue(wasRun[0], "Das Runnable sollte ausgeführt worden sein.");
    }

    @Test
    @DisplayName("Testet, ob ExitCommand das Runnable ausführt und 'Auf Wiedersehen!' ausgibt")
    void exitCommandExecutes() {
        final boolean[] wasRun = { false };
        Runnable onExit = () -> wasRun[0] = true;

        ExitCommand exitCommand = new ExitCommand(onExit);
        
        try {
            exitCommand.execute();
        } catch (SecurityException e) {
        }

        String output = outContent.toString();

        assertTrue(wasRun[0], "Das Runnable sollte ausgeführt worden sein.");
        assertTrue(output.contains("Auf Wiedersehen!"), "Die Abschiedsnachricht sollte ausgegeben werden.");
    }

    @Test
    public void testLoginCommandSuccess() {
        DummyUserRepository repo = new DummyUserRepository();
        repo.save("TestUser");

        // 1. "TestUser" für readString
        // 2. "\n" (Enter) für waitForEnter
        InputReader reader = provideInput("TestUser\n\n");

        LoginCommand cmd = new LoginCommand(reader, repo);
        cmd.execute();

        assertTrue(SessionManager.isLoggedIn(), "Nutzer sollte nach korrektem Login eingeloggt sein.");
        assertEquals("TestUser", SessionManager.getcurrentUser().getName());
    }

    @Test
    public void testLoginCommandFailure() {
        DummyUserRepository repo = new DummyUserRepository();

        // 1. "FalscherNutzer" für readString
        // 2. "\n" für waitForEnter
        InputReader reader = provideInput("FalscherNutzer\n\n");

        LoginCommand cmd = new LoginCommand(reader, repo);
        cmd.execute();

        assertFalse(SessionManager.isLoggedIn(), "Nutzer sollte bei falschem Namen NICHT eingeloggt sein.");
    }

    @Test
    public void testLogoutCommand() {
        SessionManager.setCurrentUser(new User(1, "TestUser"));
        assertTrue(SessionManager.isLoggedIn());

        LogoutCommand cmd = new LogoutCommand();
        cmd.execute();

        assertFalse(SessionManager.isLoggedIn(), "Nach dem Logout sollte SessionManager.isLoggedIn() false sein.");
    }

    @Test
    public void testNewUserCommandCreatesUser() {
        DummyUserRepository repo = new DummyUserRepository();

        // 1. "NewTestUser" für readString
        // 2. "\n" für waitForEnter
        InputReader reader = provideInput("NewTestUser\n\n");

        NewUserCommand cmd = new NewUserCommand(reader, repo);
        cmd.execute();

        assertEquals(1, repo.findAll().size(), "Ein neuer Nutzer sollte im Repo angelegt sein.");
        assertEquals("NewTestUser", repo.findAll().get(0).getName());
    }

   @Test
    public void testSaveDraftNotLoggedIn() {
        InputReader reader = provideInput("\n");
        ComputerDraft draft = new ComputerDraft();
        draft.startNewDraft();
        
        draft.setCpu(new CPU(1, "Test", 10, null, null, 1, false, 10));
        
        SaveDraftCommand cmd = new SaveDraftCommand(reader, new DummyComputerRepository(), draft);
        cmd.execute();

        assertTrue(draft.hasUnsavedChanges(), "Entwurf sollte weiterhin ungespeicherte Änderungen haben, da der User nicht eingeloggt war.");
    }

    @Test
    public void testSaveDraftLoggedIn() {
        InputReader reader = provideInput("\n");
        
        ComputerDraft draft = new ComputerDraft();
        draft.startNewDraft();
        draft.setCpu(new CPU(1, "Test", 10, null, null, 1, false, 10)); 

        SessionManager.setCurrentUser(new User(1, "TestUser"));
        
        SaveDraftCommand cmd = new SaveDraftCommand(reader, new DummyComputerRepository(), draft);
        cmd.execute();

        assertFalse(draft.hasUnsavedChanges(), "Entwurf sollte nach dem Speichern keine ungespeicherten Änderungen mehr haben.");
    }

    @Test
    public void testExecutePrintsDraft() {
        AppContext context = new AppContext();
        context.computerDraft.startNewDraft();
        
        Manufacturer m = new Manufacturer(1, "Intel");
        Socket s = new Socket(1, "LGA1700");
        context.computerDraft.setCpu(new CPU(1, "i5", 200, m, s, 3.0, 6, 4.0, true, 65));
        
        try {
            java.lang.reflect.Field readerField = AppContext.class.getDeclaredField("inputReader");
            readerField.setAccessible(true);
            readerField.set(context, provideInput("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ShowCurrentDraftCommand cmd = new ShowCurrentDraftCommand(context);
        cmd.execute(); 

        String output = outContent.toString();
        
        assertTrue(output.contains("AKTUELLER PC-ENTWURF"), "Der Titel des Menüs sollte gedruckt werden.");
        assertTrue(output.contains("i5"), "Die Tabelle sollte den Namen der CPU enthalten.");
        assertTrue(output.contains("Kerne"), "Die Tabelle sollte die Eigenschaft 'Kerne' anzeigen.");
        assertTrue(output.contains("6"), "Die Tabelle sollte die korrekte Anzahl an Kernen anzeigen.");
    }

    @AfterAll
    public static void end() {
        System.out.println("Command Testing ended."); 
    }
}