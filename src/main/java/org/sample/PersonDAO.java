package org.sample;

import java.sql.SQLException;

public class PersonDAO {
    private final DAO dao;

    public PersonDAO(DAO dao) {
        this.dao = dao;
    }

    public Person getById(int personId) throws SQLException {
        String sql = "select person_id,last_name,first_name,address,city from person where person_id=?";
        return dao.queryForObject(sql, ps -> ps.setInt(1, personId),
                rs -> Person.builder()
                        .personId(rs.getInt("person_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .address(rs.getString("address"))
                        .city(rs.getString("city")).build());
    }

    public int add(Person person) throws SQLException {
        String sql = "insert into person (person_id,last_name,first_name,address,city) values (?,?,?,?,?)";
        return dao.modify(sql, ps -> {
            ps.setInt(1, person.personId());
            ps.setString(2, person.lastName());
            ps.setString(3, person.firstName());
            ps.setString(4, person.address());
            ps.setString(5, person.city());
        });
    }

    public int delete(int personId) throws SQLException {
        String sql = "delete from person where person_id=?";
        return dao.modify(sql, ps -> ps.setInt(1, personId));
    }
}
