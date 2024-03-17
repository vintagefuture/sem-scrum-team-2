package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorldPopulationReport implements PopulationReport {
    private final Connection con;

    /**
     * Will be used to generate worldwide population report
     */
    public WorldPopulationReport(Connection con) {
        this.con = con;
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

    public void fetchCountriesWithLimit(int N) {
        String query =
                "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Fetch Countries with limit " + N;
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

    public void fetchAllCountries() {
        String query =
                "SELECT c.code, c.Name, Continent, Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "ORDER BY Population DESC";

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Fetch All Countries";
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
}
