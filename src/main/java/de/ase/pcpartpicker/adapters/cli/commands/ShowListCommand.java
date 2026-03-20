package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.UIUtils;
import de.ase.pcpartpicker.adapters.cli.rComponentConfig;

public class ShowListCommand<T> implements ICommand {
    private final rComponentConfig<T> config;
    private final InputReader inputReader;

    public ShowListCommand(rComponentConfig<T> config, InputReader inputReader) {
        this.config = config;
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        UIUtils.clear();
        System.out.println("\n=== " + config.title() + " ===\n");

        List<T> items = config.repository().findAll();

        if (items.isEmpty()) {
            System.out.println("Keine Einträge gefunden.");
        } else {
            TableGenerator table = new TableGenerator(config.headers());
            for (T item : items) {
                table.addRow(config.rowMapper().apply(item));
            }
            table.printTable();
        }

        inputReader.waitForEnter("\nEnter drücken um zurückzukehren.");
    }
}