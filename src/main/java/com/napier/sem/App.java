package com.napier.sem;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;


public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static void printReport(ArrayList<Country> countries) {

        // Print header
        System.out.println(String.format("%-5s %-40s %-20s %-30s %-20s %-20s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        System.out.println();

        for (Country country : countries) {
            String country_string = String.format("%-5s %-40s %-20s %-30s %-20s %-20s",
                    country.code, country.name, country.continent, country.region, country.population, country.capital);
            System.out.println(country_string);
        }
    }

    public static void main(String[] args) {

        App a = new App();

        // Connect to database
        a.connect();

        // Generate world population report
        WorldPopulationReport populationReporterWorld = new WorldPopulationReport(a.con);
        ArrayList<Country> countriesInWorld = populationReporterWorld.getTopNPopulatedCountries(null);

        // Print world population report
        System.out.println();
        System.out.println("+++++++++++++++++++++++++");
        System.out.println("World Population Report");
        System.out.println("+++++++++++++++++++++++++");
        printReport(countriesInWorld);

        // Generate continent population report
        ContinentPopulationReport populationReporterContinent = new ContinentPopulationReport(a.con);
        ArrayList<Country> countriesInContinent = populationReporterContinent.generateReport("Asia");

        // Print continent population report
        System.out.println();
        System.out.println("+++++++++++++++++++++++++");
        System.out.println("Continent Population Report");
        System.out.println("+++++++++++++++++++++++++");
        printReport(countriesInContinent);

        // Generate region population report
        RegionPopulationReport populationReporterRegion = new RegionPopulationReport(a.con);
        ArrayList<Country> countriesInRegion = populationReporterRegion.generateReport("North America");

        // Print region population report
        System.out.println();
        System.out.println("+++++++++++++++++++++++++");
        System.out.println("Region Population Report");
        System.out.println("+++++++++++++++++++++++++");
        printReport(countriesInRegion);


        // Generate world population report
        WorldPopulationReport populationReporterWorldLimited = new WorldPopulationReport(a.con);
        ArrayList<Country> countriesInWorldLimited = populationReporterWorld.getTopNPopulatedCountries(5);

        // Print world population report
        System.out.println();
        System.out.println("+++++++++++++++++++++++++");
        System.out.println("World Population Report Top 5");
        System.out.println("+++++++++++++++++++++++++");
        printReport(countriesInWorldLimited);

        // Disconnect from database
        a.disconnect();

    }

}