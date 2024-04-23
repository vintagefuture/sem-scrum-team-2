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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LanguagesPopulationReport} class.
 */
@ExtendWith(MockitoExtension.class)
public class LanguagesPopulationReportTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rset;

    @InjectMocks
    private LanguagesPopulationReport languagesPopulationReport;

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
     * Test case for retrieving total population for languages.
     *
     * @throws Exception if an error occurs during execution
     */
    @Test
    void testGetTotalPopulationForLanguages() throws Exception {
        // Mock behavior for ResultSet
        when(rset.next()).thenReturn(true, true, false);
        when(rset.getString("language")).thenReturn("English", "Spanish");
        when(rset.getLong("total_population")).thenReturn(100000L, 80000L);
        when(rset.getString("percentage_of_world_population")).thenReturn("20%", "16%");

        // Call the method under test
        languagesPopulationReport.getTotalPopulationForLanguages();

        // Verify that the expected SQL query is executed
        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT\n" +
                "    language,\n" +
                "    SUM(population) AS total_population,\n" +
                "    CONCAT(FORMAT((SUM(population) / (SELECT SUM(population) FROM country)) * 100, 0), '%') AS percentage_of_world_population\n" +
                "FROM\n" +
                "    countrylanguage\n" +
                "JOIN\n" +
                "    country ON countrylanguage.countrycode = country.code\n" +
                "WHERE\n" +
                "    language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')\n" +
                "GROUP BY\n" +
                "    language\n" +
                "ORDER BY\n" +
                "    total_population DESC";
        assertEquals(expectedQuery, executedSQL.trim(), "The SQL should match the expected query.");

        // Verify the output contains expected values
        String output = outContent.toString();
        assertTrue(output.contains("Total number of people who speak Chinese, English, Hindi, Spanish, Arabic, from greatest number to smallest"),
                "Output should contain the correct report data.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Language\tTotal Population\tPercentage of world population"),
                "Output should contain the correct column headers.");
    }
}
