package com.napier.semTest;

import com.napier.sem.PopulationReport;
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
public class PopulationReportTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rset;
    @InjectMocks
    private PopulationReport cityPopulationReport;
    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rset);
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByContinent() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("Continent")).thenReturn("Asia");
        when(rset.getBigDecimal("TotalPopulation")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("PopulationInCities")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("PopulationInNonCityAreas")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByContinent();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT co.Continent, " +
                        "SUM(co.Population) AS TotalPopulation, " +
                        "SUM(ci.CityPopulation) AS PopulationInCities, " +
                        "SUM(co.Population) - SUM(ci.CityPopulation) AS PopulationInNonCityAreas, " +
                        "ROUND((SUM(ci.CityPopulation) / SUM(co.Population)) * 100) AS PercentageCities, " +
                        "ROUND(((SUM(co.Population) - SUM(ci.CityPopulation)) / SUM(co.Population)) * 100) AS PercentageNonCities " +
                        "FROM country co " +
                        "LEFT JOIN (SELECT c.CountryCode, SUM(c.Population) AS CityPopulation FROM city c GROUP BY c.CountryCode) ci ON co.Code = ci.CountryCode " +
                        "GROUP BY co.Continent " +
                        "ORDER BY TotalPopulation DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The population of people, people living in cities, and people not living in cities in each continent"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Continent\tTotal Population\tCity Population\tNon-City Population\tCity Population %\tNon-City Population %"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByRegion() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("Region")).thenReturn("Eastern Asia");
        when(rset.getBigDecimal("TotalPopulation")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("PopulationInCities")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("PopulationInNonCityAreas")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByRegion();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT country.region AS Region, " +
                        "SUM(country.population) AS TotalPopulation, " +
                        "SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code)) AS PopulationInCities, " +
                        "(SUM(country.population)-SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code))) AS PopulationInNonCityAreas, " +
                        "ROUND((SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code)) / SUM(country.population))*100) AS PercentageCities, " +
                        "ROUND(((SUM(country.population) - SUM((SELECT SUM(population) FROM city WHERE countrycode = country.code))) / SUM(country.population))*100) AS PercentageNonCities " +
                        "FROM country " +
                        "GROUP BY country.region " +
                        "ORDER BY TotalPopulation DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("The population of people, people living in cities, and people not living in cities in each region"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Region\tTotal Population\tCity Population\tNon-City Population\tCity Population %\tNon-City Population %"),
                "Output should contain the correct column headers.");
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByCountry() throws Exception {
        when(rset.next()).thenReturn(true, true, false); // Simulate two rows returned
        when(rset.getString("country_name")).thenReturn("Indonesia");
        when(rset.getBigDecimal("total_population")).thenReturn(new BigDecimal(10000));
        when(rset.getBigDecimal("population_in_cities")).thenReturn(new BigDecimal(8000));
        when(rset.getBigDecimal("population_not_in_cities")).thenReturn(new BigDecimal(2000));

        cityPopulationReport.generatePopulationInCitiesVSNonCityByCountry();

        verify(con).prepareStatement(sqlCaptor.capture());
        String executedSQL = sqlCaptor.getValue();
        String expectedQuery =
                "SELECT " +
                        "co.name AS country_name, " +
                        "co.population AS total_population, " +
                        "SUM(ci.population) AS population_in_cities, " +
                        "co.population - SUM(ci.population) AS population_not_in_cities, " +
                        "ROUND((SUM(ci.population) / co.population) * 100) AS percentage_population_in_cities, " +
                        "ROUND(((co.population - SUM(ci.population)) / co.population) * 100) AS percentage_population_not_in_cities " +
                        "FROM " +
                        "country co " +
                        "LEFT JOIN " +
                        "city ci ON co.code = ci.countrycode " +
                        "GROUP BY " +
                        "co.code, co.name, co.population " +
                        "ORDER BY " +
                        "total_population DESC";

        assertTrue(executedSQL.contains(expectedQuery),
                "The SQL should contain the correct population city vs non city.");
        // Verify printReport invocation indirectly by checking the console output
        String output = outContent.toString();
        assertTrue(output.contains("The population of people, people living in cities, and people not living in cities in each country"),
                "Output should contain the report title.");
        assertTrue(output.replaceAll("\\s\\s+", "\t").contains("Country\tTotal Population\tCity Population\tNon-City Population\tCity Population %\tNon-City Population %"),
                "Output should contain the correct column headers.");
    }

}

