package de.ase.pcpartpicker.adapters.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.ase.pcpartpicker.adapters.cli.commands.BackCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ExitCommand;
import de.ase.pcpartpicker.adapters.cli.commands.OpenMenuCommand;
import de.ase.pcpartpicker.adapters.cli.commands.ShowListCommand;
import de.ase.pcpartpicker.adapters.cli.commands.StartCommand;
import de.ase.pcpartpicker.adapters.sqlite.repositories.ComponentRepository;
import de.ase.pcpartpicker.domain.Component;

class DummyMenu extends Menu {
    boolean opened = false;
    public DummyMenu(String title, InputReader reader) {
        super(title, reader);
    }
    @Override
    public void execute() {
        opened = true;
    }
}


public class CommandTest {

    @BeforeAll
    public static void setup() {
        System.out.println("Starting Command Tests...");
    }

    @Test
    @DisplayName("Testet, ob StartCommand das Hauptmenü startet und ausgibt")
    void startCommandExecutesMenu() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Simuliere Eingabe für das Menü 
        String simulatedInput = "";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        StartCommand startCommand = new StartCommand();

        try {
            startCommand.execute();
        } catch (java.util.NoSuchElementException e) {

        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();
        assertTrue(output.contains("PC Part Picker - Hauptmenü"), "Das Hauptmenü sollte ausgegeben werden.");
    }



    @Test
    @DisplayName("Testet, ob ein Menü geöffnet werden kann")
    void openMenuCommandOpensMenu() {
        DummyMenu menu = new DummyMenu("TestMenu", new InputReader());
        OpenMenuCommand command = new OpenMenuCommand(menu);
        command.execute();
        assertTrue(menu.opened, "Das Menü sollte geöffnet worden sein.");
    }


    @Test
    @DisplayName("Testet, ob ShowListCommand eine Liste ausgibt")
    void showListCommandPrintsList() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String simulatedInput = "0\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ComponentRepository<Component> dummyRepo = new ComponentRepository<Component>() {
            @Override
            public java.util.List<Component> findAll() {
                return java.util.Collections.emptyList();
            }
        };

        rListConfiguration<Component> config = new rListConfiguration<>(
            "Test-Komponenten",
            new String[] { "Header1", "Header2" },
            dummyRepo,
            c -> new String[] { "dummy" }
        );

        InputReader reader = new InputReader();
        ShowListCommand<Component> command = new ShowListCommand<>(config, reader);
        command.render("Simulation");

        System.setOut(originalOut);
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

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        ExitCommand exitCommand = new ExitCommand(onExit);
        exitCommand.execute();

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue(wasRun[0], "Das Runnable sollte ausgeführt worden sein.");
        assertTrue(output.contains("Auf Wiedersehen!"), "Die Abschiedsnachricht sollte ausgegeben werden.");
    }






    @AfterAll
    public static void end() {
        System.out.println("Command Testing ended."); 
    }
}
