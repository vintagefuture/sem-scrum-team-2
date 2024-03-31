package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CapitalCityPopulationReport implements PopulationReport {

    private final Connection con;

    public CapitalCityPopulationReport(Connection con) {
        this.con = con;
    }

    public void getCapitalCityReportOfRegion(String region) {
        String query = "SELECT c.Name AS Name, ct.Name AS Country, c.Population AS Population\n" +
                "FROM country ct\n" +
                "JOIN city c ON ct.Capital = c.ID\n" +
                "WHERE ct.Region = '"+ region +"';";
        ArrayList<City> cities = generateCapitalCityData(query);

        // Prepare data for printing
        String title = "Capital city report of " + region;
        List<String> columnNames = List.of("Name", "Country", "Population");

        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(region);
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    public ArrayList<City> generateCapitalCityData(String query) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                City city = new City();

                city.setName(rset.getString("Name"));
                city.setCountry(rset.getString("Country"));
                city.setPopulation(rset.getInt("Population"));
                cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Proper error handling is recommended
        }
        return cities;

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
