package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @AfterEach
    public void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    public void testConnect_SuccessfulConnection() {
        // Assuming the database is running at localhost:3306 with the specified credentials
        String location = "localhost:3306";
        int delay = 0;

        Connection connection = DatabaseConnection.connect(location, delay);

        assertNotNull(connection);

        try {
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConnect_UnsuccessfulConnection() {
        // Assuming there's no database running at this location
        String location = "nonexistenthost:3306";
        int delay = 0;

        Connection connection = DatabaseConnection.connect(location, delay);

        assertNull(connection);
    }

    @Test
    public void testCloseConnection() {
        // Assuming the database is running at localhost:3306 with the specified credentials
        String location = "localhost:3306";
        int delay = 0;

        Connection connection = DatabaseConnection.connect(location, delay);

        assertNotNull(connection);

        DatabaseConnection.closeConnection();

        try {
            assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }
}
