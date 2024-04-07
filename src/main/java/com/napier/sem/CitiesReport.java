package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    private final Connection con;

    public CitiesReport(Connection con) {
        this.con = con;
    }

    Helpers helpers = new Helpers();

    // All the cities in the world organised by largest population to smallest
    public void getCitiesPopulationReportInTheWorld() {
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

    // All the cities in a continent organised by largest population to smallest
    public void getCitiesPopulationInContinent(String continent) {
        String query =
                "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country c ON c.Code = ci.CountryCode " +
                        "WHERE continent = '" + continent + "' " +
                        "ORDER BY Population DESC";

        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "All the cities in a continent organised by largest population to smallest";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }
}
