package de.ase.pcpartpicker.adapters.cli;

/**
 * Interface, das definiert, welche Klassen 
 * ein Menü haben muss
 * @author Henri
 */

public interface IMenuComponent {
    void execute(); 
    String getTitle(); 
    
    // alle Komponenten sind standardmäßig sichtbar
    default boolean isVisible() {
        return true; 
    }
}
