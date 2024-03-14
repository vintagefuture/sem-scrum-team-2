package com.napier.sem;

import java.util.ArrayList;
import java.util.List;

public interface PopulationReport {
    ArrayList<Country> generateCountryData(String query);
    ArrayList<City> generateCityData(String query);
    void printReport(String title, List<String> columnNames, List<List<String>> rows);
}

