package de.ase.pcpartpicker.adapters.cli.utils;

public final class PagingInput {
    public enum Action {
        NEXT, PREVIOUS, BACK, OTHER
    }

    private PagingInput() {}

    public static Action parse(String raw) {
        if (raw == null) return Action.OTHER;
        String input = raw.trim().toLowerCase();

        return switch (input) {
            case "m" -> Action.NEXT;
            case "n" -> Action.PREVIOUS;
            case "0" -> Action.BACK;
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
}
