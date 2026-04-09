package de.ase.pcpartpicker.adapters.cli;

import java.util.function.Supplier;

import de.ase.pcpartpicker.adapters.cli.commands.ICommand;
/**
 * Klasse, die definiert welche Komponenten ein Menüeintrag hat
 * @param title Name des Menüeintrages
 * @param command Speichert die Aktion die Ausgeführt werden soll
 * @author Henri
 */
public class MenuItem implements IMenuComponent {
    private final Supplier<String> titleSupplier;
    private final ICommand command;
    
    public MenuItem(String title, ICommand command) {
        this.titleSupplier = () -> title;
        this.command = command;
    }

    // Konstruktor für dynamische Titel 
    public MenuItem(Supplier<String> titleSupplier, ICommand command) {
        this.titleSupplier = titleSupplier;
        this.command = command; 
    }
    
    @Override
    public void execute() {
        command.execute();
    }
    
    @Override
    public String getTitle() {
        return titleSupplier.get();
    }
}