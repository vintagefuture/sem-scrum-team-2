package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TopCountriesPopulationReport} class.
 */
@ExtendWith(MockitoExtension.class)
public class TopCountriesPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private TopCountriesPopulationReport topCountriesPopulationReport;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    /**
     * Sets up the test environment before each test method.
     *
     * @throws Exception if an error occurs during setup
     */
    @BeforeEach
    void setUp() throws Exception {
        // Mock behavior of the PreparedStatement and ResultSet
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    /**
     * Test case for fetching top populated countries in the world with a specified limit.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testFetchCountriesWithLimit() throws Exception {
        // Prepare test data
        int N = 5;
        when(con.prepareStatement(contains("LIMIT " + N))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
        // Mock the ResultSet to simulate database behavior
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate 5 rows returned, then end
        when(rset.getString("Code")).thenReturn("Code1");
        when(rset.getString("Name")).thenReturn("CountryName1");
        when(rset.getString("Continent")).thenReturn("Continent1");
        when(rset.getString("Region")).thenReturn("Region1");
        when(rset.getInt("Population")).thenReturn(1000);
        when(rset.getString("Capital")).thenReturn("CapitalName1");

        // Call the method under test
        topCountriesPopulationReport.getTopPopulatedCountriesInTheWorld(N);

        // Verify that the correct SQL query was executed and ResultSet was processed
        verify(con).prepareStatement(contains("LIMIT " + N));
        verify(stmt).executeQuery();
        verify(rset, atLeast(N)).next();
    }

    /**
     * Test case for fetching top populated countries in a continent.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTopPopulatedCountriesInContinent() throws Exception {
        // Prepare test data
        String continent = "Asia";
        int N = 5;
        mockResultSetForCountries();

        // Call the method under test
        topCountriesPopulationReport.getTopPopulatedCountriesInContinent(continent, N);

        // Verify that the correct SQL query was executed with the continent and limit
        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Continent = '" + continent + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    /**
     * Test case for fetching top populated countries in a region.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTopPopulatedCountriesInRegion() throws Exception {
        // Prepare test data
        String region = "Western Europe";
        int N = 3;
        mockResultSetForCountries();

        // Call the method under test
        topCountriesPopulationReport.getTopPopulatedCountriesInRegion(region, N);

        // Verify that the correct SQL query was executed with the region and limit
        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Region = '" + region + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    /**
     * Mocks the ResultSet to simulate countries data.
     *
     * @throws Exception if an error occurs during execution
     */
    private void mockResultSetForCountries() throws Exception {
        when(rset.next()).thenReturn(true, true, true, false); // Simulate three rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }

    /**
     * Asserts that the SQL query contains the expected substring.
     *
     * @param executedSQL The SQL query that was executed
     * @param expected    The expected substring to be present in the SQL query
     */
    private void assertSqlContains(String executedSQL, String expected) {
        assertTrue(executedSQL.contains(expected),
                "Expected SQL to contain: " + expected + ", but was: " + executedSQL);
    }
}
