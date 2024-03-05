package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class WorldPopulationReport {
    private Connection con;

    public WorldPopulationReport(Connection con) {
        this.con = con;
    }

    public ArrayList<Country> getTopNPopulatedCountries(Integer N) {
        // Check if N is null or less than 1 and decide on action
        if (N == null || N < 1) {
            return fetchAllCountries();
        } else {
            return fetchCountriesWithLimit(N);
        }
    }

    private ArrayList<Country> fetchCountriesWithLimit(int N) {
        ArrayList<Country> countries = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT c.Code, c.Name, Continent, Region, c.Population, ci.Name " +
                    "FROM country c " +
                    "JOIN city ci ON c.Capital = ci.ID " +
                    "ORDER BY Population DESC LIMIT " + N;

            ResultSet rset = stmt.executeQuery(strSelect);

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
            System.out.println("Failed to fetch countries with limit");
        }
        return countries;
    }

    private ArrayList<Country> fetchAllCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT c.code, c.Name, Continent, Region, c.Population, ci.Name " +
                    "FROM country c " +
                    "JOIN city ci ON c.Capital = ci.ID " +
                    "ORDER BY Population DESC";

            ResultSet rset = stmt.executeQuery(strSelect);

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
            System.out.println("Failed to fetch all countries");
        }
        return countries;
    }
}
