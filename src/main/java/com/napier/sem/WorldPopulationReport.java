package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorldPopulationReport {
    private Connection con;

    public WorldPopulationReport(Connection con) {
        this.con = con;
    }

    public List<Country> getTopNPopulatedCountries(Integer N) {
        // Check if N is null or less than 1 and decide on action
        if (N == null || N < 1) {
            return fetchAllCountries();
        } else {
            return fetchCountriesWithLimit(N);
        }
    }

    public List<Country> regionPopulationReport(String region) {
        List<Country> countries = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT  Name, Population "
                            + "FROM country "
                            + "WHERE region=" + region
                            + " ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Iterate over the ResultSet to fetch data for all countries
            while (rset.next()) {
                Country country = new Country();
                country.name = rset.getString("Name");
                country.population = rset.getInt("Population");
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country population report");
        }
        return countries;
    }

    private List<Country> fetchCountriesWithLimit(int N) {
        List<Country> countries = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect = "SELECT Name, Population FROM country ORDER BY Population DESC LIMIT " + N;
            ResultSet rset = stmt.executeQuery(strSelect);

            while (rset.next()) {
                Country country = new Country();
                country.name = rset.getString("Name");
                country.population = rset.getInt("Population");
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to fetch countries with limit");
        }
        return countries;
    }

    private List<Country> fetchAllCountries() {
        List<Country> countries = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect = "SELECT Name, Population FROM country ORDER BY Population DESC";
            ResultSet rset = stmt.executeQuery(strSelect);

            while (rset.next()) {
                Country country = new Country();
                country.name = rset.getString("Name");
                country.population = rset.getInt("Population");
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to fetch all countries");
        }
        return countries;
    }

    
}
