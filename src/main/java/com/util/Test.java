package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.dbconn.Database;

public class Test {
    public static void main(String[] args) {
        Connection c = Database.getConnection(); // Obtain database connection

        if (c == null) {
            System.out.println("Failed to establish a connection!");
            return;
        }

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
        
        
        String statement = "Select * from sampleImg where id = 1";
        try {
			PreparedStatement ps = c.prepareStatement(statement);
			System.out.println(System.currentTimeMillis());
			ps.executeQuery();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
}
