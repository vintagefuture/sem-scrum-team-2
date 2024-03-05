package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorldPopulationReport implements PopulationReport {
    private final Connection con;

    /**
     * Will be used to generate worldwide population report */
    public WorldPopulationReport(Connection con) {
        this.con = con;
    }


    @Override
    public ArrayList<Country> generateCountryData(String parameter) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<City> generateCityData(String query) {
        return null;
    }

    @Override
    public void printReport(String title, List<String> columnNames, List<List<String>> rows) {

    }

}
