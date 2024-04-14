package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PopulationReportIT {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Connection con;
    private PopulationReport report;

    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?useSSL=false", "root", "example");

        report = new PopulationReport(con);
    }

    @AfterEach
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    void testGeneratePopulationInCitiesVSNonCityByCountry() {
        report.generatePopulationInCitiesVSNonCityByCountry();

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "The population of people, people living in cities, and people not living in cities in each country\n" +
                "--------------------------------------------------------------------------------------------------\n" +
                "Country                                       Total Population  City Population  Non-City Population  City Population %  Non-City Population %  \n" +
                "China                                         1277558000        175953614        1101604386           14                 86                     \n" +
                "India                                         1013662000        123298526        890363474            12                 88                     \n" +
                "United States                                 278357000         78625774         199731226            28                 72                     \n" +
                "Indonesia                                     212107000         37485695         174621305            18                 82                     \n" +
                "Brazil                                        170115000         85876862         84238138             50                 50                     \n" +
                "Pakistan                                      156483000         31546745         124936255            20                 80                     \n" +
                "Russian Federation                            146934000         69150700         77783300             47                 53                     \n" +
                "Bangladesh                                    129155000         8569906          120585094            7                  93                     \n" +
                "Japan                                         126714000         77965107         48748893             62                 38                     \n" +
                "Nigeria                                       111506000         17366900         94139100             16                 84                     \n";
        assertTrue(output.contains(expectedOutput));
    }
}
