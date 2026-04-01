package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
import de.ase.pcpartpicker.adapters.cli.utils.PagingInput;
import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
import de.ase.pcpartpicker.domain.*;

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
            NavigationUtils.clear();
            System.out.println("\n--- " + componentName + " Auswählen ---");

            int startIndex = currentPage * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, items.size());


            TableGenerator table = new TableGenerator(tableHeaders);
            for (int i = startIndex; i < endIndex; i++) {
                T item = items.get(i);

                boolean isSelected = isComponentSelected(item);

                if (isSelected) {
                    table.addColoredRow(ColorConstants.ANSI_GREEN, rowMapper.apply(item));
                } else {
                    table.addRow(rowMapper.apply(item));
                }
            }
            table.printTable();
            System.out.println("\nSeite " + (currentPage + 1) + " von " + totalPages
                + " | m = nächste Seite | n = vorherige Seite | 0 = zurück");

            String input = inputReader.readString("ID oder Aktion (m/n/0)").trim().toLowerCase();

            PagingInput.Action action = PagingInput.parse(input);

            if (action == PagingInput.Action.BACK) {
                System.out.println("-> Auswahl abgebrochen.");
                return;
            }

            if (action != PagingInput.Action.OTHER) {
                currentPage = PagingInput.movePage(currentPage, totalPages, action);
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

    private boolean isComponentSelected(T item) {
        if (item instanceof CPU) {
            return draft.getCPU() != null && draft.getCPU().getId() == item.getId();
        }
        if (item instanceof GPU) {
            return draft.getGPU() != null && draft.getGPU().getId() == item.getId();
        }
        if (item instanceof Mainboard) {
            return draft.getMainboard() != null && draft.getMainboard().getId() == item.getId();
        }
        if (item instanceof RAM) {
            return draft.getRAM() != null && draft.getRAM().getId() == item.getId();
        }
        if (item instanceof PSU) {
            return draft.getPSU() != null && draft.getPSU().getId() == item.getId();
        }
        if (item instanceof Case) {
            return draft.getComputerCase() != null && draft.getComputerCase().getId() == item.getId();
        }
        if (item instanceof Storage) {
            if (draft.getStorage() == null || draft.getStorage().isEmpty()) {
                return false;
            }
            for (Storage storage : draft.getStorage()) {
                if (storage.getId() == item.getId()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}