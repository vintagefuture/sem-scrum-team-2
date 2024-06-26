package com.napier.sem;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link Helpers} class.
 */
public class HelpersTest {

    /**
     * Test case for printing a report with data.
     */
    @Test
    public void testPrintReport() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        Helpers helpers = new Helpers();

        // Define test data
        String title = "Test Report";
        List<String> columnNames = Arrays.asList("Name", "Age", "City");
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("John Doe", "30", "New York"));
        rows.add(Arrays.asList("Jane Smith", "25", "Los Angeles"));

        // Invoke the method under test
        helpers.printReport(title, columnNames, rows);

        // Verify printed output
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertEquals("", lines[0]); // Empty line before the title
        assertEquals(title, lines[1]);
        assertEquals("-".repeat(title.length()), lines[2]);
        assertEquals("Name        Age  City         ", lines[3]);
        assertEquals("John Doe    30   New York     ", lines[4]);
        assertEquals("Jane Smith  25   Los Angeles  ", lines[5]);
    }

    /**
     * Test case for printing a report with empty data.
     */
    @Test
    public void testPrintReport_EmptyData() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        Helpers helpers = new Helpers();

        // Define test data
        String title = "Empty Report";
        List<String> columnNames = Arrays.asList("Name", "Age", "City");
        List<List<String>> rows = new ArrayList<>();

        // Invoke the method under test
        helpers.printReport(title, columnNames, rows);

        // Verify printed output
        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());
        assertEquals("", lines[0]); // Empty line before the title
        assertEquals(title, lines[1]);
        assertEquals("-".repeat(title.length()), lines[2]);
        assertEquals("Name  Age  City", lines[3].trim()); // Ignore trailing whitespace
    }
}
