package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * This class will provide functionalities for:
 * - The population of the world
 * - The population of a continent
 * - The population of a region
 * - The population of a country
 * - The population of a district
 * - The population of a city
 */
public class TotalPopulationReport {

    private final Connection con;

    public TotalPopulationReport(Connection con) {
        this.con = con;
    }

    // The population of the world
    public void getTotalPopulationInWorld() {

        String query = "SELECT SUM(population) AS total_population FROM country";
        String title = "The population of the world";
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    // The population of a continent
    public void getTotalPopulationInContinent(String continent) {
        String query = "SELECT SUM(population) AS total_population FROM country\n" +
                "WHERE continent = '" + continent + "'";
        String title = "The population of continent " + continent;
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    // The population of a region
    public void getTotalPopulationInRegion(String region) {
        String query = "SELECT SUM(population) AS total_population FROM country\n" +
                "WHERE region = '" + region + "'";
        String title = "The population of region " + region;
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    // The population of a country
    public void getTotalPopulationInCountry(String country) {
        String query = "SELECT population AS total_population FROM country WHERE name = '" + country + "'";
        String title = "The population of country " + country;
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    // The population of a district
    public void getTotalPopulationInDistrict(String district) {
        String query = "SELECT SUM(population) AS total_population\n" +
                "FROM city\n" +
                "WHERE district = '" + district + "'";
        String title = "The population of district " + district;
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    // The population of a city
    public void getTotalPopulationInCity(String city) {
        String query = "SELECT population AS total_population\n" +
                "FROM city\n" +
                "WHERE name = '" + city + "'";
        String title = "The population of city " + city;
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
        System.out.println(generateTotalPopulationData(query));
    }

    public long generateTotalPopulationData(String query) {
        long totalPopulation = 0;
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalPopulation;
    }
}
