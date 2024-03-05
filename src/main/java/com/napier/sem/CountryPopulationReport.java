package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class CountryPopulationReport implements PopulationReport {

    private final Connection con;

    public CountryPopulationReport(Connection con) {
        this.con = con;
    }

    public void getTopNPopulatedCountriesInContinent(String continent, int N) {
        String query = "SELECT Name, Population FROM country WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + N;
        ArrayList<Country> countries = generateReport(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + continent;
        List<String> columnNames = List.of("Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getName());
            row.add(String.valueOf(country.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    public void getTopNPopulatedCountriesInRegion(String region, int N) {
        String query = "SELECT Name, Population FROM country WHERE Region = '" + region + "' ORDER BY Population DESC LIMIT " + N;
        ArrayList<Country> countries = generateReport(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + region;
        List<String> columnNames = List.of("Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getName());
            row.add(String.valueOf(country.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    @Override
    public ArrayList<Country> generateReport(String parameter) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(parameter)) {
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
            e.printStackTrace(); // Proper error handling is recommended
        }
        return countries;
    }

    @Override
    public void printReport(String title, List<String> columnNames, List<List<String>> rows) {
        // Print report title
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));

        // Print column headers
        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
        System.out.println();

        // Print row data
        for (List<String> row : rows) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

}
