package de.ase.pcpartpicker.adapters.cli;

import java.util.ArrayList;
import java.util.List;

public class TableGenerator {
    private final List<String[]> rows = new ArrayList<>();
    private final String[] headers; 

    public TableGenerator(String... headers) {
        this.headers = headers; 
    }

    public void addRow(String... columns) {
        rows.add(columns);
    }

    public void printTable() {
        if(rows.isEmpty()) {
            System.out.println("Keine Daten verfügbar.");
            return;
        }

        int[] columnWidths = new int[headers.length];
        for(int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
            for (String[] row: rows) {
                if(row[i] != null) {
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
            }
        }

        StringBuilder divider = new StringBuilder("+"); 
        for(int width: columnWidths) {
            divider.append("-".repeat(width+2)).append("+");
        }


        System.out.println(divider);
        printRow(headers, columnWidths); 
        System.out.println(divider);
        for (String[] row: rows) {
            printRow(row, columnWidths);
        }
        System.out.println(divider);

    }

    private void printRow(String[] row, int[] widths) {
        System.out.print("|");
        for (int i = 0; i < row.length; i++) {
            System.out.printf(" %-" + widths[i] + "s |", row[i]);
        }
        System.out.println();
    }
}
