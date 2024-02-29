package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ContinentPopulationReport {

    private final Connection con;

    public ContinentPopulationReport(Connection con) {
        this.con = con;
    }

    public void generateReport(String continent) {
        List<Country> countries = new ArrayList<>();

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

            if (!countries.isEmpty()) {
                System.out.println("+++++++++++++++++++++++++++++++");
                System.out.println("Continent Population Report for " + continent);
                System.out.println("+++++++++++++++++++++++++++++++");
                for (Country country : countries) {
                    System.out.println("Country: " + country.name +
                            ", total_population: " + country.population +
                            ", urban_population: " + country.urban_population +
                            ", urban_population_perc: " + country.urban_population_perc +
                            ", rural_population: " + country.rural_population +
                            ", rural_population_perc: " + country.rural_population_perc);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get continent population report");
        }
    }
}