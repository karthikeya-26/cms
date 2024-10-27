package com.dbconn;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();
    public static Properties prop = new Properties();
    
    

    static {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("resources/config.properties")){
        	if(input == null) {
        		System.out.println("Unable to fetch properties");
        		throw new Exception("unable to find properties config");
        	}
        	prop.load(input);
        	
        	cpds.setDriverClass(prop.getProperty("db.driver"));
        	cpds.setJdbcUrl(prop.getProperty("db.url"));
            cpds.setUser(prop.getProperty("db.username"));
            cpds.setPassword(prop.getProperty("db.password"));
            
            // Set connection pool properties
            cpds.setInitialPoolSize(Integer.parseInt(prop.getProperty("db.initialPoolSize")));
            cpds.setMinPoolSize(Integer.parseInt(prop.getProperty("db.minPoolSize")));
            cpds.setAcquireIncrement(Integer.parseInt(prop.getProperty("db.acquireIncrement")));
            cpds.setMaxPoolSize(Integer.parseInt(prop.getProperty("db.maxPoolSize")));
            cpds.setMaxStatements(Integer.parseInt(prop.getProperty("db.maxStatements")));
            cpds.setUnreturnedConnectionTimeout(Integer.parseInt(prop.getProperty("db.unreturnedConnectionTimeout")));
            cpds.setDebugUnreturnedConnectionStackTraces(Boolean.parseBoolean(prop.getProperty("db.debugUnreturnedConnectionStackTraces")));
            
//            cpds.setDriverClass("com.mysql.cj.jdbc.Driver"); 
//            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/cms");   
//            cpds.setUser("root");                 
//            cpds.setPassword("karthik@sql");          
//            cpds.setInitialPoolSize(10);
//            cpds.setMinPoolSize(5);
//            cpds.setAcquireIncrement(5);
//            cpds.setMaxPoolSize(20);
//            cpds.setMaxStatements(100);
//            cpds.setUnreturnedConnectionTimeout(300);
//            cpds.setDebugUnreturnedConnectionStackTraces(false);
        } catch ( IOException |PropertyVetoException e) {
            e.printStackTrace(); 
        } catch (Exception e) {
			// TODO Auto-generated catch block
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
