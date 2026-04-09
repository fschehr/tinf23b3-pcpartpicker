package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import de.ase.pcpartpicker.ColorConstants;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.adapters.cli.utils.Paging;
import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;

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
            ExceptionUtils.printInfo("\nKeine Komponenten gefunden.");
            inputReader.waitForEnter("Enter drücken...");
            return;
        }


        Paging.builder(items)
            .withTitle(componentName)
            .withPageSize(PAGE_SIZE)
            .withInputReader(() -> inputReader.readString("").trim().toLowerCase())
            .withRenderer((item, currentPage) -> {
                int startIndex = currentPage * PAGE_SIZE;
                int endIndex = Math.min(startIndex + PAGE_SIZE, items.size()); 

                TableGenerator table = new TableGenerator(tableHeaders);
                for (int i = startIndex; i < endIndex; i++) {
                    item = items.get(i);
                    boolean isSelected = isComponentSelected(item);
                    if (isSelected) {
                        table.addColoredRow(ColorConstants.ANSI_GREEN, rowMapper.apply(item));
                    } else {
                        table.addRow(rowMapper.apply(item));
                    }
                }
                table.printTable();

            })
            .onClear(() ->  {
                boolean cleared = clearCurrentCategory(items); // deine Clear-Logik
                if (cleared) {
                    ExceptionUtils.printSuccess("Auswahl wurde entfernt.");
                } else {
                    ExceptionUtils.printWarning("Nichts zum Entfernen ausgewählt.");
                }
                inputReader.waitForEnter("Enter drücken...");
            })
            .onOtherInput(input -> {
                try {
                    int selectedId = Integer.parseInt(input);
                    T selectedItem = null;
                    
            
                    for (T item : items) {
                        if (item.getId() == selectedId) { 
                            selectedItem = item;
                            break;
                        }
                    }

                    if (selectedItem == null) {
                        ExceptionUtils.printError("ID nicht gefunden.");
                        return; 
                    }

                
                    String warning = draft.getBuilder().validate(selectedItem);
                    if (warning != null) {
                        ExceptionUtils.printWarning(warning);
                        int add = inputReader.readInt("Willst du die Komponente wirklich hinzufügen? [0: Nein, 1: Ja]", 0, 1);
                        if (add == 0) return; // Zurück in die Paging-Schleife
                    }

                   
                    draftSetter.accept(draft, selectedItem);
                    ExceptionUtils.printSuccess(selectedItem.getName() + " wurde zum Computer hinzugefügt!");
                    inputReader.waitForEnter("Enter drücken...");
                    
                    // TODO: man bleibt im Menü will ich das so?
                    
                } catch (NumberFormatException e) {
                    ExceptionUtils.printError("Ungültige Eingabe.");
                }
            })
            .start();
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
                if (storage.getId() == item.getId() && storage.getClass().equals(item.getClass())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    private boolean clearCurrentCategory(List<T> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }

        T sample = items.get(0);

        if (sample instanceof CPU) {
            if (draft.getCPU() == null) return false;
            draft.setCpu(null);
            return true;
        }

        if (sample instanceof GPU) {
            if (draft.getGPU() == null) return false;
            draft.setGpu(null);
            return true;
        }

        if (sample instanceof Mainboard) {
            if (draft.getMainboard() == null) return false;
            draft.setMainboard(null);
            return true;
        }

        if (sample instanceof RAM) {
            if (draft.getRAM() == null) return false;
            draft.setRam(null, 0);
            return true;
        }

        if (sample instanceof PSU) {
            if (draft.getPSU() == null) return false;
            draft.setPsu(null);
            return true;
        }

        if (sample instanceof Case) {
            if (draft.getComputerCase() == null) return false;
            draft.setComputerCase(null);
            return true;
        }

        if (sample instanceof Storage) {
            if (draft.getStorage() == null || draft.getStorage().isEmpty()) {
                return false;
            }

            List<Storage> updated = new java.util.ArrayList<>(draft.getStorage());
            Class<?> storageType = sample.getClass();

            boolean removed = updated.removeIf(storageType::isInstance);
            if (removed) {
                draft.setStorage(updated);
            }
            return removed;
        }

        return false;
    }


}