package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


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


    public static void main(String[] args) {
        
        System.out.println("Enter the number of top populated countries to display: ");
        // set N input value
        int N = 5; 
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Use WorldPopulationReport class to generate and display the report
        WorldPopulationReport report = new WorldPopulationReport(a.con);

        // Generate and display country population report
        List<Country> countries = report.getTopNPopulatedCountries(N);

        if (countries != null && !countries.isEmpty()) {
            System.out.println("Top " + N + " Populated Countries Report:");
            for (Country country : countries) {
                System.out.println("Country: " + country.name + ", Population: " + country.population);
            }
        } else {
            System.out.println("No countries found or failed to generate the report.");
        }


        a.disconnect();

    }

}