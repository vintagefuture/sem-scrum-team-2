package com.napier.sem;

import java.util.ArrayList;
import java.util.List;

public interface PopulationReport {
    ArrayList<Country> generateReport(String query);
    void printReport(String title, List<String> columnNames, List<List<String>> rows);
}

