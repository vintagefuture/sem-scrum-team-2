package com.napier.sem;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public class TotalPopulationReportTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetTotalPopulationInWorld() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("total_population")).thenReturn(1000000L);

        TotalPopulationReport totalPopulationReport = new TotalPopulationReport(mockConnection);
        totalPopulationReport.getTotalPopulationInWorld();

        // Verify that executeQuery is called
        verify(mockPreparedStatement).executeQuery();
    }

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
