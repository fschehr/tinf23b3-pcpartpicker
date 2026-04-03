package de.ase.pcpartpicker.adapters.cli.utils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Paging {
    public enum Action {
        NEXT, PREVIOUS, BACK, CLEAR, EDIT, OTHER
    }

    private Paging() {}

    public static Action parse(String raw, boolean allowClear) {
        if (raw == null) return Action.OTHER;
        String input = raw.trim().toLowerCase();

        return switch (input) {
            case "m" -> Action.NEXT;
            case "n" -> Action.PREVIOUS;
            case "0" -> Action.BACK;
            case "c" -> allowClear ? Action.CLEAR : Action.OTHER;
            case "e" -> Action.EDIT;
            default -> Action.OTHER;
        };
    }

    public static int movePage(int currentPage, int totalPages, Action action) {
        if (totalPages <= 0) return 0;

        return switch (action) {
            case NEXT -> Math.min(currentPage + 1, totalPages - 1);
            case PREVIOUS -> Math.max(currentPage - 1, 0);
            default -> currentPage;
        };
    }

    public static String helpText(int currentPage, int totalPages, boolean allowClear, boolean alloEdit) {
        StringBuilder text = new StringBuilder("Seite " + (currentPage + 1) + " von " + totalPages
            + " | m = nächste Seite | n = vorherige Seite"); 

        if(allowClear) text.append(" | c = Auswahl löschen");
        if(alloEdit) text.append (" | e = bearbeiten"); 
        text.append(" | 0 = zurück");
        return text.toString();
    }

    public static String promptText(boolean allowClear) {
        return allowClear ? "ID oder Aktion (m/n/c/0)" : "Aktion (m/n/0)";
    }

    
    public static <T> void pageThroughList(
        List<T> items,
        BiConsumer<T, Integer> renderPage,
        Supplier<String> readInput,
        boolean allowClear,
        int pageSize,
        Function<T, List<T>> onEdit
    ) {
        if (items == null || items.isEmpty()) {
            System.out.println("Keine Einträge vorhanden.");
            return;
        }
        int currentPage = 0;
        int totalPages = (pageSize > 0) 
            ? (items.size() + pageSize -1) / pageSize
            :1;

        boolean allowEdit = (onEdit != null); 

        while (true) {
            NavigationUtils.clear();
            renderPage.accept(items.get(currentPage), currentPage);

            System.out.println(Paging.helpText(currentPage, totalPages, allowClear, allowEdit));

            // Eingabe lesen
            String input = readInput.get();
            Action action = Paging.parse(input, allowClear);

            if (action == Action.BACK) return;
            if (action == Action.CLEAR && allowClear) {
                // Optional: Clear-Logik hier einbauen
                continue;
            }
            if (action == Action.EDIT) {
                if(onEdit != null) {
                    List<T> updateItems = onEdit.apply(items.get(currentPage)); 
                    
                    if(updateItems != null) {
                        items = updateItems;
                        if(items.isEmpty()) {
                            System.out.println("Keine Einträge mehr vorhanden nach Bearbeitung");
                        }
                        totalPages = (pageSize > 0) ? (items.size() + pageSize -1) / pageSize : 1;
                        currentPage = Math.min(currentPage, totalPages -1); 
                    }

                }
                continue;
            }

            currentPage = Paging.movePage(currentPage, totalPages, action);
        }
        
    }
}
