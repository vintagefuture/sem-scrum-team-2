package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContinentPopulationReport implements PopulationReport {

    private final Connection con;

    public ContinentPopulationReport(Connection con) {
        this.con = con;
    }

    public void generateAndPrintContinentReport(String continent) {
        // Prepare the SQL query
        String query = "SELECT c.name, c.Population AS total_population, SUM(ci.Population) AS urban_population, " +
                "ROUND(SUM(ci.Population) / c.Population * 100, 0) AS urban_population_perc, " +
                "(c.Population - SUM(ci.Population)) AS rural_population, " +
                "ROUND((1 - (SUM(ci.Population) / c.Population)) * 100, 0) AS rural_population_perc " +
                "FROM country c JOIN city ci ON c.code = ci.CountryCode WHERE c.Continent = '" + continent + "' " +
                "GROUP BY c.name, c.Population ORDER BY total_population DESC";
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = continent + " Population Report";
        List<String> columnNames = List.of("Country", "Total Population", "Urban Population", "Urban Percentage", "Rural Population", "Rural Percentage");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getName());
            row.add(String.valueOf(country.getPopulation()));
            row.add(String.valueOf(country.getUrbanPopulation()));
            row.add(country.getUrbanPopulationPerc() + "%");
            row.add(String.valueOf(country.getRuralPopulation()));
            row.add(country.getRuralPopulationPerc() + "%");
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    @Override
    public ArrayList<Country> generateCountryData(String query) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Country country = new Country();
                country.setName(rset.getString("name"));
                country.setPopulation(rset.getInt("total_population"));
                country.setUrbanPopulation(rset.getInt("urban_population"));
                country.setUrbanPopulationPerc(rset.getInt("urban_population_perc"));
                country.setRuralPopulation(rset.getInt("rural_population"));
                country.setRuralPopulationPerc(rset.getInt("rural_population_perc"));
                countries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }

    @Override
    public ArrayList<City> generateCityData(String query) {
        return null;
    }

    @Override
    public void printReport(String title, List<String> columnNames, List<List<String>> rows) {
        // Print report title
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));

        // Print column headers
        columnNames.forEach(columnName -> System.out.print(columnName + "\t"));
        System.out.println();

        // Print row data
        rows.forEach(row -> {
            row.forEach(cell -> System.out.print(cell + "\t"));
            System.out.println();
        });
    }
}
