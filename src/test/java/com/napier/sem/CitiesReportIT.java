package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CitiesReportIT {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Connection con;
    private CitiesReport report;

    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");

        report = new CitiesReport(con);
    }

    @AfterEach
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    void testGetCitiesPopulationReportInTheWorld() {
        report.getCitiesPopulationReportInTheWorld();

        // Verify the output contains expected values
        String output = outContent.toString();
        String expectedOutput = "All the cities in the world organised by largest population to smallest\n" +
                "-----------------------------------------------------------------------\n" +
                "Name                               Country                                District              Population  \n" +
                "Mumbai (Bombay)                    India                                  Maharashtra           10500000    \n" +
                "Seoul                              South Korea                            Seoul                 9981619     \n" +
                "São Paulo                          Brazil                                 São Paulo             9968485     \n" +
                "Shanghai                           China                                  Shanghai              9696300     \n" +
                "Jakarta                            Indonesia                              Jakarta Raya          9604900     \n" +
                "Karachi                            Pakistan                               Sindh                 9269265     \n" +
                "Istanbul                           Turkey                                 Istanbul              8787958     \n" +
                "Ciudad de México                   Mexico                                 Distrito Federal      8591309     \n" +
                "Moscow                             Russian Federation                     Moscow (City)         8389200     \n" +
                "New York                           United States                          New York              8008278     \n" +
                "Tokyo                              Japan                                  Tokyo-to              7980230     \n" +
                "Peking                             China                                  Peking                7472000     \n" +
                "London                             United Kingdom                         England               7285000     \n" +
                "Delhi                              India                                  Delhi                 7206704     \n" +
                "Cairo                              Egypt                                  Kairo                 6789479     \n" +
                "Teheran                            Iran                                   Teheran               6758845     \n" +
                "Lima                               Peru                                   Lima                  6464693     \n" +
                "Chongqing                          China                                  Chongqing             6351600     \n" +
                "Bangkok                            Thailand                               Bangkok               6320174     \n" +
                "Santafé de Bogotá                  Colombia                               Santafé de Bogotá     6260862     \n" +
                "Rio de Janeiro                     Brazil                                 Rio de Janeiro        5598953     \n" +
                "Tianjin                            China                                  Tianjin               5286800     \n" +
                "Kinshasa                           Congo, The Democratic Republic of the  Kinshasa              5064000";

        assertTrue(output.contains(expectedOutput));
    }
};
