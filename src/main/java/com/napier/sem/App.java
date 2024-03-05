package com.napier.sem;

import java.sql.Connection;
import java.util.ArrayList;


public class App {

    public static void main(String[] args) {
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {
                ContinentPopulationReport populationReporterContinent = new ContinentPopulationReport(con);
                ArrayList<Country> countriesInContinent = populationReporterContinent.generateReport("Asia");
                populationReporterContinent.printReport(countriesInContinent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
