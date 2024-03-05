package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class WorldPopulationReport implements PopulationReport {
    private final Connection con;

    public WorldPopulationReport(Connection con) {
        this.con = con;
    }

    public ArrayList<Country> getTopNPopulatedCountries(int N) {
        ArrayList<Country> countries = new ArrayList<>();
        String query = "SELECT Name, Population FROM country ORDER BY Population DESC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, N);
            ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Country country = new Country();
                country.setName(rset.getString("Name"));
                country.setPopulation(rset.getInt("Population"));
                countries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Consider a more sophisticated approach for production code
        }
        return countries;
    }

    @Override
    public ArrayList<Country> generateReport(String parameter) {
        // Assuming parameter is a numeric string specifying the top N countries
        int N;
        try {
            N = Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for parameter: " + parameter);
            return new ArrayList<>();
        }

        return getTopNPopulatedCountries(N);
    }

    @Override
    public void printReport(ArrayList<Country> countries) {
        System.out.println("\n+++++++++++++++++++++++++++++++++");
        System.out.println("  World Population Report  ");
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.printf("%-30s %-20s%n", "Country", "Population");

        for (Country country : countries) {
            System.out.printf("%-30s %-20d%n", country.getName(), country.getPopulation());
        }
    }
}
