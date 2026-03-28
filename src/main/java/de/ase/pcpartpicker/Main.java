package de.ase.pcpartpicker;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// import de.ase.pcpartpicker.adapters.cli.MenuFactory;
import de.ase.pcpartpicker.adapters.cli.commands.StartCommand;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;


public class Main {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);

        databaseInitializer.initialize();


        try {
            // Zwingt die Windows-Konsole auf UTF-8
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "chcp 65001").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Ignorieren, falls es nicht klappt
        }

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        new StartCommand().execute();
        
    }
}