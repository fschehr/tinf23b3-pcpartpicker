package de.ase.pcpartpicker.adapters.cli;

import java.util.LinkedHashMap;
import java.util.Map;

import de.ase.pcpartpicker.adapters.sqlite.repositories.BaseRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.CpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.GpuRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.MainboardRepository;
import de.ase.pcpartpicker.adapters.sqlite.repositories.RamRepository;
import de.ase.pcpartpicker.domain.Component;

public class ComponentSelectionMenu extends BaseMenu{
    
    private final InputReader reader;
    private final Map<Integer, ListMenu<?>> menus = new LinkedHashMap<>();

    public ComponentSelectionMenu(
        InputReader reader,
        CpuRepository cpuRepository,
        GpuRepository gpuRepository,
        RamRepository ramRepository,
        MainboardRepository mainboardRepository
    ) {
        this.reader = reader;
        addMenu(1, reader, cpuRepository, "CPU auswählen");
        addMenu(2, reader, gpuRepository, "GPU auswählen");
        addMenu(3, reader, ramRepository, "RAM auswählen");
        addMenu(4, reader, mainboardRepository, "Mainboard auswählen");
        
    }
    
    private <T extends Component> void addMenu(int key, InputReader reader, BaseRepository<T> repository, String title) {
        menus.put(key, new ListMenu<>(reader, repository, title));
    }

    @Override
    public void run() {

        System.out.println("\n--- Komponenten Auswahl ---");
        menus.forEach((key, menu) -> System.out.println(key + ". " + menu.getTitle()));
        System.out.println("0. Zurück zum Hauptmenü");
    
        int choice = reader.readInt("Wählen Sie eine Kategorie", 0, 4);

        if (choice == 0) {
            stop();
        } else {
            menus.get(choice).start();
        }
        
    }



  
}
