package org.sample;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {
    public static void executeSQL(Connection connection, String sql) throws SQLException {
        connection.createStatement().execute(sql);
    }
}
