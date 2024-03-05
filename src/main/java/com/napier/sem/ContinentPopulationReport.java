package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ContinentPopulationReport implements PopulationReport {

    private final Connection con;

    public ContinentPopulationReport(Connection con) {
        this.con = con;
    }

    @Override
    public ArrayList<Country> generateReport(String continent) {
        ArrayList<Country> countries = new ArrayList<>();
        String query = "SELECT c.name, c.Population AS total_population, SUM(ci.Population) AS urban_population, " +
                "ROUND(SUM(ci.Population) / c.Population * 100, 0) AS urban_population_perc, " +
                "(c.Population - SUM(ci.Population)) AS rural_population, " +
                "ROUND((1 - (SUM(ci.Population) / c.Population)) * 100, 0) AS rural_population_perc " +
                "FROM country c JOIN city ci ON c.code = ci.CountryCode WHERE c.Continent = ? " +
                "GROUP BY c.name, c.Population ORDER BY total_population DESC";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, continent);
            ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Country country = new Country();
                country.setName(rset.getString("name"));
                country.setPopulation(rset.getInt("total_population"));
                country.setUrbanPopulation(rset.getInt("urban_population"));
                country.setUrbanPopulationPerc(rset.getInt("urban_population_perc"));
                country.setRuralPopulation(rset.getInt("rural_population"));
                country.setRuralPopulationPerc(rset.getInt("rural_population_perc"));
                countries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }

    @Override
    public void printReport(ArrayList<Country> countries) {
        System.out.println("\n+++++++++++++++++++++++++++++++");
        System.out.println("  Continent Population Report  ");
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.printf("%-30s %-20s %-20s %-20s %-20s %-20s%n", "Country", "Total Population", "Urban Population", "Urban Percentage", "Rural Population", "Rural Percentage");

        for (Country country : countries) {
            System.out.printf("%-30s %-20d %-20d %-20d %-20d %-20d%n",
                    country.getName(), country.getPopulation(), country.getUrbanPopulation(),
                    country.getUrbanPopulationPerc(), country.getRuralPopulation(),
                    country.getRuralPopulationPerc());
        }
    }
}
