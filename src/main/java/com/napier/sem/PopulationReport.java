package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to generate population reports based on different geographical levels.
 */
public class PopulationReport {

    private final Connection con;
    Helpers helpers = new Helpers();

    public PopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * Generates a population report comparing population in cities versus non-city areas by continent.
     */
    public void generatePopulationInCitiesVSNonCityByContinent() {
        // SQL query to compute population data by continent
        List<List<String>> rows = generateContinentData();
        String title = "The population of people, people living in cities, and people not living in cities in each continent";
        List<String> columnNames = List.of("Continent", "Total Population", "City Population", "Non-City Population", "City Population %", "Non-City Population %");
        helpers.printReport(title, columnNames, rows);
    }

    private List<List<String>> generateContinentData() {

        String query = "SELECT co.Continent, " +
                "SUM(co.Population) AS TotalPopulation, " +
                "SUM(ci.CityPopulation) AS PopulationInCities, " +
                "SUM(co.Population) - SUM(ci.CityPopulation) AS PopulationInNonCityAreas, " +
                "ROUND((SUM(ci.CityPopulation) / SUM(co.Population)) * 100) AS PercentageCities, " +
                "ROUND(((SUM(co.Population) - SUM(ci.CityPopulation)) / SUM(co.Population)) * 100) AS PercentageNonCities " +
                "FROM country co " +
                "LEFT JOIN (SELECT c.CountryCode, SUM(c.Population) AS CityPopulation FROM city c GROUP BY c.CountryCode) ci ON co.Code = ci.CountryCode " +
                "GROUP BY co.Continent " +
                "ORDER BY TotalPopulation DESC";

        ArrayList<List<String>> rows = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                List<String> row = new ArrayList<>();
                row.add(rset.getString("Continent"));
                row.add(String.valueOf(rset.getBigDecimal("TotalPopulation")));
                row.add(String.valueOf(rset.getBigDecimal("PopulationInCities")));
                row.add(String.valueOf(rset.getBigDecimal("PopulationInNonCityAreas")));
                row.add(String.valueOf(rset.getBigDecimal("PercentageCities")));
                row.add(String.valueOf(rset.getBigDecimal("PercentageNonCities")));
                rows.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Generates a population report comparing population in cities versus non-city areas by region.
     */
    public void generatePopulationInCitiesVSNonCityByRegion() {
        // SQL query to compute population data by region
        List<List<String>> rows = generateRegionData();
        String title = "The population of people, people living in cities, and people not living in cities in each region";
        List<String> columnNames = List.of("Region", "Total Population", "City Population", "Non-City Population", "City Population %", "Non-City Population %");
        helpers.printReport(title, columnNames, rows);
    }

    private List<List<String>> generateRegionData() {

        String query = "SELECT country.region AS Region, " +
                "SUM(country.population) AS TotalPopulation, " +
                "SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code)) AS PopulationInCities, " +
                "(SUM(country.population)-SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code))) AS PopulationInNonCityAreas, " +
                "ROUND((SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code)) / SUM(country.population))*100) AS PercentageCities, " +
                "ROUND(((SUM(country.population) - SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code))) / SUM(country.population))*100) AS PercentageNonCities " +
                "FROM country " +
                "GROUP BY country.region " +
                "ORDER BY TotalPopulation DESC";

        ArrayList<List<String>> rows = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                List<String> row = new ArrayList<>();
                row.add(rset.getString("Region"));
                row.add(String.valueOf(rset.getBigDecimal("TotalPopulation")));
                row.add(String.valueOf(rset.getBigDecimal("PopulationInCities")));
                row.add(String.valueOf(rset.getBigDecimal("PopulationInNonCityAreas")));
                row.add(String.valueOf(rset.getBigDecimal("PercentageCities")));
                row.add(String.valueOf(rset.getBigDecimal("PercentageNonCities")));
                rows.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;

    }


    /**
     * Generates a population report comparing population in cities versus non-city areas by country.
     */
    public void generatePopulationInCitiesVSNonCityByCountry() {
        // SQL query to compute population data by country
        List<List<String>> rows = generateCountryData();
        String title = "The population of people, people living in cities, and people not living in cities in each country";
        List<String> columnNames = List.of("Country", "Total Population", "City Population", "Non-City Population", "City Population %", "Non-City Population %");
        helpers.printReport(title, columnNames, rows);
    }

    private List<List<String>> generateCountryData() {
        String query = "SELECT " +
                "co.name AS country_name, " +
                "co.population AS total_population, " +
                "SUM(ci.population) AS population_in_cities, " +
                "co.population - SUM(ci.population) AS population_not_in_cities, " +
                "ROUND((SUM(ci.population) / co.population) * 100) AS percentage_population_in_cities, " +
                "ROUND(((co.population - SUM(ci.population)) / co.population) * 100) AS percentage_population_not_in_cities " +
                "FROM " +
                "country co " +
                "LEFT JOIN " +
                "city ci ON co.code = ci.countrycode " +
                "GROUP BY " +
                "co.code, co.name, co.population " +
                "ORDER BY " +
                "total_population DESC";

        ArrayList<List<String>> rows = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                List<String> row = new ArrayList<>();
                row.add(rset.getString("country_name"));
                row.add(String.valueOf(rset.getBigDecimal("total_population")));
                row.add(String.valueOf(rset.getBigDecimal("population_in_cities")));
                row.add(String.valueOf(rset.getBigDecimal("population_not_in_cities")));
                row.add(String.valueOf(rset.getBigDecimal("percentage_population_in_cities")));
                row.add(String.valueOf(rset.getBigDecimal("percentage_population_not_in_cities")));
                rows.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
}
