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
 * - The top `N` populated cities in the continent  where `N` is provided by the user.
 * - The top `N` populated cities in the world  where `N` is provided by the user
 * - The top `N` populated cities in the country  where `N` is provided by the user
 */
public class TopCitiesPopulationReport {
    private static final Logger LOGGER = Logger.getLogger(TopCitiesPopulationReport.class.getName());
    private final Connection con;

    Helpers helpers = new Helpers();

    public TopCitiesPopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * The top `N` populated cities in the world  where `N` is provided by the user
     * @param N Top filter
     */
    public void getTopPopulatedCitiesInTheWorld(int N) {
        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci ON c.Code = ci.CountryCode ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated cities in the world";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(city.getDistrict());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * The top `N` populated cities in the continent  where `N` is provided by the user
     * @param continent The chosen continent
     * @param N Top filter
     */
    public void getTopPopulatedCitiesInTheContinent(String continent, int N) {
        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci ON c.Code = ci.CountryCode WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated cities in continent " + continent;
        List<String> columnNames = List.of("Name", "Country", "District", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(city.getDistrict());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * The top `N` populated cities in the country  where `N` is provided by the user
     * @param country The chosen country
     * @param N Top filter
     */
    public void getTopPopulatedCitiesInTheCountry(String country, int N) {
        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci ON c.Code = ci.CountryCode WHERE c.Name = '" + country + "' ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated cities in country " + country;
        List<String> columnNames = List.of("Name", "Country", "District", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(city.getDistrict());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * The top `N` populated cities in the region  where `N` is provided by the user
     * @param region The chosen region
     * @param N Top filter
     */
    public void getTopPopulatedCitiesInTheRegion(String region, int N) {
        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci ON c.Code = ci.CountryCode WHERE c.Region = '" + region + "' ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated cities in region " + region;
        List<String> columnNames = List.of("Name", "Country", "District", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(city.getDistrict());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * The top `N` populated cities in the district  where `N` is provided by the user
     * @param district The chose district
     * @param N Top filter
     */
    public void getTopPopulatedCitiesInTheDistrict(String district, int N) {
        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci ON c.Code = ci.CountryCode WHERE ci.District = '" + district + "' ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated cities in district " + district;
        List<String> columnNames = List.of("Name", "Country", "District", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountry());
            row.add(city.getDistrict());
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
    public ArrayList<City> generateCityData(String query) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                City city = new City();

                city.setName(rset.getString("Name"));
                city.setCountry(rset.getString("Country"));
                city.setDistrict(rset.getString("District"));
                city.setPopulation(rset.getInt("Population"));
                cities.add(city);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while generating country data: " + e.getMessage(), e);
        }
        return cities;
    }
}
