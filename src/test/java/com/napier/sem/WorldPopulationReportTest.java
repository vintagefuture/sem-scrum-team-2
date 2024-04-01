package com.napier.sem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private CountriesPopulationReport countriesPopulationReport;

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

        countriesPopulationReport.getWorldReport();

        // Verify `printReport` was called with the correct parameters
        // This requires modifying `WorldPopulationReport` to make `printReport` verifiable or inspecting console output

        verify(con).prepareStatement(anyString());
        verify(stmt).executeQuery();
        verify(rset, atLeastOnce()).next();
    }

    @Test
    void testFetchCountriesWithLimit() throws Exception {
        int N = 5; // Example limit
        when(con.prepareStatement(contains("LIMIT " + N))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
        // Mock the ResultSet to simulate database behavior
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate 5 rows returned, then end
        when(rset.getString("c.Code")).thenReturn("Code1");
        when(rset.getString("c.Name")).thenReturn("CountryName1");
        when(rset.getString("Continent")).thenReturn("Continent1");
        when(rset.getString("Region")).thenReturn("Region1");
        when(rset.getInt("c.Population")).thenReturn(1000);
        when(rset.getString("ci.Name")).thenReturn("CapitalName1");

        countriesPopulationReport.fetchCountriesWithLimit(N);

        // Verify `printReport` was called with the correct parameters
        // This step requires manual verification or adjusting the WorldPopulationReport class to be more test-friendly

        verify(con).prepareStatement(contains("LIMIT " + N));
        verify(stmt).executeQuery();
        verify(rset, atLeast(N)).next();
    }
}
