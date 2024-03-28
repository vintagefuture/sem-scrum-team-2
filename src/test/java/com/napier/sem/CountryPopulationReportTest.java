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

@ExtendWith(MockitoExtension.class)
public class CountryPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private CountryPopulationReport countryPopulationReport;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    void setUp() throws Exception {
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    @Test
    void testGetTopNPopulatedCountriesInContinent() throws Exception {
        String continent = "Asia";
        int N = 5;
        mockResultSetForCountries();

        countryPopulationReport.getTopNPopulatedCountriesInContinent(continent, N);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Continent = '" + continent + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    @Test
    void testGetTopNPopulatedCountriesInRegion() throws Exception {
        String region = "Western Europe";
        int N = 3;
        mockResultSetForCountries();

        countryPopulationReport.getTopNPopulatedCountriesInRegion(region, N);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "Region = '" + region + "'");
        assertSqlContains(executedSQL, "LIMIT " + N);
    }

    @Test
    void testGetCapitalCityReportOfCountry() throws Exception {
        String countryName = "France";
        mockResultSetForCapitalCity();

        countryPopulationReport.getCapitalCityReportOfCountry(countryName);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "ct.Name = '" + countryName + "'");
    }

    private void mockResultSetForCountries() throws Exception {
        when(rset.next()).thenReturn(true, true, true, false); // Simulate three rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }

    private void mockResultSetForCapitalCity() throws Exception {
        when(rset.next()).thenReturn(true, false); // Simulate one row returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }

    private void assertSqlContains(String executedSQL, String expected) {
        assertTrue(executedSQL.contains(expected),
                "Expected SQL to contain: " + expected + ", but was: " + executedSQL);
    }

}
