package com.queryLayer;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dbconn.Database;
import com.loggers.AppLogger;

public class Executor {
	
	public  ResultSet executeQuery(String statement ){
		
//		int columnssize =0;
//		List<List<Object>> result = new ArrayList<>();
//		try (Connection c = Database.getConnection()) {
//			System.out.println(select.build());
//			PreparedStatement ps = c.prepareStatement(select.build());
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				List<Object> RsRow = new ArrayList<Object>();
//				for(int i=0; i<columnssize;i++) {
//					RsRow.add(rs.getObject(i+1));
//				}
//				result.add(RsRow);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("error in method");
//		}
//		return result;
		
		try(Connection c = Database.getConnection();
				PreparedStatement ps = c.prepareStatement(statement)){
			
			return ps.executeQuery();
			
		}catch(SQLException e) {
			AppLogger.ApplicationLog(e);
			e.printStackTrace();
			return null;
			
		}
	}
	
	
	public int executeUpdate(String statement) {
		
		try(Connection c = Database.getConnection()){
			PreparedStatement ps = c.prepareStatement(statement);
			return ps.executeUpdate();
		}catch(SQLException e) {
			AppLogger.ApplicationLog(e);
			e.printStackTrace();
			return -1;
		}
	}
	
	public int executeUpdates(Query... queries) {
		return -1;
	}
	
	
}
