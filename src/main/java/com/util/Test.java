package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.dbObjects.UserGroupsObj;
import com.dbconn.Database;
import com.enums.Operators;
import com.enums.Servers;
import com.enums.Table;
import com.google.gson.Gson;
import com.queryLayer.Insert;
import com.queryLayer.Select;

public class Test {
    private static final Logger logger = Logger.getLogger(Test.class.getName());
    public static void main(String[] args) throws Exception {
    	UserGroupsObj obj = new UserGroupsObj();
    	obj.setGroupName("karthik");
    	Gson gson = new Gson();
    	System.out.println(gson.toJson(obj));
//        Connection c = Database.getConnection(); // Obtain database connection
//
//        if (c == null) {
//            System.out.println("Failed to establish a connection!");
//            return;
//        }

//        String insertSQL = "INSERT INTO sampleImg (id, img) VALUES (?, ?)";
//        String filePath = "/home/karthi-pt7680/Pictures/Screenshots/Screenshot from 2024-09-05 15-46-22.png";
//; // Replace with your image path
//
//        try (FileInputStream fis = new FileInputStream(new File(filePath));
//             PreparedStatement ps = c.prepareStatement(insertSQL)) {
//
//            ps.setInt(1, 1); // Set the ID value (replace with desired ID)
//            ps.setBinaryStream(2, fis, fis.available()); // Set the image as binary stream
//
//            int rows = ps.executeUpdate();
//            if (rows > 0) {
//                System.out.println("Image inserted successfully!");
//            }
//
//        } catch (IOException e) {
//            System.err.println("File operation failed: " + e.getMessage());
//        } catch (Exception e) {
//            System.err.println("Database operation failed: " + e.getMessage());
//        } finally {
//            try {
//                if (c != null) {
//                    c.close();
//                }
//            } catch (Exception e) {
//                System.err.println("Failed to close the connection: " + e.getMessage());
//            }
//        }
        
//        try (InputStream configFile = Test.class.getClassLoader().getResourceAsStream("logging.properties")) {
//            if (configFile != null) {
//                LogManager.getLogManager().readConfiguration(configFile);
//                logger.info("Logging configuration loaded successfully from classpath!");
//            } else {
//                System.err.println("Could not find logging.properties in classpath!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String statement = "Select * from sampleImg where id = 1";
//        try {
//			PreparedStatement ps = c.prepareStatement(statement);
//			System.out.println(System.currentTimeMillis());
//			ps.executeQuery();
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//        StartUpTasks tasks = new StartUpTasks();
//        tasks.loadLoggingConfig();
//        Logger logger = Logger.getLogger("Karthik");
//        logger.log(Level.INFO,"My name is tomcat");
//        ServersDao dao = new ServersDao();
//		 Insert i = new Insert();
//        i.table(Table.Servers).columns(Servers.NAME, Servers.PORT).values("karthikeya", "8084");
//        PreExecuteTasks tasks = new PreExecuteTasks();
//        tasks.addTimeToQueries(i);
//        System.out.println(i.getColumns());
//        System.out.println(i.getValues());
//        int serverId = i.executeUpdate(true);
//        System.out.println("generated server id :"+ serverId);
//        Select s = new Select();
//        s.table(Table.Servers).condition(Servers.SERVER_ID, Operators.Equals, String.valueOf(serverId));
//        System.out.println(s.executeQuery(ServersObj.class));
    }
}
