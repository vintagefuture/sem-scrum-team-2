package com.napier.sem;

import java.util.List;

public interface PopulationReport {
    void printReport(String title, List<String> columnNames, List<List<String>> rows);
}

