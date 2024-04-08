package com.napier.sem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public class CitiesReportTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetCitiesPopulationReportInTheWorld() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn("City1");
        when(mockResultSet.getString("Country")).thenReturn("Country1");
        when(mockResultSet.getString("District")).thenReturn("District1");
        when(mockResultSet.getInt("Population")).thenReturn(1000000);

        CitiesReport citiesReport = new CitiesReport(mockConnection);
        citiesReport.getCitiesPopulationReportInTheWorld();

        // Verify that executeQuery is called
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetCitiesPopulationInContinent() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn("City1");
        when(mockResultSet.getString("Country")).thenReturn("Country1");
        when(mockResultSet.getString("District")).thenReturn("District1");
        when(mockResultSet.getInt("Population")).thenReturn(1000000);

        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "WHERE continent = 'Europe' " +
                "ORDER BY Population DESC";

        CitiesReport citiesReport = new CitiesReport(mockConnection);

        // Call the method under test
        citiesReport.getCitiesPopulationInContinent("Europe");

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }
    

    @Test
    public void testGetCitiesPopulationReportInDistrict() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn("City1");
        when(mockResultSet.getString("Country")).thenReturn("Country1");
        when(mockResultSet.getString("District")).thenReturn("District1");
        when(mockResultSet.getInt("Population")).thenReturn(1000000);

        CitiesReport citiesReport = new CitiesReport(mockConnection);

        citiesReport.getCitiesPopulationReportInDistrict("District1");

        // Method under test
        citiesReport.getCitiesPopulationReportInDistrict("District1");

        // Capture and verify the SQL query executed by the method
        verify(mockConnection).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();


        String expectedQuery  = "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "WHERE ci.District = 'District1' " +
                "ORDER BY Population DESC";

        // Assert that the captured SQL query matches the expected SQL query
        assertEquals(expectedQuery, executedSQL);
        
        verify(mockPreparedStatement).executeQuery();
    }


   @Test
    public void testGetCitiesPopulationInRegion() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn("City1");
        when(mockResultSet.getString("Country")).thenReturn("Country1");
        when(mockResultSet.getString("District")).thenReturn("District1");
        when(mockResultSet.getInt("Population")).thenReturn(1000000);

        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "WHERE region = 'ExampleRegion' " +
                "ORDER BY Population DESC";

        CitiesReport citiesReport = new CitiesReport(mockConnection);

        // Call the method under test
        citiesReport.getCitiesPopulationInRegion("ExampleRegion");

        // Verify that the expected query is executed
        verify(mockConnection).prepareStatement(query);
        verify(mockPreparedStatement).executeQuery();
    }


    @Test
    public void testGenerateCityData() throws Exception {
        // Mock data
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn("City1");
        when(mockResultSet.getString("Country")).thenReturn("Country1");
        when(mockResultSet.getString("District")).thenReturn("District1");
        when(mockResultSet.getInt("Population")).thenReturn(1000000);

        String query = "SELECT ci.Name AS Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city ci " +
                "JOIN country c ON c.Code = ci.CountryCode " +
                "ORDER BY Population DESC";

        CitiesReport citiesReport = new CitiesReport(mockConnection);
        ArrayList<City> cities = citiesReport.generateCityData(query);

        // Verify that data is retrieved correctly
        assert cities.size() == 1;
        assert cities.get(0).getName().equals("City1");
        assert cities.get(0).getCountry().equals("Country1");
        assert cities.get(0).getDistrict().equals("District1");
        assert cities.get(0).getPopulation() == 1000000;
    }
}



