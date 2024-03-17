package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class CountryPopulationReport implements PopulationReport {

    private final Connection con;

    public CountryPopulationReport(Connection con) {
        this.con = con;
    }

    public void getTopNPopulatedCountriesInContinent(String continent, int N) {
        String query = "SELECT Code, Name, Continent, Region, Population FROM country WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + N;
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + continent;
        List<String> columnNames = List.of("Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getCode());
            row.add(country.getName());
            row.add(country.getContinent());
            row.add(country.getRegion());
            row.add(String.valueOf(country.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    public void getTopNPopulatedCountriesInRegion(String region, int N) {
        String query = "SELECT Code, Name, Continent, Region, Population FROM country WHERE Region = '" + region + "' ORDER BY Population DESC LIMIT " + N;
        ArrayList<Country> countries = generateCountryData(query);

        // Prepare data for printing
        String title = "Top " + N + " Populated Countries in " + region;
        List<String> columnNames = List.of("Country", "Population");
        List<List<String>> rows = new ArrayList<>();

        for (Country country : countries) {
            List<String> row = new ArrayList<>();
            row.add(country.getCode());
            row.add(country.getName());
            row.add(country.getContinent());
            row.add(country.getRegion());
            row.add(String.valueOf(country.getPopulation()));
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    public void getCapitalCityReportOfCountry(String country) {
        String query = "SELECT ct.Name AS CountryName, c.Name AS CapitalCity, c.Population AS CapitalPopulation, c.District AS District\n" +
                "FROM Country ct\n" +
                "JOIN City c ON ct.Capital = c.ID\n" +
                "WHERE ct.Name = '"+ country +"';";
        ArrayList<City> cities = generateCityData(query);

        // Prepare data for printing
        String title = "Capital city report of " + country;
        List<String> columnNames = List.of("Country Name", "City Name", "Capital Population", "District");
        List<List<String>> rows = new ArrayList<>();

        for (City city : cities) {
            List<String> row = new ArrayList<>();
            row.add(country);
            row.add(city.getName());
            row.add(String.valueOf(city.getPopulation()));
            row.add(city.getDistrict());
            rows.add(row);
        }

        printReport(title, columnNames, rows);
    }

    public ArrayList<Country> generateCountryData(String parameter) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(parameter)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Country country = new Country();
                country.setCode(rset.getString("Code"));
                country.setName(rset.getString("Name"));
                country.setContinent(rset.getString("Continent"));
                country.setRegion(rset.getString("Region"));
                country.setPopulation(rset.getInt("total_population"));
                countries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Proper error handling is recommended
        }
        return countries;
    }

    public ArrayList<City> generateCityData(String query) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                City city = new City();
                city.setName(rset.getString("Name"));
                city.setCountryCode(rset.getString("CountryCode"));
                city.setDistrict(rset.getString("District"));
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
