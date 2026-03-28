package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.InputReader;
import de.ase.pcpartpicker.adapters.cli.TableGenerator;
import de.ase.pcpartpicker.adapters.cli.rListConfiguration;
import de.ase.pcpartpicker.adapters.sqlite.repositories.Repository;
import de.ase.pcpartpicker.domain.Component;

public class SelectComponentCommand<T extends Component> implements ICommand {
    
    private final InputReader inputReader;
    private final Repository<T> repository; 
    private final ComputerDraft draft;
    
    private final String componentName;
    private final String[] tableHeaders;
    
    private final Function<T, String[]> rowMapper; 
    private final Function<T, Integer> idExtractor; 
    private final Function<T, String> nameExtractor; 
    private final BiConsumer<ComputerDraft, T> draftSetter; 

    public SelectComponentCommand(
            InputReader inputReader, 
            ComputerDraft draft,
            rListConfiguration<T> config, 
            Function<T, Integer> idExtractor,
            Function<T, String> nameExtractor,
            BiConsumer<ComputerDraft, T> draftSetter) {
        
        this.draft = draft;
        this.inputReader = inputReader;
        this.repository = config.repository();
        this.componentName = config.title();
        this.tableHeaders = config.headers();
        this.rowMapper = config.rowMapper();
        this.draftSetter = draftSetter;
        this.idExtractor = idExtractor;
        this.nameExtractor = nameExtractor;
    }

    @Override
    public void execute() {
        System.out.println("\n--- " + componentName + " Auswählen ---");
        
        List<T> items = repository.findAll(); 
        
        TableGenerator table = new TableGenerator(tableHeaders);
        for (T item : items) {
            table.addRow(rowMapper.apply(item));
        }
        table.printTable();

        System.out.println("\n[Tipp: Gib 0 ein, um die Auswahl abzubrechen und zurückzukehren]");

        int selectedId = inputReader.readInt("Bitte die ID der gewünschten Komponente eingeben", 0, items.size());

        if (selectedId == 0) {
            System.out.println("-> Auswahl abgebrochen.");
            return; 
        }
        
        for (T item : items) {
            if (idExtractor.apply(item) == selectedId) {
    
                String warning = draft.getBuilder().validate(draft, item); 
                if (warning != null) {
                    System.out.println("\nAchtung: " + warning); 
                    int add = inputReader.readInt("Willst du die Komponente wirklich hinzufügen? [0: Nein, 1: Ja]", 0, 1);
                    if(add == 0) {
                        return; 
                    }
                }
                draftSetter.accept(draft, item);
                System.out.println("\n-> " + nameExtractor.apply(item) + " wurde zum Computer hinzugefügt!");
                inputReader.waitForEnter("Enter drücken um zurück zum Konfigurator zu gelangen");
                return;
            }
        }
        System.out.println("Fehler: ID nicht gefunden.");
        inputReader.waitForEnter("Enter drücken...");
    }
}