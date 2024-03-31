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
        String query = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital FROM country c JOIN city ci ON c.Capital = ci.ID WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + N;

        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + continent;
        List<String> columnNames = List.of("Code", "Name", "Continent", "Region", "Population", "Capital");
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

    public void getTopNPopulatedCountriesInRegion(String region, int N) {

        String query = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital FROM country c JOIN city ci ON c.Capital = ci.ID WHERE Region = '" + region + "' ORDER BY Population DESC LIMIT " + N;


        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + region;
        List<String> columnNames = List.of("Code", "Name", "Continent", "Region", "Population", "Capital");
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

    public ArrayList<Country> generateCountryData(String parameter) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(parameter)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Country country = new Country();
                country.setCode(rset.getString("Code"));
                country.setName(rset.getString("Name"));
                country.setContinent(rset.getString("Continent"));
                country.setRegion(rset.getString("Region"));
                country.setPopulation(rset.getInt("Population"));
                country.setCapital(rset.getString("Capital"));
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

        // Find maximum width for each column
        int[] maxWidths = new int[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {
            maxWidths[i] = columnNames.get(i).length();
        }
        for (List<String> row : rows) {
            for (int j = 0; j < row.size(); j++) {
                maxWidths[j] = Math.max(maxWidths[j], row.get(j).length());
            }
        }

        // Print column headers
        for (int i = 0; i < columnNames.size(); i++) {
            System.out.printf("%-" + (maxWidths[i] + 2) + "s", columnNames.get(i));
        }
        System.out.println();

        // Print row data
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                System.out.printf("%-" + (maxWidths[i] + 2) + "s", row.get(i));
            }
            System.out.println();
        }
    }

}
