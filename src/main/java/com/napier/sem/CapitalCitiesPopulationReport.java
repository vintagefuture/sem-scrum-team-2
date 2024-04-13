package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the functionality for:
 * - All the capital cities in the world organised by largest population to smallest.
 * - All the capital cities in a continent organised by largest population to smallest.
 * - All the capital cities in a region organised by largest population to smallest.
 */
public class CapitalCitiesPopulationReport {

    private final Connection con;

    Helpers helpers = new Helpers();


    public CapitalCitiesPopulationReport(Connection con) {
        this.con = con;
    }

    // All the capital cities in the world organised by largest population to smallest
    public void getCapitalCitiesReportOfTheWorld() {
        String query = "SELECT ci.Name, c.Name AS Country, ci.Population\n" +
                "FROM city ci\n" +
                "JOIN country c ON c.Capital = ci.id\n" +
                "ORDER BY Population DESC";

        ArrayList<City> cities = generateCapitalCityData(query);

        // Prepare data for printing
        String title = "All the capital cities in the world organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "Population");

        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    // All the capital cities in a continent organised by largest population to smallest
    public ArrayList<City> getCapitalCitiesReportOfContinent(String continent) {
        String query = "SELECT c.Name AS Name, ct.Name AS Country, c.Population AS Population\n" +
                "FROM country ct " +
                "JOIN city c ON ct.Capital = c.ID " +
                "WHERE ct.Continent = '" + continent + "' " +
                "ORDER BY Population DESC";

        ArrayList<City> cities = generateCapitalCityData(query);

        // Prepare data for printing
        String title = "All the capital cities in continent " + continent + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "Population");

        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
        return cities;
    }

    // All the capital cities in a region organised by largest population to smallest
    public void getCapitalCityReportOfRegion(String region) {
        String query = "SELECT c.Name AS Name, ct.Name AS Country, c.Population AS Population\n" +
                "FROM country ct " +
                "JOIN city c ON ct.Capital = c.ID " +
                "WHERE ct.Region = '"+ region +"' " +
                "ORDER BY Population DESC";

        ArrayList<City> cities = generateCapitalCityData(query);

        // Prepare data for printing
        String title = "All the capital cities in region " + region + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "Population");

        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
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
}
