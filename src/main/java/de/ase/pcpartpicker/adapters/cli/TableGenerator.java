package de.ase.pcpartpicker.adapters.cli;

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
    private String[] headers;

    public TableGenerator(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... columns) {
        rows.add(columns);
    }

    public void printTable() {
        if (rows.isEmpty()) {
            System.out.println("Keine Daten verfügbar.");
            return;
        }

        // Dynamisch maximale Spaltenanzahl ermitteln
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
        printRow(headers, columnWidths);
        System.out.println(divider);
        for (String[] row : rows) {
            printRow(row, columnWidths);
        }
        System.out.println(divider);
    }

    private void printRow(String[] row, int[] widths) {
        System.out.print("|");
        for (int i = 0; i < widths.length; i++) {
            String value = (i < row.length && row[i] != null) ? row[i] : "";
            System.out.printf(" %-" + widths[i] + "s |", value);
        }
        System.out.println();
    }
}