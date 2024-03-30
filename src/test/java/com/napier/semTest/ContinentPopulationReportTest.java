package com.napier.semTest;

import com.napier.sem.ContinentPopulationReport;
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
public class ContinentPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private ContinentPopulationReport continentPopulationReport;

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
    void testGenerateAndPrintContinentReport() throws Exception {
        String continent = "Asia";
        mockResultSetForCountries();

        continentPopulationReport.generateAndPrintContinentReport(continent);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();

        assertTrue(executedSQL.contains("WHERE continent='" + continent + "'"),
                "The SQL should contain the correct WHERE clause for the continent.");

        // Verify the output contains the expected report structure
        verifyOutputContains(continent + " Population Report");
        verifyOutputContains("Code\tName\tContinent\tRegion\tPopulation\tCapital");
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
