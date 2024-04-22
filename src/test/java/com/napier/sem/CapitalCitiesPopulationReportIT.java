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
 * Integration test class for CapitalCitiesPopulationReport.
 */
public class CapitalCitiesPopulationReportIT {

    /** Stream to capture the output. */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /** Database connection object. */
    private Connection con;

    /** CapitalCitiesPopulationReport instance. */
    private CapitalCitiesPopulationReport report;

    /**
     * Sets up the test environment before each test method is executed.
     * @throws Exception if setup fails
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");

        report = new CapitalCitiesPopulationReport(con);
    }

    /**
     * Cleans up the test environment after each test method is executed.
     * @throws Exception if tear down fails
     */
    @AfterEach
    public void tearDown() throws Exception {
        con.close();
    }

    /**
     * Tests the retrieval of capital cities population report in the world.
     */
    @Test
    void testGetCitiesPopulationReportInTheWorld() {
        report.getCapitalCitiesReportOfTheWorld();

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "All the capital cities in the world organised by largest population to smallest\n" +
                "-------------------------------------------------------------------------------\n" +
                "Name                               Country                                Population  \n" +
                "Seoul                              South Korea                            9981619     \n" +
                "Jakarta                            Indonesia                              9604900     \n" +
                "Ciudad de México                   Mexico                                 8591309     \n" +
                "Moscow                             Russian Federation                     8389200     \n" +
                "Tokyo                              Japan                                  7980230     \n" +
                "Peking                             China                                  7472000     \n" +
                "London                             United Kingdom                         7285000     \n" +
                "Cairo                              Egypt                                  6789479     \n" +
                "Teheran                            Iran                                   6758845     \n" +
                "Lima                               Peru                                   6464693     \n";

        assertTrue(output.contains(expectedOutput));
    }
}
