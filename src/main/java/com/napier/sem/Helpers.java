package com.napier.sem;

import java.util.List;

public class Helpers {

    public void printReport(String title, List<String> columnNames, List<List<String>> rows) {
        // Print report title
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));

        // Find maximum width for each column
        int[] maxWidths = new int[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {
            maxWidths[i] = columnNames.get(i).length();
        }
        for (List<String> row : rows) {
            for (int j = 0; j < row.size(); j++) {
                maxWidths[j] = Math.max(maxWidths[j], row.get(j).length());
            }
        }

        // Print column headers
        for (int i = 0; i < columnNames.size(); i++) {
            System.out.printf("%-" + (maxWidths[i] + 2) + "s", columnNames.get(i));
        }
        System.out.println();

        // Print row data
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                System.out.printf("%-" + (maxWidths[i] + 2) + "s", row.get(i));
            }
            System.out.println();
        }
    }
}
