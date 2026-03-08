package de.ase.pcpartpicker.adapters.cli;

import java.util.ArrayList;
import java.util.List;

public class Menu implements IMenuComponent {
    private final String title;
    private final List<IMenuComponent> children = new ArrayList<>();
    private final InputReader inputReader;
    private boolean running = true;
    
    public Menu(String title, InputReader inputReader) {
        this.title = title;
        this.inputReader = inputReader;
    }
    
    public void add(IMenuComponent component) {
        children.add(component);
    }
    
    @Override
    public void execute() {
        running = true;
        while (running) {
            UIUtils.clear();
            System.out.println("\n=== " + title + " ===\n");
            
            for (int i = 0; i < children.size(); i++) {
                System.out.println((i + 1) + ") " + children.get(i).getTitle());
            }
            System.out.println("0) Beenden"); 
            System.out.print("\nAuswahl: ");
            
            int choice = inputReader.readInt("Ein Zahl eingeben", 0, children.size());
            
            if (choice == 0) {
                running = false;
            } else if (choice > 0 && choice <= children.size()) {
                children.get(choice - 1).execute();
            }
        }
    }
    
    @Override
    public String getTitle() {
        return title;
    }
}