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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CapitalCitiesPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private CapitalCitiesPopulationReport capitalCityPopulationReport;

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
    void testGetCapitalCityReportOfRegion() throws Exception {
        String regionName = "Caribbean";
        mockResultSetForCapitalCity();

        capitalCityPopulationReport.getCapitalCityReportOfRegion(regionName);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertSqlContains(executedSQL, "ct.Region = '" + regionName + "'");
    }
    private void assertSqlContains(String executedSQL, String expected) {
        assertTrue(executedSQL.contains(expected),
                "Expected SQL to contain: " + expected + ", but was: " + executedSQL);
    }
    private void mockResultSetForCapitalCity() throws Exception {
        when(rset.next()).thenReturn(true, false); // Simulate one row returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt(any())).thenReturn(100000);
    }
}
