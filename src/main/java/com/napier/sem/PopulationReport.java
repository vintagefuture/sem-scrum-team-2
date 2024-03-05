package com.napier.sem;

import java.util.ArrayList;

public interface PopulationReport {
    ArrayList<Country> generateReport(String parameter);
    void printReport(ArrayList<Country> countries);
}

