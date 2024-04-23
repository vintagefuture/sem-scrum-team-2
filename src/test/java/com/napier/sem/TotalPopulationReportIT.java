package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for TotalPopulationReport class.
 */
public class TotalPopulationReportIT {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private Connection con;
    private TotalPopulationReport report;

    /**
     * Setup method executed before each test.
     *
     * @throws Exception if any setup operation fails
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        // Connect to the database
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");

        // Initialize TotalPopulationReport with database connection
        report = new TotalPopulationReport(con);
    }

    /**
     * Tear down method executed after each test.
     *
     * @throws Exception if any tear down operation fails
     */
    @AfterEach
    public void tearDown() throws Exception {
        // Close the database connection
        con.close();
    }

    /**
     * Test case for getting total population of the world.
     */
    @Test
    public void testGetTotalPopulationInWorld() {
        // Execute the method under test
        report.getTotalPopulationInWorld();

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of the world\n" +
                "---------------------------\n" +
                "6078749450";

        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Test case for getting total population of a continent.
     */
    @Test
    public void testGetTotalPopulationInContinent() {
        // Execute the method under test
        report.getTotalPopulationInContinent("Europe");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of continent Europe\n" +
                "----------------------------------\n" +
                "730074600";

        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Test case for getting total population of a region.
     */
    @Test
    public void testGetTotalPopulationInRegion() {
        // Execute the method under test
        report.getTotalPopulationInRegion("North America");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of region North America\n" +
                "--------------------------------------\n" +
                "309632000";

        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Test case for getting total population of a country.
     */
    @Test
    public void testGetTotalPopulationInCountry() {
        // Execute the method under test
        report.getTotalPopulationInCountry("France");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of country France\n" +
                "--------------------------------\n" +
                "59225700";

        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Test case for getting total population of a district.
     */
    @Test
    public void testGetTotalPopulationInDistrict() {
        // Execute the method under test
        report.getTotalPopulationInDistrict("Tasmania");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of district Tasmania\n" +
                "-----------------------------------\n" +
                "126118";

        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Test case for getting total population of a city.
     */
    @Test
    public void testGetTotalPopulationInCity() {
        // Execute the method under test
        report.getTotalPopulationInCity("Amsterdam");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of city Amsterdam\n" +
                "--------------------------------\n" +
                "731200";

        assertTrue(output.contains(expectedOutput));
    }
}
