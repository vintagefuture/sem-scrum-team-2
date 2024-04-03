package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PopulationReport {

    private final Connection con;

    public PopulationReport(Connection con) {
        this.con = con;
    }

    Helpers helpers = new Helpers();


    public void generatePopulationInCitiesVSNonCityByContinent() {
        // SQL query to compute population data by continent
        List<List<String>> rows = generatePopulationData("country.Continent");
        String title = "Population Report City vs Non City by Continent";
        List<String> columnNames = List.of("Continent", "Total Population", "City Population", "Non-City Population");
        helpers.printReport(title, columnNames, rows);
    }

    public void generatePopulationInCitiesVSNonCityByRegion() {
        // SQL query to compute population data by region
        List<List<String>> rows = generatePopulationData("country.Region");
        String title = "Population Report City vs Non City by Region";
        List<String> columnNames = List.of("Region", "Total Population", "City Population", "Non-City Population");
        helpers.printReport(title, columnNames, rows);
    }

    public void generatePopulationInCitiesVSNonCityByCountry() {
        // SQL query to compute population data by continent
        List<List<String>> rows = generatePopulationData("country.Name");
        String title = "Population Report City vs Non City by Country";
        List<String> columnNames = List.of("Country", "Total Population", "City Population", "Non-City Population");
        helpers.printReport(title, columnNames, rows);
    }

    private List<List<String>> generatePopulationData(String level) {
        // Dynamic SQL query based on the aggregation level
        String query =
                "SELECT " + level + " AS Level, " +
                        "SUM(country.Population) AS TotalPopulation, " +
                        "SUM(city.Population) AS CityPopulation, " +
                        "(SUM(country.Population) - SUM(city.Population)) AS NonCityPopulation " +
                        "FROM country " +
                        "JOIN city ON country.Code = city.CountryCode " +
                        "GROUP BY " + level +
                        " ORDER BY TotalPopulation DESC";

        ArrayList<List<String>> rows = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                List<String> row = new ArrayList<>();
                row.add(rset.getString("Level"));
                row.add(String.valueOf(rset.getBigDecimal("TotalPopulation")));
                row.add(String.valueOf(rset.getBigDecimal("CityPopulation")));
                row.add(String.valueOf(rset.getBigDecimal("NonCityPopulation")));
                rows.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Proper error handling is recommended
        }

        return rows;
    }
}
