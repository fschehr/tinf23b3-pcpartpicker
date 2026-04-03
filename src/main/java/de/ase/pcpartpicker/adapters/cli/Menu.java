package de.ase.pcpartpicker.adapters.cli;

import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;
/**
 * Klasse, die den grundsätzlichen Aufbau eines Menüs definiert
 * @param title Titel des Menüs
 * @param children Enthält Untermenüs, die im aktuellen Menü angezeigt werden können
 * @param inputReader liest die Benutzereingaben von der Konsole 
 * @param running Flag das angibt, ob das Menü gerade aktiv ist 
 * @author Henri
*/
public class Menu implements IMenuComponent {
    public enum NavMode {STANDARD, PAGING}; 

    private final String title;
    private final List<IMenuComponent> children = new ArrayList<>();
    private IMenuComponent zeroComponent; 
    private final InputReader inputReader;
    private boolean running = true;
    private Renderable customContent;
    private final NavMode nav; 


    
    public Menu(String title, InputReader inputReader, NavMode nav) {
        this.title = title;
        this.inputReader = inputReader;
        this.nav = nav;
    }

    public Menu(String title, InputReader inputReader) {
        // setze den Standard naviagtionsmodus
        this(title, inputReader, NavMode.STANDARD);
    }


    /**
     * Setzt beliebigen renderbaren Inhalt, der im Menü angezeigt werden soll
     */
    public void setCustomContent(Renderable content) {
        this.customContent = content;
    }

    /**
     * Gibt beliebigen renderbaren Inhalt direkt im Menü aus
     */
    public void renderContent(Renderable content) {
        if (content != null) {
            content.render();
        }
    }
    
    public void add(IMenuComponent component) {
        children.add(component);
    }

    public void setZeroComponent(IMenuComponent component) {
        this.zeroComponent = component;
    }

    public void setRunning(boolean running){
        this.running = running;
    }
    
    @Override
    public void execute() {
        running = true;
        while (running) {
            
            
            if(nav == NavMode.PAGING && customContent != null) {
                customContent.render();
                running=false;
                return;
            }

            NavigationUtils.clear();
            System.out.println("\n=== " + title + " ===\n");
            
            if(SessionManager.isLoggedIn()) {
                System.out.println("Eingeloggt als: "+ SessionManager.getcurrentUser().getName()+ "\n");
            }

            if(customContent != null) {
                customContent.render();
            }

            for (int i = 0; i < children.size(); i++) {
                System.out.println((i + 1) + ") " + children.get(i).getTitle());
            }

            if (zeroComponent != null) {
                System.out.println("0) " + zeroComponent.getTitle());
            }
            
                
            System.out.print("\nAuswahl: ");
            
            int choice = inputReader.readInt("Eine Zahl eingeben", 0, children.size());
            
        
            if (choice == 0 && zeroComponent != null) {
                zeroComponent.execute();
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