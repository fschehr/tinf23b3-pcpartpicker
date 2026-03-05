package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.adapters.sqlite.repositories.BaseRepository;
import de.ase.pcpartpicker.domain.Component;

public class ListMenu<T extends Component> extends BaseMenu {

    private final InputReader reader;
    private final PrintList printList = new PrintList();
    private final BaseRepository<T> repository;
    private final String title;
 
    public ListMenu(InputReader reader, BaseRepository<T> repository, String title) {
        this.reader = reader;
        this.repository = repository;
        this.title = title; 
    }

    @Override
    protected void run() {
        printList.print(repository, title);
        reader.waitForEnter("\nDrücken Sie Enter, um zurückzukehren.");
        stop();    
    }

    public String getTitle() {
        return title;
    }

}
