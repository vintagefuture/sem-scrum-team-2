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
 * - All the countries in the world organised by largest population to smallest.
 * - All the countries in a continent organised by largest population to smallest.
 * - All the countries in a region organised by largest population to smallest.
 */
public class CountriesPopulationReport {
    private static final Logger LOGGER = Logger.getLogger(CitiesReport.class.getName());
    private final Connection con;

    Helpers helpers = new Helpers();

    /**
     * Will be used to generate worldwide population report
     */
    public CountriesPopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * All the countries in the world organised by largest population to smallest
     */
    public void getCountriesPopulationInTheWorldReport() {
        // Prepare the SQL query
        String query =
                "SELECT c.code, c.Name, Continent, Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "ORDER BY Population DESC";

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "All the countries in the world organised by largest population to smallest";
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

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * All the countries in a continent organised by largest population to smallest
     * @param continent The chosen continent
     */
    public void getCountriesPopulationInContinentReport(String continent) {
        // Prepare the SQL query
        String query =
                "SELECT c.Code, c.Name, Continent, c.Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE continent='" + continent + "' " +
                        "ORDER BY Population DESC";
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "All the countries in continent " + continent + " organised by largest population to smallest";
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

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * All the countries in a region organised by largest population to smallest
     * @param region The chosen region
     */
    public void getCountriesPopulationInRegionReport(String region) {
        // Prepare the SQL query
        String query =
                "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE region='" + region + "' " +
                        "ORDER BY Population DESC";

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "All the countries in region " + region + " organised by largest population to smallest";

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

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * Helper method for populating the Country class
     * @param query The SQL query to parse the data from
     * @return countries
     */
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while generating country data: " + e.getMessage(), e);
        }
        return countries;
    }
}
