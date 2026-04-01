package de.ase.pcpartpicker.adapters.cli;

import de.ase.pcpartpicker.ColorConstants;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasse, die Tabellen für das CLI erzeugt
 * @param rows Spalten der Tabelle
 * @param headers Überschriften der Tabelle 
 * @author Henri
 */
public class TableGenerator {
    private final List<String[]> rows = new ArrayList<>();
    private final List<String> coloredRows = new ArrayList<>();
    private String[] headers;

    public TableGenerator(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... columns) {
        rows.add(columns);
        coloredRows.add(null);
    }

    public void addColoredRow(String color, String... columns) {
        rows.add(columns);
        coloredRows.add(color);
    }


    public void printTable() {
        if (rows.isEmpty()) {
            System.out.println("Keine Daten verfügbar.");
            return;
        }

        int maxColumns = headers.length;
        for (String[] row : rows) {
            maxColumns = Math.max(maxColumns, row.length);
        }
        // Headers erweitern falls nötig
        if (headers.length < maxColumns) {
            String[] newHeaders = new String[maxColumns];
            for (int i = 0; i < maxColumns; i++) {
                newHeaders[i] = (i < headers.length) ? headers[i] : "";
            }
            headers = newHeaders;
        }

        // Spaltenbreiten berechnen
        int[] columnWidths = new int[maxColumns];
        for (int i = 0; i < maxColumns; i++) {
            columnWidths[i] = headers[i].length();
            for (String[] row : rows) {
                if (i < row.length && row[i] != null) {
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
            }
        }

        // Tabelle ausgeben
        StringBuilder divider = new StringBuilder("+");
        for (int width : columnWidths) {
            divider.append("-".repeat(width + 2)).append("+");
        }

        System.out.println(divider);
        printRow(headers, columnWidths, null);
        System.out.println(divider);

        for (int i = 0; i < rows.size(); i++) {
            String color = coloredRows.get(i);
            printRow(rows.get(i), columnWidths, color);
        }
        System.out.println(divider);
    }


    private void printRow(String[] row, int[] widths, String color) {
        String prefix = (color != null) ? color: "";
        String suffix = (color != null) ? ColorConstants.ANSI_RESET : "";
        System.out.print(prefix + "|");
        for (int i = 0; i < widths.length; i++) {
            String value = (i < row.length && row[i] != null) ? row[i] : "";
            System.out.printf(" %-" + widths[i] + "s |", value);
        }
        System.out.println(suffix);


    }


}