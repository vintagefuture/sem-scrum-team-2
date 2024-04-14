package com.napier.semTest;

import com.napier.sem.TopCountriesPopulationReport;
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

    @BeforeEach
    void setUp() throws Exception {
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    @Test
    void testFetchCountriesWithLimit() throws Exception {
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

        topCountriesPopulationReport.getTopPopulatedCountriesInTheWorld(N);

        // Verify `printReport` was called with the correct parameters

        verify(con).prepareStatement(contains("LIMIT " + N));
        verify(stmt).executeQuery();
        verify(rset, atLeast(N)).next();
    }

    @Test
    void testGetTopPopulatedCountriesInContinent() throws Exception {
        String continent = "Asia";
        int N = 5;
        mockResultSetForCountries();

        topCountriesPopulationReport.getTopPopulatedCountriesInContinent(continent, N);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Continent = '" + continent + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    @Test
    void testGetTopPopulatedCountriesInRegion() throws Exception {
        String region = "Western Europe";
        int N = 3;
        mockResultSetForCountries();

        topCountriesPopulationReport.getTopPopulatedCountriesInRegion(region, N);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Region = '" + region + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    private void mockResultSetForCountries() throws Exception {
        when(rset.next()).thenReturn(true, true, true, false); // Simulate three rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }

    private void assertSqlContains(String executedSQL, String expected) {
        assertTrue(executedSQL.contains(expected),
                "Expected SQL to contain: " + expected + ", but was: " + executedSQL);
    }

}
