package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * This class provides methods for:
 * - The top `N` populated capital cities in the world  where `N` is provided by the user.
 * - The top `N` populated capital cities in a continent where `N` is provided by the user.
 * - The top `N` populated capital cities in a region where `N` is provided by the user.
 */
public class TopCapitalCitiesPopulationReport {

    private final Connection con;

    public TopCapitalCitiesPopulationReport(Connection con) {
        this.con = con;
    }

    Helpers helpers = new Helpers();

    // The top `N` populated capital cities in the world  where `N` is provided by the user
    public void getTopCapitalCitiesInTheWorldReport(int N) {

        // SQL query to select the top N populated capital cities in the world
        String query =
                "SELECT city.Name, country.Name AS Country, city.Population " +
                        "FROM city JOIN country ON city.ID = country.Capital " +
                        "ORDER BY city.Population DESC LIMIT "+ N ;
        ArrayList<City> cities = generateCapitalCitiesData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Capital Cities in the World";
        List<String> columnNames = List.of("City Name", "Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city: cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountryCode());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        // Print out data for printing
        helpers.printReport(title, columnNames, rows);
    }

    // The top `N` populated capital cities in a continent where `N` is provided by the user
    public void getTopCapitalCitiesInTheContinentReport(int N, String continent) {

        // SQL query to select the top N populated capital cities in the continent
        String query =
                "SELECT city.Name, country.Name AS Country, city.Population " +
                        "FROM city JOIN country ON city.ID = country.Capital " +
                        "WHERE country.Continent='" + continent + "' " +
                        "ORDER BY city.Population DESC LIMIT "+ N ;
        ArrayList<City> cities = generateCapitalCitiesData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Capital Cities in continent " + continent;
        List<String> columnNames = List.of("City Name", "Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city: cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountryCode());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    // The top `N` populated capital cities in a region where `N` is provided by the user
    public void getTopCapitalCitiesInTheRegionReport(int N, String region) {

        // SQL query to select the top N populated capital cities in the region
        String query =
                "SELECT city.Name, country.Name AS Country, city.Population " +
                        "FROM city JOIN country ON city.ID = country.Capital " +
                        "WHERE country.Region='" + region + "' " +
                        "ORDER BY city.Population DESC LIMIT "+ N ;
        ArrayList<City> cities = generateCapitalCitiesData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Capital Cities in region " + region;
        List<String> columnNames = List.of("City Name", "Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city: cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountryCode());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    public ArrayList<City> generateCapitalCitiesData(String query) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                City city = new City();
                city.setName(rset.getString("Name"));
                city.setCountryCode(rset.getString("Country"));
                city.setPopulation(rset.getInt("Population"));
                cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Proper error handling is recommended
        }
        return cities;
    }
}
