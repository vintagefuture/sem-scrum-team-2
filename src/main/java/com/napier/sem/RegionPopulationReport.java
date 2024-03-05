package com.napier.sem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RegionPopulationReport {

    public RegionPopulationReport(Connection con) {
        this.con = con;
    }
    private final Connection con;
    public ArrayList<Country> generateReport(String region) {
        ArrayList<Country> countries = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name " +
                    "FROM country c " +
                    "JOIN city ci ON c.Capital = ci.ID " +
                    "WHERE region='" + region + "' " +
                    "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Iterate over the ResultSet to fetch data for all countries
            while (rset.next()) {
                Country country = new Country();
                country.code = rset.getString("c.Code");
                country.name = rset.getString("c.Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("c.Population");
                country.capital = rset.getString("ci.Name");
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get region population report");
        }
        return countries;
    }
}
