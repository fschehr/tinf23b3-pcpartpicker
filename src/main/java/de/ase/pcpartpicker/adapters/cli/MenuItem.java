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
    private Supplier<Boolean> visibilitySupplier =  () -> true; 
    
    public MenuItem(String title, ICommand command) {
        this.titleSupplier = () -> title;
        this.command = command;
    }

    // Konstruktor für dynamische Titel 
    public MenuItem(Supplier<String> titleSupplier, ICommand command) {
        this.titleSupplier = titleSupplier;
        this.command = command; 
    }

    public MenuItem(String title, ICommand command, Supplier<Boolean> visibilitySupplier) {
        this.titleSupplier =  () -> title;
        this.command = command; 
        this.visibilitySupplier = visibilitySupplier; 
    }
    
    @Override
    public boolean isVisible() {
        return visibilitySupplier.get();
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