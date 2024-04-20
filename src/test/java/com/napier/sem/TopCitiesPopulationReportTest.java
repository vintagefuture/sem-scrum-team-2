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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopCitiesPopulationReportTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rset;

    @InjectMocks
    private TopCitiesPopulationReport topCitiesPopulationReport;
    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    @Test
    void testGetTopPopulatedCitiesInTheContinent() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Seoul");
        when(rset.getString(eq("Country"))).thenReturn("South Korea");
        when(rset.getString(eq("District"))).thenReturn("Seoul");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCitiesPopulationReport.getTopPopulatedCitiesInTheContinent("Asia", limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci " +
                "ON c.Code = ci.CountryCode WHERE Continent = 'Asia' ORDER BY Population DESC LIMIT " + limit;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The top " + limit + " populated cities in continent Asia"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tDistrict\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGetTopPopulatedCitiesInTheWorld() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Mumbai");
        when(rset.getString(eq("Country"))).thenReturn("India");
        when(rset.getString(eq("District"))).thenReturn("Maharashtra");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCitiesPopulationReport.getTopPopulatedCitiesInTheWorld(limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci " +
                "ON c.Code = ci.CountryCode ORDER BY Population DESC LIMIT " + limit;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The top " + limit + " populated cities in the world"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tDistrict\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGetTopPopulatedCitiesInTheCountry() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Jakarta");
        when(rset.getString(eq("Country"))).thenReturn("Indonesia");
        when(rset.getString(eq("District"))).thenReturn("Jakarta Raya");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCitiesPopulationReport.getTopPopulatedCitiesInTheCountry("Indonesia", limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci " +
                "ON c.Code = ci.CountryCode WHERE c.Name = 'Indonesia' ORDER BY Population DESC LIMIT " + limit;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The top " + limit + " populated cities in country Indonesia"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tDistrict\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGetTopPopulatedCitiesInTheRegion() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Jakarta");
        when(rset.getString(eq("Country"))).thenReturn("Indonesia");
        when(rset.getString(eq("District"))).thenReturn("Jakarta Raya");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCitiesPopulationReport.getTopPopulatedCitiesInTheRegion("Southern and Central Asia", limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci " +
                "ON c.Code = ci.CountryCode WHERE c.Region = 'Southern and Central Asia' ORDER BY Population DESC LIMIT " + limit;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The top " + limit + " populated cities in region Southern and Central Asia"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tDistrict\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGetTopPopulatedCitiesInTheDistrict() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Santa Monica");
        when(rset.getString(eq("Country"))).thenReturn("USA");
        when(rset.getString(eq("District"))).thenReturn("California");
        when(rset.getInt("Population")).thenReturn(100000);

        int limit = 5;
        topCitiesPopulationReport.getTopPopulatedCitiesInTheDistrict("California", limit);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT ci.Name AS Name, c.Name AS Country, ci.District AS District, ci.Population AS Population FROM country c JOIN city ci " +
                "ON c.Code = ci.CountryCode WHERE ci.District = 'California' ORDER BY Population DESC LIMIT " + limit;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The top " + limit + " populated cities in district California"), "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Name\tCountry\tDistrict\tPopulation"),
                "Output should contain the correct column headers.");
    }
}
