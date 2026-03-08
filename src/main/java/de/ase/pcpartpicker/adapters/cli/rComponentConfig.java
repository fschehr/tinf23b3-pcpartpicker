package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.repositories.ComponentRepository;
import de.ase.pcpartpicker.domain.Component;
import java.util.function.Function;

public record rComponentConfig<T extends Component>(
    String title,
    String[] headers,
    ComponentRepository<T> repository,
    Function<T, String[]> rowMapper
) {}