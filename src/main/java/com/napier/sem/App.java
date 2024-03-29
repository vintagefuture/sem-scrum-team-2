package com.napier.sem;

import java.sql.Connection;


public class App {

    public static void main(String[] args) {
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {

                // Generate world population report
                WorldPopulationReport worldPopulationReport = new WorldPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("World Population Report");
                System.out.println("+++++++++++++++++++++++++");
                worldPopulationReport.fetchAllCountries();

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("World Population Report Top 5");
                System.out.println("+++++++++++++++++++++++++");
                worldPopulationReport.fetchCountriesWithLimit(5);

                // Generate continent population report
                ContinentPopulationReport continentPopulationReporter = new ContinentPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Continent Population Report");
                System.out.println("+++++++++++++++++++++++++");
                continentPopulationReporter.generateAndPrintContinentReport("Asia");

                // Generate region population report
                RegionPopulationReport regionPopulationReport = new RegionPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Region Population Report");
                System.out.println("+++++++++++++++++++++++++");
                regionPopulationReport.generateRegionReport("North America");

                // Generate country population report
                CountryPopulationReport countryPopulationReporter = new CountryPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top 5 Populated Countries in Region Report");
                System.out.println("+++++++++++++++++++++++++");
                countryPopulationReporter.getTopNPopulatedCountriesInRegion("Southern and Central Asia", 5);

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top 5 Populated Countries in Continent Report");
                System.out.println("+++++++++++++++++++++++++");
                countryPopulationReporter.getTopNPopulatedCountriesInContinent("Asia", 5);

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top Capital City Report");
                System.out.println("+++++++++++++++++++++++++");
                countryPopulationReporter.getCapitalCityReportOfCountry("Indonesia");

                // Generate city population report
                CityPopulationReport cityPopulationReport = new CityPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top N Capital Cities Report in the world where N is provided by User");
                System.out.println("+++++++++++++++++++++++++");
                cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheWorldReport(5);

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top N Capital Cities Report in the region where N is provided by User");
                System.out.println("+++++++++++++++++++++++++");
                cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheRegionReport(5, "North America");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
