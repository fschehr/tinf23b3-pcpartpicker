package de.ase.pcpartpicker;
// import de.ase.pcpartpicker.adapters.cli.MenuFactory;
import de.ase.pcpartpicker.adapters.cli.commands.StartCommand;
import de.ase.pcpartpicker.adapters.sqlite.ConnectionFactory;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;


public class Main {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connectionFactory);

        databaseInitializer.initialize();
        new StartCommand().execute();
        
    }
}