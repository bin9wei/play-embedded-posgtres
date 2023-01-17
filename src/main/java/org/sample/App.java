package org.sample;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public final static String PG_JDBC = "jdbc:postgresql://localhost:15432/myapp";
    public final static String PG_USER = "myapp";
    public final static String PG_PASSWORD = "dbpass";
    static final String MIGRATION_LOCATION = "db/migration";

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PG_JDBC);
        config.setUsername(PG_USER);
        config.setPassword(PG_PASSWORD);
        HikariDataSource ds = new HikariDataSource(config);
        flywayDbMigrate(ds);
        DAO dao = new DAO(ds);
        PersonDAO personDAO = new PersonDAO(dao);
        try {
            Person test = personDAO.getById(1);
            if (test == null) {
                int howManyAdded = personDAO.add(Person.builder().personId(1).firstName("John").lastName("Doe").build());
                LOGGER.info("Added {} person", howManyAdded);
            } else {
                LOGGER.info("Fetched person {}", test);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to add person!", e);
            throw new RuntimeException(e);
        }
    }

    private static void flywayDbMigrate(DataSource ds) {
        Flyway flyway = Flyway.configure()
                .locations(MIGRATION_LOCATION)
                .dataSource(ds)
                .validateOnMigrate(false)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }
}
