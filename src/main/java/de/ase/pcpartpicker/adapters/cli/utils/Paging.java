package de.ase.pcpartpicker.adapters.cli.utils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Paging {
    public enum Action {
        NEXT, PREVIOUS, BACK, CLEAR, EDIT, OTHER
    }

    private Paging() {}

    public static Action parse(String raw, boolean allowClear, boolean allowEdit) {
        if (raw == null) return Action.OTHER;
        String input = raw.trim().toLowerCase();

        return switch (input) {
            case "m" -> Action.NEXT;
            case "n" -> Action.PREVIOUS;
            case "0" -> Action.BACK;
            case "c" -> allowClear ? Action.CLEAR : Action.OTHER;
            case "e" -> allowEdit ? Action.EDIT : Action.OTHER;
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

    public static String promptText(boolean allowClear, boolean allowEdit) {
        StringBuilder text = new StringBuilder("ID oder Aktion (m/n");
        if(allowClear) text.append("/c");
        if(allowEdit) text.append("/e");
        text.append("/0)"); 
        String result = text.toString();
        return result; 
    }

    
    public static <T> Builder<T> builder(List<T> items) {
        return new Builder<>(items);
    }

    public static class Builder<T> {
        private String pageTitle;
        private final List<T> items;
        private BiConsumer<T, Integer> renderPage;
        private Supplier<String> readInput;
        private Function<T, List<T>> onEdit = null;
        private Consumer<String> onOtherInput = null; 
        private Runnable onClear = null; 
        private int pageSize = 1; 

        
        public Builder(List<T> items) {
            this.items = items;
        }
        
        public Builder<T> withTitle(String title) {
            this.pageTitle = title;
            return this;
        }
        
        public Builder<T> withRenderer(BiConsumer<T, Integer> renderPage) {
            this.renderPage = renderPage;
            return this;
        }
        
        public Builder<T> withInputReader(Supplier<String> readInput) {
            this.readInput = readInput;
            return this;
        }
        
        
        public Builder<T> withPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }
        
        public Builder<T> onClear(Runnable clearAction) {
            this.onClear = clearAction; 
            return this; 
        }

        public Builder<T> onEdit(Function<T, List<T>> onEdit) {
            this.onEdit = onEdit;
            return this;
        }

        public Builder<T> onOtherInput(java.util.function.Consumer<String> action) {
            this.onOtherInput = action;
            return this;
        }

   
        
        public void start() {
            List<T> currentItems = this.items;
            
            if (currentItems == null || currentItems.isEmpty()) {
                System.out.println("Keine Einträge vorhanden.");
                return;
            }

            int currentPage = 0;
            int totalPages = (pageSize > 0) ? (currentItems.size() + pageSize - 1) / pageSize : 1;
            boolean canClear = (onClear != null);
            boolean canEdit = (onEdit != null);

            while (true) {
                NavigationUtils.clear();

                if (pageTitle != null && !pageTitle.isBlank()) {
                    System.out.println("\n=== " + pageTitle + " ===\n");
                }

                renderPage.accept(currentItems.get(currentPage), currentPage);

                System.out.println(Paging.helpText(currentPage, totalPages, canClear, canEdit));
                System.out.print(Paging.promptText(canClear, canEdit)); 
                String input = readInput.get();
                Action action = Paging.parse(input, canClear, canEdit);

                if (action == Action.BACK) return;

                if(action == Action.PREVIOUS || action == Action.NEXT) {
                    currentPage = Paging.movePage(currentPage, totalPages, action); 
                    continue; 
                }
                
                if (action == Action.CLEAR && canClear) {
                    onClear.run();
                    continue;  
                }

                if (action == Action.EDIT && onEdit != null) {
                    if (onEdit != null) {
                        List<T> updatedItems = onEdit.apply(currentItems.get(currentPage));
                        if (updatedItems != null) {
                            currentItems = updatedItems;
                            if (currentItems.isEmpty()) {
                                System.out.println("Keine Einträge mehr vorhanden nach Bearbeitung.");
                                return;
                            }
                            totalPages = (pageSize > 0) ? (currentItems.size() + pageSize - 1) / pageSize : 1;
                            currentPage = Math.min(currentPage, totalPages - 1);
                        }
                    }
                    continue;
                }

                if(action == Action.OTHER && onOtherInput != null) {
                    onOtherInput.accept(input);
                }
            }
        }
    }

}
