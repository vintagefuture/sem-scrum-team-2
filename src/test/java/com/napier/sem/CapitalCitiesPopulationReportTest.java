package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link CapitalCitiesPopulationReport} class.
 */
@ExtendWith(MockitoExtension.class)
public class CapitalCitiesPopulationReportTest {

    /** Output stream for capturing printed content. */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /** Mocked database connection. */
    @Mock
    private Connection con;

    /** Mocked prepared statement. */
    @Mock
    private PreparedStatement stmt;

    /** Mocked result set. */
    @Mock
    private ResultSet rset;

    /** Instance of the class under test. */
    @InjectMocks
    private CapitalCitiesPopulationReport capitalCityPopulationReport;

    /** Argument captor for SQL queries. */
    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    /**
     * Sets up the test environment before each test method is executed.
     * @throws Exception if setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    /**
     * Tests the generation of capital city data.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testGenerateCapitalCityData() throws Exception {
        String query = "SELECT ci.Name, c.Name AS Country, ci.Population\n" +
                "FROM city ci\n" +
                "JOIN country c ON c.Capital = ci.id\n" +
                "ORDER BY Population DESC";

        // Mock result set
        when(rset.next()).thenReturn(true).thenReturn(false);
        when(rset.getString("Name")).thenReturn("CityName");
        when(rset.getString("Country")).thenReturn("CountryName");
        when(rset.getInt("Population")).thenReturn(1000);

        CapitalCitiesPopulationReport report = new CapitalCitiesPopulationReport(con);
        ArrayList<City> cities = report.generateCapitalCityData(query);

        // Verify that city data is correctly retrieved from the result set
        assertEquals(1, cities.size());
        assertEquals("CityName", cities.get(0).getName());
        assertEquals("CountryName", cities.get(0).getCountry());
        assertEquals(1000, cities.get(0).getPopulation());
    }

    /**
     * Tests the retrieval of capital cities report of a continent.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetCapitalCitiesReportOfContinent() throws Exception {
        // Mock data
        when(rset.next()).thenReturn(true).thenReturn(false);
        when(rset.getString("Name")).thenReturn("City1");
        when(rset.getString("Country")).thenReturn("Country1");
        when(rset.getInt("Population")).thenReturn(1000000);

        String continentName = "ExampleContinent";
        String query = "SELECT c.Name AS Name, ct.Name AS Country, c.Population AS Population\n" +
                "FROM country ct " +
                "JOIN city c ON ct.Capital = c.ID " +
                "WHERE ct.Continent = '" + continentName + "' " +
                "ORDER BY Population DESC";

        // Call the method under test
        capitalCityPopulationReport.getCapitalCitiesReportOfContinent(continentName);

        // Verify that the expected query is executed
        verify(con).prepareStatement(query);
        verify(stmt).executeQuery();
    }

    /**
     * Tests the retrieval of capital city report of a region.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testGetCapitalCityReportOfRegion() throws Exception {
        String regionName = "Caribbean";
        mockResultSetForCapitalCity();

        capitalCityPopulationReport.getCapitalCityReportOfRegion(regionName);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "ct.Region = '" + regionName + "'");
    }

    /**
     * Asserts that the executed SQL contains the expected string.
     * @param executedSQL the executed SQL query
     * @param expected the expected string in the SQL query
     */
    private void assertSqlContains(String executedSQL, String expected) {
        assertTrue(executedSQL.contains(expected),
                "Expected SQL to contain: " + expected + ", but was: " + executedSQL);
    }

    /**
     * Mocks the result set for capital city.
     * @throws Exception if an error occurs during mocking
     */
    private void mockResultSetForCapitalCity() throws Exception {
        when(rset.next()).thenReturn(true, false); // Simulate one row returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }
}
