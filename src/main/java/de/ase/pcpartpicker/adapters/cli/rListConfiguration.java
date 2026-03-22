package de.ase.pcpartpicker.adapters.cli;

import java.util.function.Function;

import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
/**
 * Record, der die Koniguration für eine PC-Komponente kapselt 
 * @param title Name der Komponente
 * @param headers Array mit Spaltenüberschriften
 * @param repository Repository für Datenbankabfrage
 * @param rowMapper Wandelt Komponente in Array von Strings um für tabellarische Ansicht
 * @author Henri
 */
public record rListConfiguration<T>(
    String title,
    String[] headers,
    Repository<T> repository,
    Function<T, String[]> rowMapper
) {}