package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class provides methods for:
 * - The top `N` populated capital cities in the world  where `N` is provided by the user.
 * - The top `N` populated capital cities in a continent where `N` is provided by the user.
 * - The top `N` populated capital cities in a region where `N` is provided by the user.
 */
public class TopCapitalCitiesPopulationReport {
    private static final Logger LOGGER = Logger.getLogger(PopulationReport.class.getName());
    private final Connection con;
    Helpers helpers = new Helpers();

    public TopCapitalCitiesPopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * The top `N` populated capital cities in the world  where `N` is provided by the user
     * @param N Top filter
     */
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

    /**
     *  The top `N` populated capital cities in a continent where `N` is provided by the user
     * @param N Top filter
     * @param continent The chosen continent
     */
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

    /**
     * The top `N` populated capital cities in a region where `N` is provided by the user
     */
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

    /**
     * Helper method for populating the City class
     * @param query The SQL query to parse the data from
     * @return cities
     */
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while generating country data: " + e.getMessage(), e);
        }
        return cities;
    }
}
