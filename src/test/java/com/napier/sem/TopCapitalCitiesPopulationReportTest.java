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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link TopCapitalCitiesPopulationReport} class.
 */
@ExtendWith(MockitoExtension.class)
public class TopCapitalCitiesPopulationReportTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rset;
    @InjectMocks
    private TopCapitalCitiesPopulationReport topCapitalCitiesPopulationReport;
    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    /**
     * Sets up the test environment before each test method.
     *
     * @throws Exception if an error occurs during setup
     */
    @BeforeEach
    void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        // Mock behavior of the PreparedStatement and ResultSet
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    /**
     * Test case for generating a report on the top capital cities in the world.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTopCapitalCitiesInTheWorldReport() throws Exception {
        // Set up mock behavior for ResultSet
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Beijing");
        when(rset.getString(eq("Country"))).thenReturn("China");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCapitalCitiesPopulationReport.getTopCapitalCitiesInTheWorldReport(limit);

        // Verify the SQL query executed
        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC LIMIT "+ limit ;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the capital city.");

        // Verify the output contains expected values
        String output = outContent.toString();
        assertTrue(output.contains("Top " + limit + " Populated Capital Cities in the World"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

    /**
     * Test case for generating a report on the top capital cities in a continent.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTopCapitalCitiesInTheContinentReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Beijing");
        when(rset.getString(eq("Country"))).thenReturn("China");
        when(rset.getInt("Population")).thenReturn(100000);

        String continent = "Asia";
        int limit = 5;
        topCapitalCitiesPopulationReport.getTopCapitalCitiesInTheContinentReport(limit, continent);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Continent='" + continent + "' " +
                "ORDER BY city.Population DESC LIMIT "+ limit ;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the capital city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Top " + limit + " Populated Capital Cities in continent " + continent),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

    /**
     * Test case for generating a report on the top capital cities in a region.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTopCapitalCitiesInTheRegionReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Washington DC");
        when(rset.getString(eq("Country"))).thenReturn("USA");
        when(rset.getInt("Population")).thenReturn(100000);

        String region = "North America";
        int limit = 5;
        topCapitalCitiesPopulationReport.getTopCapitalCitiesInTheRegionReport(limit, region);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Region='" + region + "' " +
                "ORDER BY city.Population DESC LIMIT "+ limit ;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the capital city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Top " + limit + " Populated Capital Cities in region " + region),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }
}
