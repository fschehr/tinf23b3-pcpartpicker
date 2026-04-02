package de.ase.pcpartpicker.adapters.cli.utils;

public final class PagingInput {
    public enum Action {
        NEXT, PREVIOUS, BACK, CLEAR, EDIT, OTHER
    }

    private PagingInput() {}

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

    public static String helpText(int currentPage, int totalPages, boolean allowClear) {
        String base = "Seite " + (currentPage + 1) + " von " + totalPages
                + " | m = nächste Seite | n = vorherige Seite";
        return allowClear ? base + " | c = Auswahl löschen | 0 = zurück"
                : base + " | 0 = zurück";
    }

    public static String promptText(boolean allowClear) {
        return allowClear ? "ID oder Aktion (m/n/c/0)" : "Aktion (m/n/0)";
    }
}
