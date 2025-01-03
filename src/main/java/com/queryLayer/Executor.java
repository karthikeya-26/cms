//package com.queryLayer;
//import java.sql.Connection;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import com.dbconn.Database;
//import com.loggers.AppLogger;
//
//public class Executor {
//	
//	public  ResultSet executeQuery(String statement ){
//		
////		int columnssize =0;
////		List<List<Object>> result = new ArrayList<>();
////		try (Connection c = Database.getConnection()) {
////			System.out.println(select.build());
////			PreparedStatement ps = c.prepareStatement(select.build());
////			ResultSet rs = ps.executeQuery();
////			while (rs.next()) {
////				List<Object> RsRow = new ArrayList<Object>();
////				for(int i=0; i<columnssize;i++) {
////					RsRow.add(rs.getObject(i+1));
////				}
////				result.add(RsRow);
////			}
////		} catch (Exception e) {
////			// TODO: handle exception
////			System.out.println("error in method");
////		}
////		return result;
//		
//		try(Connection c = Database.getConnection();
//				PreparedStatement ps = c.prepareStatement(statement)){
//			
//			return ps.executeQuery();
//			
//		}catch(SQLException e) {
//			AppLogger.ApplicationLog(e);
//			e.printStackTrace();
//			return null;
//			
//		}
//	}
//	
//	
//	public int executeUpdate(Query query) {
//		
//		try(Connection c = Database.getConnection()){
//			PreparedStatement ps = c.prepareStatement(query.build());
//			return ps.executeUpdate();
//		}catch(SQLException e) {
//			AppLogger.ApplicationLog(e);
//			e.printStackTrace();
//			return -1;
//		}
//	}
//	
//	public int executeUpdate(Query... queries) {
//		try (Connection c = Database.getConnection()){
//			for(Query query : queries) {
//				PreparedStatement ps = c.prepareStatement(query.build());
//				
//			}
//		} catch (SQLException e) {
//			
//		}
//		return -1;
//	}
//	
//	public int executeUpdate( boolean useGeneratedKey, Query... queries) {
//		int number = queries.length;
//		try(Connection c = Database.getConnection()){
//			String query1 = queries[0].build();
//			c.setAutoCommit(false);
//			PreparedStatement ps = c.prepareStatement(query1,PreparedStatement.RETURN_GENERATED_KEYS);
//			ps.executeUpdate();
//			Integer  id  = ps.getGeneratedKeys().getInt(1);
//			
////			if (number >1) {
////				for(int i =1; i<queries.length; i++) {
////					ps = c.prepareStatement(queries[i].values(id.toString()).build());
////				}
////			}
//			
//			
//		}catch(SQLException e) {
//			
//		}
//		return -1;
//	}
//	
//	
//}
