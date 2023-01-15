package org.sample;

import io.zonky.test.db.postgres.embedded.FlywayPreparer;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlywayPreparerTest {
    @RegisterExtension
    public PreparedDbExtension db = EmbeddedPostgresExtension.preparedDatabase(FlywayPreparer.forClasspathLocation("db/migration"));

    @DisplayName("Should connect database")
    @Test
    public void testDbConnection() throws Exception {
        try (Connection c = db.getTestDatabase().getConnection();
             Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT 1");
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @DisplayName("Should create table via flyway")
    @Test
    public void testTablesMade() throws Exception {
        try (Connection c = db.getTestDatabase().getConnection();
             Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT count(1) FROM information_schema.tables WHERE table_name='person'");
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }
}
