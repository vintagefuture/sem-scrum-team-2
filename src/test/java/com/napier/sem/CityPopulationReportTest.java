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
import java.math.BigDecimal;
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
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("City Name\tCountry\tPopulation"),
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
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("City Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGenerateTopNPopulatedCapitalCitiesInTheContinentReport() throws Exception {
        when(rset.next()).thenReturn(true, true, true, true, true, false); // Simulate five rows returned
        when(rset.getString(eq("Name"))).thenReturn("Beijing");
        when(rset.getString(eq("Country"))).thenReturn("China");
        when(rset.getInt("Population")).thenReturn(100000);

        String continent = "Asia";
        int limit = 5;
        cityPopulationReport.generateTopNPopulatedCapitalCitiesInTheContinentReport(limit, continent);

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery = "SELECT city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Continent='" + continent + "' " +
                "ORDER BY city.Population DESC LIMIT "+ limit ;

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct LIMIT clause for the capital city.");

        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Top " + limit + " Populated Capital Cities in continent " + continent),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("City Name\tCountry\tPopulation"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByContinent() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("Level")).thenReturn("Asia");
        when(rset.getBigDecimal("TotalPopulation")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("CityPopulation")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("NonCityPopulation")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByContinent();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT " + "country.Continent" + " AS Level, " +
                        "SUM(country.Population) AS TotalPopulation, " +
                        "SUM(city.Population) AS CityPopulation, " +
                        "(SUM(country.Population) - SUM(city.Population)) AS NonCityPopulation " +
                        "FROM country " +
                        "JOIN city ON country.Code = city.CountryCode " +
                        "GROUP BY " + "country.Continent" +
                        " ORDER BY TotalPopulation DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Population Report City vs Non City by Continent"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Continent\tTotal Population\tCity Population\tNon-City Population"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByRegion() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("Level")).thenReturn("Eastern Asia");
        when(rset.getBigDecimal("TotalPopulation")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("CityPopulation")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("NonCityPopulation")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByRegion();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT " + "country.Region" + " AS Level, " +
                        "SUM(country.Population) AS TotalPopulation, " +
                        "SUM(city.Population) AS CityPopulation, " +
                        "(SUM(country.Population) - SUM(city.Population)) AS NonCityPopulation " +
                        "FROM country " +
                        "JOIN city ON country.Code = city.CountryCode " +
                        "GROUP BY " + "country.Region" +
                        " ORDER BY TotalPopulation DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Population Report City vs Non City by Region"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Region\tTotal Population\tCity Population\tNon-City Population"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByCountry() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("Level")).thenReturn("Indonesia");
        when(rset.getBigDecimal("TotalPopulation")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("CityPopulation")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("NonCityPopulation")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByCountry();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT " + "country.Name" + " AS Level, " +
                        "SUM(country.Population) AS TotalPopulation, " +
                        "SUM(city.Population) AS CityPopulation, " +
                        "(SUM(country.Population) - SUM(city.Population)) AS NonCityPopulation " +
                        "FROM country " +
                        "JOIN city ON country.Code = city.CountryCode " +
                        "GROUP BY " + "country.Name" +
                        " ORDER BY TotalPopulation DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("Population Report City vs Non City by Country"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Country\tTotal Population\tCity Population\tNon-City Population"),
                "Output should contain the correct column headers.");
    }

}

