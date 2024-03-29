package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CityPopulationReport implements PopulationReport {

    private final Connection con;

    public CityPopulationReport(Connection con) {
        this.con = con;
    }

    public void generateTopNPopulatedCapitalCitiesInTheWorldReport(int N) {

        // SQL query to select the top N populated capital cities in the world
        String query =
                "SELECT city.Name, country.Name AS Country, city.Population " +
                        "FROM city JOIN country ON city.ID = country.Capital " +
                        "ORDER BY city.Population DESC LIMIT "+ N ;
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Capital Cities in the World";
        List<String> columnNames = List.of("City Name", "Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (City city: cities) {
            List<String> row = new ArrayList<>();
            row.add(city.getName());
            row.add(city.getCountryCode());
            row.add(String.valueOf(city.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    private ArrayList<City> generateCityData(String parameter) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(parameter)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                City city = new City();
                city.setName(rset.getString("Name"));
                city.setCountryCode(rset.getString("Country"));
                city.setPopulation(rset.getInt("Population"));
                cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Proper error handling is recommended
        }
        return cities;
    }

    @Override
    public void printReport(String title, List<String> columnNames, List<List<String>> rows) {
        // Print report title
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));

        // Print column headers
        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
        System.out.println();

        // Print row data
        for (List<String> row : rows) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
