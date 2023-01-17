package org.sample;

import io.zonky.test.db.postgres.embedded.FlywayPreparer;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {
    private DAO daoEmbeddedPg;
    private PersonDAO underTest;

    @RegisterExtension
    public PreparedDbExtension db = EmbeddedPostgresExtension.preparedDatabase(FlywayPreparer.forClasspathLocation("db/migration"));

    @BeforeEach
    void setUp() {
        daoEmbeddedPg = new DAO(db.getTestDatabase());
        underTest = new PersonDAO(daoEmbeddedPg);
    }

    @Test
    void shouldGetPersonById() throws SQLException {
        // add test person id=10
        DBUtils.executeSQL(db.getTestDatabase().getConnection(), "insert into person(person_id,first_name,last_name) values (10,'John','Doe')");
        Person actual = underTest.getById(10);
        assertEquals(10, actual.personId());
        assertEquals("John", actual.firstName());
        assertEquals("Doe", actual.lastName());
    }

    @Test
    void shouldAddPerson() throws SQLException {
        Person testPerson = Person.builder().personId(10).firstName("John").lastName("Doe").build();
        int howManyAdded = underTest.add(testPerson);
        Person actualAdded = underTest.getById(10);
        assertEquals(1, howManyAdded);
        assertEquals("John", actualAdded.firstName());
        assertEquals("Doe", actualAdded.lastName());
    }

    @Test
    void shouldDeletePerson() throws SQLException {
        Person testPerson = Person.builder().personId(10).firstName("John").lastName("Doe").build();
        underTest.add(testPerson);
        int howManyDeleted = underTest.delete(10);
        assertEquals(1, howManyDeleted);
    }
}