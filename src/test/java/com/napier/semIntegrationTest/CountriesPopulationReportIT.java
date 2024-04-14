package com.napier.semIntegrationTest;

import com.napier.sem.CountriesPopulationReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountriesPopulationReportIT {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Connection con;
    private CountriesPopulationReport report;

    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?useSSL=false", "root", "example");

        report = new CountriesPopulationReport(con);
    }

    @AfterEach
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    void testGetCountriesPopulationInContinentReport() {
        report.getCountriesPopulationInContinentReport("Asia");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "All the countries in continent Asia organised by largest population to smallest\n" +
                "-------------------------------------------------------------------------------\n" +
                "Code  Name                  Continent  Region                     Population  Capital              \n" +
                "CHN   China                 Asia       Eastern Asia               1277558000  Peking               \n" +
                "IND   India                 Asia       Southern and Central Asia  1013662000  New Delhi            \n" +
                "IDN   Indonesia             Asia       Southeast Asia             212107000   Jakarta              \n" +
                "PAK   Pakistan              Asia       Southern and Central Asia  156483000   Islamabad            \n" +
                "BGD   Bangladesh            Asia       Southern and Central Asia  129155000   Dhaka                \n" +
                "JPN   Japan                 Asia       Eastern Asia               126714000   Tokyo                \n" +
                "VNM   Vietnam               Asia       Southeast Asia             79832000    Hanoi                \n" +
                "PHL   Philippines           Asia       Southeast Asia             75967000    Manila               \n" +
                "IRN   Iran                  Asia       Southern and Central Asia  67702000    Teheran              \n" +
                "TUR   Turkey                Asia       Middle East                66591000    Ankara               \n" +
                "THA   Thailand              Asia       Southeast Asia             61399000    Bangkok              \n" +
                "KOR   South Korea           Asia       Eastern Asia               46844000    Seoul                \n" +
                "MMR   Myanmar               Asia       Southeast Asia             45611000    Rangoon (Yangon)";

        assertTrue(output.contains(expectedOutput)); // Example assertion
    }

    @Test
    void testGetCountriesPopulationInRegionReport() {
        report.getCountriesPopulationInRegionReport("North America");

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "All the countries in region North America organised by largest population to smallest\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Code  Name                       Continent      Region         Population  Capital       \n" +
                "USA   United States              North America  North America  278357000   Washington    \n" +
                "CAN   Canada                     North America  North America  31147000    Ottawa        \n" +
                "BMU   Bermuda                    North America  North America  65000       Hamilton      \n" +
                "GRL   Greenland                  North America  North America  56000       Nuuk          \n" +
                "SPM   Saint Pierre and Miquelon  North America  North America  7000        Saint-Pierre  ";

        assertTrue(output.contains(expectedOutput));
    }

}
