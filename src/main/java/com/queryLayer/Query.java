package com.queryLayer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.crypto.Data;

import org.eclipse.jdt.internal.compiler.ast.SwitchExpression;

import com.dbObjects.ResultObject;
import com.dbconn.Database;
import com.loggers.AppLogger;
import com.queryBuilder.*;
import com.tables.Table;

public  class Query {
	static Builder builder;

	static Properties prop = Database.prop;

	public String build() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public  List<ResultObject> executeQuery( String statement, Class<? extends ResultObject> clazz ) {
	

		 List<ResultObject> resultList = new ArrayList<>();
		
		try(Connection c = Database.getConnection();
				PreparedStatement ps = c.prepareStatement(statement)){
			ResultSet rs  = ps.executeQuery();
			
			
			while(rs.next()) {

				ResultObject r = clazz.getDeclaredConstructor().newInstance();
				for(Field f : clazz.getDeclaredFields()) {
					f.setAccessible(true);
					
					String fieldName = f.getName();
					Object value = rs.getObject(fieldName);
					
					if(value != null) {
						f.set(r, value);
					}
				}
				resultList.add(r);
			}
			
		}catch(Exception e) {
			AppLogger.ApplicationLog(e);
			e.printStackTrace();
			return null;
			
		}
		return resultList;
	}
	
	public List<HashMap<String, Object>> executeQuery(String query, HashMap<String, Class<?>> fields) {
		List<HashMap<String,Object>> resultObj = new ArrayList<HashMap<String,Object>>();
		try(Connection c = Database.getConnection();
			PreparedStatement ps = c.prepareStatement(query)){
			
			ResultSet resultSet = ps.executeQuery();
//			System.out.println(fields);
//			System.out.println(ps);
//			System.out.println(resultSet);
			while(resultSet.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for(String colName : fields.keySet()) {
					Class<?> fieldType = fields.get(colName);
					
					if (fieldType == Integer.class) {
	                    row.put(colName, resultSet.getInt(colName));
	                } else if (fieldType == String.class) {
	                    row.put(colName, resultSet.getString(colName));
	                } else if (fieldType == Boolean.class) {
	                    row.put(colName, resultSet.getBoolean(colName));
	                } else if (fieldType == Double.class) {
	                    row.put(colName, resultSet.getDouble(colName));
	                } else if (fieldType == Date.class) {
	                    row.put(colName, resultSet.getDate(colName));
	                } else if (fieldType == Long.class) {
	                	row.put(colName, resultSet.getLong(colName));
	                } else {
	                
	                    row.put(colName, resultSet.getObject(colName)); // Handle unknown types
	                }
				}
				resultObj.add(row);
			}
		}catch(SQLException e) {
			System.out.println("SQL Exception occured");
		}
		return resultObj;
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

	public int executeUpdate(String query, boolean returnGeneratedKey) throws SQLException {
		
		try(Connection c =Database.getConnection();
				PreparedStatement ps = c.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
			int success = ps.executeUpdate();
			if(success>=0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	

	
}
