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
 * - The top `N` populated countries in the world  where `N` is provided by the user.
 * - The top `N` populated countries in a continent where `N` is provided by the user.
 * - The top `N` populated countries in a region where `N` is provided by the user.
 */
public class TopCountriesPopulationReport {
    private static final Logger LOGGER = Logger.getLogger(TopCountriesPopulationReport.class.getName());

    private final Connection con;
    Helpers helpers = new Helpers();

    public TopCountriesPopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * The top `N` populated countries in the world  where `N` is provided by the user
     * @param N Top filter
     */
    public void getTopPopulatedCountriesInTheWorld(int N) {
        String query =
                "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name AS Capital FROM country c JOIN city ci ON c.Capital = ci.ID ORDER BY Population DESC LIMIT " + N;

        // Execute SQL statement
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated countries in the world";
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
     *  The top `N` populated countries in a continent where `N` is provided by the user
     * @param continent The chosen continent
     * @param N Top filter
     */
    public void getTopPopulatedCountriesInContinent(String continent, int N) {
        String query = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital FROM country c JOIN city ci ON c.Capital = ci.ID WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + N;

        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated countries in continent " + continent;
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
     * The top `N` populated countries in a region where `N` is provided by the user
     * @param region The chosen region
     * @param N Top filter
     */
    public void getTopPopulatedCountriesInRegion(String region, int N) {

        String query = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital FROM country c JOIN city ci ON c.Capital = ci.ID WHERE Region = '" + region + "' ORDER BY Population DESC LIMIT " + N;

        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "The top " + N + " populated countries in region " + region;
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
     * Helper method for retrieving country data
     * @param parameter The SQL query to parse the data from
     * @return countries
     */
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while generating country data: " + e.getMessage(), e);
        }
        return countries;
    }
}
