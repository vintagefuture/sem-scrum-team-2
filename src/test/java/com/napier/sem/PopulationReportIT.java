package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PopulationReportIT {

    private Connection con;
    private PopulationReport report;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() throws Exception {
        // Redirect System.out to capture the output
        System.setOut(new PrintStream(outContent));

        String host = System.getenv("MYSQL_HOST");
        String port = System.getenv("MYSQL_PORT");
        String database = System.getenv("MYSQL_DB");
        String user = System.getenv("MYSQL_USER");
        String password = System.getenv("MYSQL_PASSWORD");

        con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", user, password);

        report = new PopulationReport(con);
        try (Statement stmt = con.createStatement()) {
            // Create schema (tables) and insert some test data
            stmt.execute("CREATE TABLE IF NOT EXISTS country (" +
                    "  Code char(3) NOT NULL DEFAULT ''," +
                    "  Name char(52) NOT NULL DEFAULT ''," +
                    "  Continent enum('Asia','Europe','North America','Africa','Oceania','Antarctica','South America') NOT NULL DEFAULT 'Asia'," +
                    "  Region char(26) NOT NULL DEFAULT ''," +
                    "  SurfaceArea decimal(10,2) NOT NULL DEFAULT '0.00'," +
                    "  IndepYear smallint DEFAULT NULL," +
                    "  Population int NOT NULL DEFAULT '0'," +
                    "  LifeExpectancy decimal(3,1) DEFAULT NULL," +
                    "  GNP decimal(10,2) DEFAULT NULL," +
                    "  GNPOld decimal(10,2) DEFAULT NULL," +
                    "  LocalName char(45) NOT NULL DEFAULT ''," +
                    "  GovernmentForm char(45) NOT NULL DEFAULT ''," +
                    "  HeadOfState char(60) DEFAULT NULL," +
                    "  Capital int DEFAULT NULL," +
                    "  Code2 char(2) NOT NULL DEFAULT ''," +
                    "  PRIMARY KEY (Code)" +
                    ");");
            stmt.execute("CREATE TABLE city (" +
                    "  ID int NOT NULL AUTO_INCREMENT," +
                    "  Name char(35) NOT NULL DEFAULT ''," +
                    "  CountryCode char(3) NOT NULL DEFAULT ''," +
                    "  District char(20) NOT NULL DEFAULT ''," +
                    "  Population int NOT NULL DEFAULT '0'," +
                    "  PRIMARY KEY (ID)," +
                    "  CONSTRAINT city_ibfk_1 FOREIGN KEY (CountryCode) REFERENCES country (Code)" +
                    ");");
            stmt.execute("INSERT INTO country VALUES ('FRA','France','Europe','Western Europe',551500.00,843,59225700,78.8,1424285.00,1392448.00,'France','Republic','Jacques Chirac',2974,'FR');");
            stmt.execute("INSERT INTO city VALUES (2974,'Paris','FRA','Île-de-France',2125246);");
            // Add more test data as needed

        } catch (Exception e) {
            System.out.printf(e.toString());
        }
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
        assertTrue(output.contains("France")); // Example assertion
        // Add more assertions as needed based on expected output
    }
}
