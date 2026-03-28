package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.sqlite.DatabaseInitializer;

/**
 * Setzt die Datenbank auf Basis der JSONL-Dateien im data-Ordner zurueck.
 */
public class ResetDatabaseCommand implements ICommand {

    private final InputReader inputReader;
    private final DatabaseInitializer databaseInitializer;

    public ResetDatabaseCommand(InputReader inputReader, DatabaseInitializer databaseInitializer) {
        this.inputReader = inputReader;
        this.databaseInitializer = databaseInitializer;
    }

    @Override
    public void execute() {
        System.out.println("WARNUNG: Alle vorhandenen Daten in der Datenbank werden geloescht.");
        int confirmation = inputReader.readInt("Datenbank wirklich resetten? 1=Ja, 2=Nein", 1, 2);

        if (confirmation != 1) {
            System.out.println("Datenbank-Reset abgebrochen.");
            inputReader.waitForEnter("Druecke Enter zum Fortfahren...");
            return;
        }

        try {
            System.out.println("Reset beginnt. Dies dauert ungefähr eine Minute...");
            databaseInitializer.resetDatabase();
            System.out.println("Datenbank erfolgreich aus JSONL-Dateien neu aufgebaut.");
        } catch (IllegalStateException e) {
            System.out.println("Fehler beim Datenbank-Reset: " + e.getMessage());
        }

        inputReader.waitForEnter("Druecke Enter zum Fortfahren...");
    }
}
