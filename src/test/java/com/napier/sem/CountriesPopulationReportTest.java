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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountriesPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private CountriesPopulationReport countriesPopulationReport;

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
    void testFetchAllCountries() throws Exception {
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
        // Mock the ResultSet to simulate database behavior
        when(rset.next()).thenReturn(true, false); // Simulate one row returned, then end
        when(rset.getString("c.Code")).thenReturn("Code1");
        when(rset.getString("c.Name")).thenReturn("CountryName1");
        when(rset.getString("Continent")).thenReturn("Continent1");
        when(rset.getString("Region")).thenReturn("Region1");
        when(rset.getInt("c.Population")).thenReturn(1000);
        when(rset.getString("ci.Name")).thenReturn("CapitalName1");

        countriesPopulationReport.getCountriesPopulationInTheWorldReport();

        // Verify `printReport` was called with the correct parameters
        // This requires modifying `WorldPopulationReport` to make `printReport` verifiable or inspecting console output

        verify(con).prepareStatement(anyString());
        verify(stmt).executeQuery();
        verify(rset, atLeastOnce()).next();
    }

    @Test
    void testGenerateAndPrintContinentReport() throws Exception {
        String continent = "Asia";
        mockResultSetForCountries();

        countriesPopulationReport.getCountriesPopulationInContinentReport(continent);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertTrue(executedSQL.contains("WHERE continent='" + continent + "'"),
                "The SQL should contain the correct WHERE clause for the continent.");

        // Verify the output contains the expected report structure
        verifyOutputContains(continent + " Population Report");
        verifyOutputContains("Code\tName\tContinent\tRegion\tPopulation\tCapital");
    }

    @Test
    void testGenerateRegionReport() throws Exception {
        String region = "Europe";
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt("c.Population")).thenReturn(100000);

        countriesPopulationReport.getCountriesPopulationInRegionReport(region);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        assertTrue(executedSQL.contains("WHERE region='" + region + "'"),
                "The SQL should contain the correct WHERE clause for the region.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains(region + " Population Report"), "Output should contain the region report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Code\tName\tContinent\tRegion\tPopulation\tCapital"),
                "Output should contain the correct column headers.");
    }

    private void mockResultSetForCountries() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }

    private void verifyOutputContains(String expectedContent) {
        assertTrue(outContent.toString().replaceAll("\\s\\s+", "\t").contains(expectedContent),
                "The output should contain: " + expectedContent);
    }
}
