package com.napier.semIntegrationTest;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.napier.sem.TotalPopulationReport;
import static org.junit.Assert.assertEquals;

public class TotalPopulationReportIT {
    private Connection con;
    private TotalPopulationReport report;

    @Before
    public void setUp() throws Exception {
        // Connect to the test database
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "example");
        report = new TotalPopulationReport(con);
    }

    @Test
    public void testGetTotalPopulationInWorld() {
        // Execute the method under test
        long totalPopulation = report.generateTotalPopulationData("SELECT SUM(population) AS total_population FROM country");

        assertEquals(6078749450L, totalPopulation);
    }

    @Test
    public void testGetTotalPopulationInContinent() {
        // Execute the method under test
        long totalPopulation = report.getTotalPopulationInContinent("Europe");

        assertEquals(730074600L, totalPopulation);
    }

    @Test
    public void testGetTotalPopulationInRegion() {
        // Execute the method under test
        long totalPopulation = report.getTotalPopulationInRegion("North America");

        assertEquals(309632000L, totalPopulation);
    }

    @Test
    public void testGetTotalPopulationInCountry() {
        // Execute the method under test
        long totalPopulation = report.getTotalPopulationInCountry("France");

        assertEquals(59225700L, totalPopulation);
    }

    @Test
    public void testGetTotalPopulationInDistrict() {
        // Execute the method under test
        long totalPopulation = report.getTotalPopulationInDistrict("Tasmania");

        assertEquals(126118L, totalPopulation);
    }

    @Test
    public void testGetTotalPopulationInCity() {
        // Execute the method under test
        long totalPopulation = report.getTotalPopulationInCity("Amsterdam");

        assertEquals(731200L, totalPopulation);
    }

    @After
    public void tearDown() throws Exception {
        // Close the database connection and perform any necessary cleanup
        if (con != null) {
            con.close();
        }
    }
}
