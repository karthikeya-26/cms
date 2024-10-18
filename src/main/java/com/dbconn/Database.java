package com.dbconn;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    public static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver"); 
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/cms");   
            cpds.setUser("root");                 
            cpds.setPassword("karthik@sql");          
            cpds.setInitialPoolSize(10);
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(20);
            cpds.setMaxStatements(100);
            cpds.setUnreturnedConnectionTimeout(300);
            cpds.setDebugUnreturnedConnectionStackTraces(false);
        } catch (PropertyVetoException e) {
            e.printStackTrace(); 
        }
    }

    private Database() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging is recommended
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // Returns the connection to the pool
            } catch (SQLException e) {
                e.printStackTrace(); // Proper logging is recommended
            }
        }
    }
}
