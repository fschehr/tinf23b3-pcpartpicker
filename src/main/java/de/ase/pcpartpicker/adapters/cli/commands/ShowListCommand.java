package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.Paging;

public class ShowListCommand<T> implements Renderable {
    private static final int PAGE_SIZE = 10;

    private final rListConfiguration<T> config;
    private final InputReader inputReader;

    public ShowListCommand(rListConfiguration<T> config, InputReader inputReader) {
        this.config = config;
        this.inputReader = inputReader;
    }


    @Override
    public void render(String title) {
        List<T> items = config.repository().findAll();

        if (items.isEmpty()) {
            NavigationUtils.clear();
            System.out.println("\n=== " + config.title() + " ===\n");
            System.out.println("Keine Einträge gefunden.");
            inputReader.waitForEnter("\nEnter drücken um zurückzukehren.");
            return;
        }

        Paging.builder(items)
            .withTitle(title)
            .withPageSize(PAGE_SIZE)
            .withInputReader(() -> inputReader.readString("").trim().toLowerCase())
            .withRenderer((item, currentPage) -> {
                int startIndex = currentPage * PAGE_SIZE;
                int endIndex = Math.min(startIndex + PAGE_SIZE, items.size());

                TableGenerator table = new TableGenerator(config.headers());
                for (int i = startIndex; i < endIndex; i++) {
                    table.addRow(config.rowMapper().apply(items.get(i)));
                }
                table.printTable();
            })
            .start();
    }
}