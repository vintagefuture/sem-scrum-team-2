package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegionPopulationReport implements PopulationReport {

    public RegionPopulationReport(Connection con) {
        this.con = con;
    }

    private final Connection con;

    public void generateRegionReport(String region) {

        // Create string for SQL statement
        String query =
                "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE region='" + region + "' " +
                        "ORDER BY Population DESC";

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = region + " Population Report";
        List<String> columnNames = List.of("Code", "Name", "Continent", "Region", "Population", "City Name");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getCode());
            row.add(country.getName());
            row.add(country.getContinent());
            row.add(country.getRegion());
            row.add(String.valueOf(country.getPopulation()));
            row.add(country.getCapital());
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    private ArrayList<Country> generateCountryData(String query) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Country country = new Country();
                country.setCode(rset.getString("c.Code"));
                country.setName(rset.getString("c.Name"));
                country.setContinent(rset.getString("Continent"));
                country.setRegion(rset.getString("Region"));
                country.setPopulation(rset.getInt("c.Population"));
                country.setCapital(rset.getString("ci.Name"));
                countries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
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
