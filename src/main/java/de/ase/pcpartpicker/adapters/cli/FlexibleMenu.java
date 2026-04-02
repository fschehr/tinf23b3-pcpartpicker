package de.ase.pcpartpicker.adapters.cli;

import java.util.LinkedHashMap;
import java.util.Map;

import de.ase.pcpartpicker.adapters.cli.utils.NavigationUtils;

public class FlexibleMenu implements IMenuComponent {
    private final String title;
    private final InputReader inputReader;
    private boolean running = true;

    // LinkedHashMap behält die Reihenfolge bei, in der die Elemente hinzugefügt wurden
    private final Map<String, IMenuComponent> items = new LinkedHashMap<>();
    private final Map<String, IMenuComponent> navigationItems = new LinkedHashMap<>();

    // Optionale Render-Funktionen für vollkommen freie Ausgaben
    private Runnable customHeaderRenderer;
    private Runnable customFooterRenderer;

    public FlexibleMenu(String title, InputReader inputReader) {
        this.title = title;
        this.inputReader = inputReader;
    }

    // Fügt normale Menüpunkte hinzu
    public void addItem(String key, IMenuComponent component) {
        items.put(key.toLowerCase(), component);
    }

    // Fügt Punkte zur Navigationsleiste hinzu
    public void addNavigation(String key, IMenuComponent component) {
        navigationItems.put(key.toLowerCase(), component);
    }

    // Erlaubt das Überschreiben der Standard-Kopfzeile
    public void setCustomHeader(Runnable customHeaderRenderer) {
        this.customHeaderRenderer = customHeaderRenderer;
    }

    // Erlaubt das Überschreiben der Standard-Navigationsleiste
    public void setCustomFooter(Runnable customFooterRenderer) {
        this.customFooterRenderer = customFooterRenderer;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void execute() {
        running = true;
        while (running) {
            NavigationUtils.clear();

            // --- 1. HEADER RENDERING ---
            if (customHeaderRenderer != null) {
                customHeaderRenderer.run();
            } else {
                // Standard-Header, falls keiner gesetzt wurde
                System.out.println("\n=== " + title + " ===\n");
                if (SessionManager.isLoggedIn()) {
                    System.out.println("Eingeloggt als: " + SessionManager.getcurrentUser().getName() + "\n");
                }
            }

            // --- 2. ITEMS RENDERING ---
            for (Map.Entry<String, IMenuComponent> entry : items.entrySet()) {
                System.out.println(entry.getKey() + ") " + entry.getValue().getTitle());
            }

            // --- 3. FOOTER / NAVIGATION RENDERING ---
            if (customFooterRenderer != null) {
                customFooterRenderer.run();
            } else {
                // Standard-Footer
                System.out.println("\n--- Navigation ---");
                for (Map.Entry<String, IMenuComponent> entry : navigationItems.entrySet()) {
                    System.out.print("[" + entry.getKey() + "] " + entry.getValue().getTitle() + "   ");
                }
                System.out.println();
            }

            // --- 4. EINGABE VERARBEITEN ---
            String choice = inputReader.readString("\nAuswahl").trim().toLowerCase();

            if (items.containsKey(choice)) {
                items.get(choice).execute();
            } else if (navigationItems.containsKey(choice)) {
                navigationItems.get(choice).execute();
            } else {
                System.out.println("Ungültige Auswahl!");
                inputReader.waitForEnter("Enter drücken...");
            }
        }
    }
}