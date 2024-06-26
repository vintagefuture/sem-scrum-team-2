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
 * This class provides the functionality for:
 * - All the cities in the world organised by largest population to smallest.
 * - All the cities in a continent organised by largest population to smallest.
 * - All the cities in a region organised by largest population to smallest.
 * - All the cities in a country organised by largest population to smallest.
 * - All the cities in a district organised by largest population to smallest.
 */
public class CitiesReport
{
    private static final Logger LOGGER = Logger.getLogger(CitiesReport.class.getName());

    private final Connection con;
    Helpers helpers = new Helpers();

    public CitiesReport(Connection con) {
        this.con = con;
    }

    /**
     * All the cities in the world organised by largest population to smallest
     */
    public void getCitiesPopulationReportInTheWorld() {
        // Prepare the SQL query
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "ORDER BY Population DESC";

        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "All the cities in the world organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");

        ArrayList<List<String>> rows = new ArrayList<>();

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
     * All the cities in a continent organised by largest population to smallest
     * @param continent the chosen continent
     */
    public void getCitiesPopulationInContinent(String continent) {
        // Prepare the SQL query
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country c ON c.Code = ci.CountryCode " +
                        "WHERE continent = '" + continent + "' " +
                        "ORDER BY Population DESC";

        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "All the cities in continent " + continent + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");

        ArrayList<List<String>> rows = new ArrayList<>();

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
     * All the cities in a country organised by largest population to smallest
     * @param country the chosen country
     */
    public void getCitiesPopulationReportInCountry(String country) {
        // Prepare the SQL query
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country c ON c.Code = ci.CountryCode " +
                        "WHERE c.Name = '" + country + "' " +
                        "ORDER BY ci.Population DESC";

        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "All the cities in country " + country + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");

        ArrayList<List<String>> rows = new ArrayList<>();

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
     * All the cities in a district organised by largest population to smallest.
     */
    public void getCitiesPopulationReportInDistrict(String districtName) {
        // Prepare the SQL query
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "WHERE ci.District = '" + districtName + "' " + 
                "ORDER BY ci.Population DESC";

        ArrayList<City> cities = generateCityData(query); 

        // Prepare data for printing
        String title = "All the cities in the district " + districtName + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");

        ArrayList<List<String>> rows = new ArrayList<>();

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
     * All the cities in a region organised by largest population to smallest.
     * @param region the chosen region
     */
    public void getCitiesPopulationInRegion(String region) {
        // Prepare the SQL query
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country c ON c.Code = ci.CountryCode " +
                        "WHERE region = '" + region + "' " +
                        "ORDER BY Population DESC";

        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "All the cities in region " + region + " organised by largest population to smallest";
        List<String> columnNames = List.of("Name", "Country", "District", "Population");

        ArrayList<List<String>> rows = new ArrayList<>();

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
            LOGGER.log(Level.SEVERE, "Error while generating city data: " + e.getMessage(), e);
        }
        return cities;
    }
}
