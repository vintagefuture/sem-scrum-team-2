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
public class RegionPopulationReportTest {

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private RegionPopulationReport regionPopulationReport;

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
    void testGenerateRegionReport() throws Exception {
        String region = "Europe";
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString(any())).thenReturn("TestData");
        when(rset.getInt("c.Population")).thenReturn(100000);

        regionPopulationReport.generateRegionReport(region);

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
}
