package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides the functionality for:
 * Total number of people, with percentage of world population, who speak a language from greatest number to smallest
 */
public class LanguagesPopulationReport {
    private static final Logger LOGGER = Logger.getLogger(LanguagesPopulationReport.class.getName());
    private final Connection con;
    Helpers helpers = new Helpers();

    /**
     * Constructs a new LanguagesPopulationReport object.
     * @param con The database connection to use for retrieving data.
     */
    public LanguagesPopulationReport(Connection con) {
        this.con = con;
    }

    /**
     * Retrieves the total population for specific languages and generates a report.
     */
    public void getTotalPopulationForLanguages() {
        String query = " SELECT\n" +
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

        ArrayList<Language> languages = generateTotalPopulationForLanguageData(query);

        String title = "Total number of people who speak Chinese, English, Hindi, Spanish, Arabic, from greatest number to smallest";
        List<String> columnNames = List.of("Language", "Total Population", "Percentage of world population");
        List<List<String>> rows = new ArrayList<>();

        for (Language language : languages) {
            List<String> row = new ArrayList<>();
            row.add(language.getName());
            row.add(String.valueOf(language.getPopulation()));
            row.add(language.getPercentage());
            rows.add(row);
        }

        helpers.printReport(title, columnNames, rows);
    }

    /**
     * Helper method for retrieving the total population
     * @param query The SQL query to parse the data from
     * @return An array containing totalPopulation and percentage
     */
    ArrayList<Language> generateTotalPopulationForLanguageData(String query) {
        ArrayList<Language> languages = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                Language language = new Language();
                language.setName(rset.getString("language"));
                language.setPopulation(rset.getLong("total_population"));
                language.setPercentage(rset.getString("percentage_of_world_population"));
                languages.add(language);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while generating language data: " + e.getMessage(), e);
        }
        return languages;
    }
}
