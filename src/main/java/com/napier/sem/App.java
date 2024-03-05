package com.napier.sem;

import java.sql.Connection;
import java.util.ArrayList;


public class App {

    public static void main(String[] args) {
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {
                ContinentPopulationReport populationReporterContinent = new ContinentPopulationReport(con);
                populationReporterContinent.generateAndPrintContinentReport("Asia");

                CountryPopulationReport populationReporterCountry = new CountryPopulationReport(con);
                populationReporterCountry.getTopNPopulatedCountriesInRegion("Southern and Central Asia", 5);
                populationReporterCountry.getTopNPopulatedCountriesInContinent("Asia", 5);
                populationReporterCountry.getCapitalCityReportOfCountry("Indonesia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
