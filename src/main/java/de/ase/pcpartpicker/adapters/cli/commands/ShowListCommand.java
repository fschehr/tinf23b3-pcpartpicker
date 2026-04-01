package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;

import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.PagingInput;

public class ShowListCommand<T> implements ICommand {
    private static final int PAGE_SIZE = 10;

    private final rListConfiguration<T> config;
    private final InputReader inputReader;

    public ShowListCommand(rListConfiguration<T> config, InputReader inputReader) {
        this.config = config;
        this.inputReader = inputReader;
    }

    @Override
    public void execute() {
        List<T> items = config.repository().findAll();

        if (items.isEmpty()) {
            NavigationUtils.clear();
            System.out.println("\n=== " + config.title() + " ===\n");
            System.out.println("Keine Einträge gefunden.");
            inputReader.waitForEnter("\nEnter drücken um zurückzukehren.");
            return;
        }

        int currentPage = 0;
        int totalPages = (items.size() + PAGE_SIZE - 1) / PAGE_SIZE;

        while (true) {
            NavigationUtils.clear();
            System.out.println("\n=== " + config.title() + " ===\n");

            int startIndex = currentPage * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, items.size());

            TableGenerator table = new TableGenerator(config.headers());
            for (int i = startIndex; i < endIndex; i++) {
                table.addRow(config.rowMapper().apply(items.get(i)));
            }
            table.printTable();

            System.out.println("\n" + PagingInput.helpText(currentPage, totalPages, false));
            String input = inputReader.readString(PagingInput.promptText(false)).trim().toLowerCase();
            PagingInput.Action action = PagingInput.parse(input, false);

            if (action == PagingInput.Action.BACK) {
                return;
            }

            if (action != PagingInput.Action.OTHER) {
                currentPage = PagingInput.movePage(currentPage, totalPages, action);
            }

        }
    }
}