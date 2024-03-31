package com.napier.sem;

import java.sql.Connection;


public class App {
    /**
     * Main entry point for the application
     * Contains the call to each report
     */
    public static void main(String[] args) {
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {

                // All countries in the world organised by largest population to smallest
                WorldPopulationReport worldPopulationReport = new WorldPopulationReport(con);
                worldPopulationReport.fetchAllCountries();

                // All the countries in a continent organised by largest population to smallest
                ContinentPopulationReport continentPopulationReporter = new ContinentPopulationReport(con);
                continentPopulationReporter.generateAndPrintContinentReport("Asia");

                // All the countries in a region organised by largest population to smallest
                RegionPopulationReport regionPopulationReport = new RegionPopulationReport(con);
                regionPopulationReport.generateRegionReport("North America");

                // The top N populated countries in the world where N is provided by the user
                worldPopulationReport.fetchCountriesWithLimit(5);

                // The top N populated countries in a continent where N is provided by the user
                CountryPopulationReport countryPopulationReporter = new CountryPopulationReport(con);
                countryPopulationReporter.getTopNPopulatedCountriesInContinent("Asia", 5);

                // The top N populated countries in a region where N is provided by the user
                countryPopulationReporter.getTopNPopulatedCountriesInRegion("Southern and Central Asia", 5);

                // All the cities in a continent organised by largest population to smallest

                // All the cities in a region organised by largest population to smallest

                // All the cities in the world organised by largest population to smallest

                // All the cities in a country organised by largest population to smallest

                // All the cities in a district organised by largest population to smallest

                // The top N populated cities in the world where N is provided by the user

                // The top N populated cities in a continent where N is provided by the user

                // The top N populated cities in a region where N is provided by the user

                // The top N populated cities in a country where N is provided by the user

                // The top N populated cities in a district where N is provided by the user

                // All the capital cities in the world organised by largest population to smallest

                // All the capital cities in a continent organised by largest population to smallest

                // All the capital cities in a region organised by largest to smallest
                CapitalCityPopulationReport capitalCityPopulationReport = new CapitalCityPopulationReport(con);
                capitalCityPopulationReport.getCapitalCityReportOfRegion("Caribbean");

                // The top N populated capital cities in the world where N is provided by the user
                CityPopulationReport cityPopulationReport = new CityPopulationReport(con);
                cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheWorldReport(5);

                // The top N populated capital cities in a continent where N is provided by the user
                cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheContinentReport(3, "Asia");

                // The top N populated capital cities in a region where N is provided by the user
                cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheRegionReport(5, "North America");

                // The population of people, people living in cities, and people not living in cities in each continent
                cityPopulationReport.generatePopulationInCitiesVSNonCityByContinent();

                // The population of people, people living in cities, and people not living in cities in each region
                cityPopulationReport.generatePopulationInCitiesVSNonCityByRegion();

                // The population of people, people living in cities, and people not living in cities in each country
                cityPopulationReport.generatePopulationInCitiesVSNonCityByCountry();

                // The population of the world

                // The population of a continent

                // The population of a region

                // The population of a country

                // The population of a district

                // The population of a city

                // Total number of people, with percentage of world population, who speak Chinese, English, Hindi, Spanish, Arabic, from greatest number to smallest

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
