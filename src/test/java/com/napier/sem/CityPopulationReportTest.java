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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private CityPopulationReport cityPopulationReport;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    @Test
    void testGenerateTopNPopulatedCapitalCitiesInTheWorldReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Beijing");
        when(rset.getString(eq("Country"))).thenReturn("China");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheWorldReport(limit);

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
        assertTrue(output.contains("City Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGenerateTopNPopulatedCapitalCitiesInTheRegionReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Washington DC");
        when(rset.getString(eq("Country"))).thenReturn("USA");
        when(rset.getInt("Population")).thenReturn(100000);

        String region = "North America";
        int limit = 5;
        cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheRegionReport(limit, region);

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
        assertTrue(output.contains("City Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

}

