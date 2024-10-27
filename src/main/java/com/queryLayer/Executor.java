package com.queryLayer;

import java.lang.reflect.Field;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbconn.Database;
import com.tables.Table;
import com.tables.UserDetails;

public class Executor {
	private static List<List<Object>> executeQuery(Select select){
		int columnssize = select.columns.size()+select.aggregratecolumns.size();
		if (select.allCol) {
			try {
				Class<?> c = Class.forName("com.tables.UserDetails");
				Field[] fields = c.getDeclaredFields();
				System.out.println(fields.length);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		List<List<Object>> result = new ArrayList<>();
		try (Connection c = Database.getConnection()) {
			System.out.println("inside try");
			System.out.println(select.build());
			PreparedStatement ps = c.prepareStatement(select.build());
			System.out.println("after ps");
			ResultSet rs = ps.executeQuery();
			System.out.println("gotbres");
			while (rs.next()) {
				List<Object> RsRow = new ArrayList<Object>();
				for(int i=0; i<columnssize;i++) {
					RsRow.add(rs.getObject(i+1));
				}
				result.add(RsRow);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in method");
		}
		return result;
	}
	
	
	public int executeUpdate(Query query) {
		
		try(Connection c = Database.getConnection()){
			PreparedStatement ps = c.prepareStatement(query.build());
			return ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		Select s = new Select();
		s.table(Table.UserDetails).columns(UserDetails.CONTACT_TYPE);
		try {
			List<List<Object>> res = executeQuery(s);
			System.out.println(res.get(0));

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in main");
		}
	}
	
	
}
