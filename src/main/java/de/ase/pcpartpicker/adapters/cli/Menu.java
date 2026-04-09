package de.ase.pcpartpicker.adapters.cli;

import java.util.ArrayList;
import java.util.List;

import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
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
    private String infoMessage; 
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
    
    public void setInfoMessage(String message) {
        this.infoMessage = message; 
    }
    /**
     * Gibt beliebigen renderbaren Inhalt direkt im Menü aus
     */
    public void renderContent(Renderable content) {
        if (content != null) {
            content.render(title);
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
                customContent.render(title);
                running=false;
                return;
            }

            NavigationUtils.clear();
            System.out.println("\n=== " + title + " ===\n");
            
            if(SessionManager.isLoggedIn()) {
                System.out.println("Eingeloggt als: "+ SessionManager.getcurrentUser().getName()+ "\n");
            }

            if(infoMessage != null && !infoMessage.isEmpty()) {
                ExceptionUtils.printInfo(infoMessage);
            }

            if(customContent != null) {
                customContent.render(title);
            }

            List<IMenuComponent> visibleChildren = new ArrayList<>(); 
            for(IMenuComponent child: children) {
                if(child.isVisible()) {
                    visibleChildren.add(child); 
                }
            }

            for (int i = 0; i < visibleChildren.size(); i++) {
                System.out.println((i + 1) + ") " + visibleChildren.get(i).getTitle());
            }

            if (zeroComponent != null) {
                System.out.println("0) " + zeroComponent.getTitle());
            }
            
                
            System.out.print("\nAuswahl: ");
            
            int choice = inputReader.readInt("Eine Zahl eingeben", 0, visibleChildren.size());
            
        
            if (choice == 0 && zeroComponent != null) {
                zeroComponent.execute();
            } else if (choice > 0 && choice <= visibleChildren.size()) {
                visibleChildren.get(choice - 1).execute();
            }
            
        }
    }
    
    @Override
    public String getTitle() {
        return title; 
    }

}