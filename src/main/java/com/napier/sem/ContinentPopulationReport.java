package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;


public class ContinentPopulationReport {

    private final Connection con;

    public ContinentPopulationReport(Connection con) {
        this.con = con;
    }

    public ArrayList<Country> generateReport(String continent) {
        ArrayList<Country> countries = new ArrayList<>();

        try {
            // Establish connection
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT " +
                    "c.name, " +
                    "c.Population AS total_population, " +
                    "SUM(ci.Population) AS urban_population, " +
                    "ROUND(SUM(ci.Population) / c.Population * 100, 0) AS urban_population_perc, " +
                    "(c.Population - SUM(ci.Population)) AS rural_population, " +
                    "ROUND((1 - (SUM(ci.Population) / c.Population)) * 100, 0) AS rural_population_perc " +
                    "FROM " +
                    "country c " +
                    "JOIN city ci ON c.code = ci.CountryCode " +
                    "WHERE " +
                    "c.Continent = '" + continent + "'" +
                    " GROUP BY " +
                    "c.name, c.Population " +
                    "ORDER BY " +
                    "total_population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Iterate over the ResultSet to fetch data for all countries
            while (rset.next()) {
                Country country = new Country();
                country.name = rset.getString("Name");
                country.population = rset.getInt("total_population");
                country.urban_population = rset.getInt("urban_population");
                country.urban_population_perc = rset.getInt("urban_population_perc");
                country.rural_population = rset.getInt("rural_population");
                country.rural_population_perc = rset.getInt("rural_population_perc");
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get continent population report");
        }
        return countries;
    }
    public void printReport(ArrayList<Country> countries) {

        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("  Continent Population Report  ");
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println();
        // Print header
        System.out.println(String.format("%-30s %-30s %-30s %-30s %-30s %-30s", "Country", "total_population", "urban_population", "urban_percentage", "rural_population", "rural_percentage"));

        for (Country country : countries) {
            String country_string = String.format("%-30s %-30s %-30s %-30s %-30s %-30s",
                    country.name, country.population, country.urban_population, country.urban_population_perc,
                    country.rural_population, country.rural_population_perc);
            System.out.println(country_string);
        }
    }
}