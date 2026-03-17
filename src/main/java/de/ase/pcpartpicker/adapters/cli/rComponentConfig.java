package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.repositories.ComponentRepository;
import de.ase.pcpartpicker.domain.Component;
import java.util.function.Function;
/**
 * Record, der die Koniguration für eine PC-Komponente kapselt 
 * @param title Name der Komponente
 * @param headers Array mit Spaltenüberschriften
 * @param repository Repository für Datenbankabfrage
 * @param rowMapper Wandelt Komponente in Array von Strings um für tabellarische Ansicht
 * @author Henri
 */
public record rComponentConfig<T extends Component>(
    String title,
    String[] headers,
    ComponentRepository<T> repository,
    Function<T, String[]> rowMapper
) {}