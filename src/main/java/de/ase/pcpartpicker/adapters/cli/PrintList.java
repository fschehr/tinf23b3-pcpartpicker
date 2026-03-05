package de.ase.pcpartpicker.adapters.cli;

import java.util.List;
import java.util.Locale;

import de.ase.pcpartpicker.adapters.sqlite.repositories.BaseRepository;
import de.ase.pcpartpicker.domain.Component;

public class PrintList {
    
    public <T extends Component> void print(BaseRepository<T> repository, String title) {

        List<T> components = repository.findAll(); 
        if(components.isEmpty()) {
            System.err.println("\nKeine Komponenten in der Datenbank gefunden.");
        }

        TableGenerator table = new TableGenerator("ID", "Name", "Hersteller", "Preis"); 

        for(T component: components) {
            table.addRow(
                String.valueOf(component.getId()),
                component.getName(),
                component.getManufacturer().getName(),
                String.format(Locale.GERMANY, "%.2f€", component.getPrice())
            );
        }

        System.out.println("\n--- " + title + " ---");
        table.printTable();
    
    }


}
