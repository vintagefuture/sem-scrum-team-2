package com.napier.sem;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for TotalPopulationReport class using Mockito.
 */
public class TotalPopulationReportTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    /**
     * Setup method executed before each test.
     *
     * @throws Exception if any setup operation fails
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Mock objects initialization
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Define behavior for mocked objects
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    /**
     * Test case for getting total population of the world.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInWorld() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        // Instantiate TotalPopulationReport with mocked connection
        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInWorld();

        // Verify that executeQuery is called
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for getting total population of a continent.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInContinent() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String continent = "Europe";
        String query = "SELECT SUM(population) AS total_population FROM country\n" +
                "WHERE continent = '" + continent + "'";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInContinent(continent);

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for getting total population of a region.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInRegion() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String region = "ExampleRegion";
        String query = "SELECT SUM(population) AS total_population FROM country\n" +
                "WHERE region = '" + region + "'";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInRegion(region);

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for getting total population of a country.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInCountry() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String country = "ExampleCountry";
        String query = "SELECT population AS total_population FROM country WHERE name = '" + country + "'";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInCountry(country);

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for getting total population of a district.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInDistrict() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String district = "ExampleDistrict";
        String query = "SELECT SUM(population) AS total_population\n" +
                "FROM city\n" +
                "WHERE district = '" + district + "'";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInDistrict(district);

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for getting total population of a city.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGetTotalPopulationInCity() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String city = "ExampleCity";
        String query = "SELECT population AS total_population\n" +
                "FROM city\n" +
                "WHERE name = '" + city + "'";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInCity(city);

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }

    /**
     * Test case for generating total population data from a query.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testGenerateTotalPopulationData() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        String query = "SELECT SUM(population) AS total_population FROM country";

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        long totalPopulation = totalPopulationReport.generateTotalPopulationData(query);

        // Verify that the method returns the expected total population
        assertEquals(1000000L, totalPopulation);
    }
}
