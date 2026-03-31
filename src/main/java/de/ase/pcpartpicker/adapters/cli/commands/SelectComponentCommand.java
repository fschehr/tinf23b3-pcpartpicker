package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.UIUtils;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
import de.ase.pcpartpicker.domain.Component;

import de.ase.pcpartpicker.ColorConstants;

public class SelectComponentCommand<T extends Component> implements ICommand {
    private static final int PAGE_SIZE = 10;
    
    private final InputReader inputReader;
    private final Repository<T> repository; 
    private final ComputerDraft draft;
    
    private final String componentName;
    private final String[] tableHeaders;
    
    private final BiConsumer<ComputerDraft, T> draftSetter; 
    private final Function<T, String[]> rowMapper; 

    public SelectComponentCommand(
            InputReader inputReader, 
            ComputerDraft draft,
            rListConfiguration<T> config, 
            BiConsumer<ComputerDraft, T> draftSetter) {
        
        this.draft = draft;
        this.inputReader = inputReader;
        this.repository = config.repository();
        this.componentName = config.title();
        this.tableHeaders = config.headers();
        this.draftSetter = draftSetter;
        this.rowMapper = config.rowMapper();
    }

    @Override
    public void execute() {
        List<T> items = repository.findAll();

        if (items.isEmpty()) {
            System.out.println("\nKeine Komponenten gefunden.");
            inputReader.waitForEnter("Enter drücken...");
            return;
        }

        int currentPage = 0;
        int totalPages = (items.size() + PAGE_SIZE - 1) / PAGE_SIZE;

        while (true) {
            UIUtils.clear();
            System.out.println("\n--- " + componentName + " Auswählen ---");

            int startIndex = currentPage * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, items.size());


           TableGenerator table = new TableGenerator(tableHeaders);
            for (int i = startIndex; i < endIndex; i++) {
                table.addRow(rowMapper.apply(items.get(i)));
            }
            table.printTable();
            System.out.println("\nSeite " + (currentPage + 1) + " von " + totalPages
                + " | m = nächste Seite | n = vorherige Seite | 0 = zurück");

            String input = inputReader.readString("ID oder Aktion (m/n/0)").trim().toLowerCase();

            if ("0".equals(input)) {
                System.out.println("-> Auswahl abgebrochen.");
                return;
            }

            if ("m".equals(input)) {
                if (currentPage < totalPages - 1) currentPage++;
                continue;
            }

            if ("n".equals(input)) {
                if (currentPage > 0) currentPage--;
                continue;
            }

            int selectedId;
            try {
                selectedId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe.");
                continue;
            }

            T selectedItem = null;
            for (T item : items) {
                if (item.getId() == selectedId) { // Direkt item.getId() aufrufen!
                    selectedItem = item;
                    break;
                }
            }

            if (selectedItem == null) {
                System.out.println(ColorConstants.RED("FEHLER") + " | ID nicht gefunden.");
                continue;
            }

            String warning = draft.getBuilder().validate(draft, selectedItem);
            if (warning != null) {
                System.out.println(ColorConstants.YELLOW("WARNUNG") + " | " + warning);
                int add = inputReader.readInt("Willst du die Komponente wirklich hinzufügen? [0: Nein, 1: Ja]", 0, 1);
                if (add == 0) return;
            }

            draftSetter.accept(draft, selectedItem);
            System.out.println("\n" + ColorConstants.GREEN("ERFOLG") + " | " + selectedItem.getName() + " wurde zum Computer hinzugefügt!");
            inputReader.waitForEnter("Enter drücken um zurück zum Konfigurator zu gelangen");
            return;
        }
    }
}