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
                ContinentPopulationReport populationReporterContinent = new ContinentPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Continent Population Report");
                System.out.println("+++++++++++++++++++++++++");
                populationReporterContinent.generateAndPrintContinentReport("Asia");

                // Generate region population report
                RegionPopulationReport regionPopulationReport = new RegionPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Region Population Report");
                System.out.println("+++++++++++++++++++++++++");
                regionPopulationReport.generateRegionReport("North America");

                // Generate country population report
                CountryPopulationReport populationReporterCountry = new CountryPopulationReport(con);
                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top 5 Populated Countries in Region Report");
                System.out.println("+++++++++++++++++++++++++");
                populationReporterCountry.getTopNPopulatedCountriesInRegion("Southern and Central Asia", 5);

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top 5 Populated Countries in Continent Report");
                System.out.println("+++++++++++++++++++++++++");
                populationReporterCountry.getTopNPopulatedCountriesInContinent("Asia", 5);

                System.out.println("+++++++++++++++++++++++++");
                System.out.println("Top Capital City Report");
                System.out.println("+++++++++++++++++++++++++");
                populationReporterCountry.getCapitalCityReportOfCountry("Indonesia");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
