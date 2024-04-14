package com.napier.semTest;

import com.napier.sem.TopCapitalCitiesPopulationReport;
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

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }
    @Test
    void testGetTopCapitalCitiesInTheWorldReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Beijing");
        when(rset.getString(eq("Country"))).thenReturn("China");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCapitalCitiesPopulationReport.getTopCapitalCitiesInTheWorldReport(limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC LIMIT "+ limit ;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the capital city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Top " + limit + " Populated Capital Cities in the World"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

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
