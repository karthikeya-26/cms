package com.queryLayer;
import java.lang.reflect.Field;

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
//import com.notifier.ResourceRemoveNotifier;
//import com.notifier.SessionmapUpdateNotifier;
//import com.notifier.UserDetailsUpdateNotifier;
import com.queryBuilder.*;
import com.session.SessionDataManager;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;

public class Query {
	
//	static Builder builder;
//
//	public String tableName;
//	public List<Columns> columns;
//	public List<String> values;
//	public List<Condition> conditions;
	static Properties prop = Database.prop;
	
//	public Query table(Table tableName) {
//		this.tableName = tableName.value();
//		return this;
//	}
//
	public String build() {
		// TODO Auto-generated method stub
		return null;
	}
//	public Query values(String string) {
//		// TODO Auto-generated method stub
//		return this;
//	}
//	
//	public Query condition(Columns column, Operators operator, String value) {
//		return this;
//	}
//	
//	public String getTableName() {
//		return this.tableName;
//	}
//	public List<Columns> getColumns(){
//		return this.columns;
//	}
//	public List<String> getValues(){
//		return this.values;
//	}
//	
//	public List<Condition> getConditions() {
//		// TODO Auto-generated method stub
//		return this.conditions;
//	}
//	
	public  List<ResultObject> executeQuery( Query query, Class<? extends ResultObject> clazz ) {
	

		 List<ResultObject> resultList = new ArrayList<>();
		
		try(Connection c = Database.getConnection();
				PreparedStatement ps = c.prepareStatement(query.build())){
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
	
	public List<HashMap<Columns, Object>> executeQuery(Query query, HashMap<Columns, Class<?>> fields) {
		List<HashMap<Columns,Object>> resultObj = new ArrayList<HashMap<Columns,Object>>();
		try(Connection c = Database.getConnection();
			PreparedStatement ps = c.prepareStatement(query.build())){
			
			ResultSet resultSet = ps.executeQuery();
//			System.out.println(fields);
//			System.out.println(ps);
//			System.out.println(resultSet);
			while(resultSet.next()) {
				HashMap<Columns, Object> row = new HashMap<Columns, Object>();
				for(Columns colName : fields.keySet()) {
					Class<?> fieldType = fields.get(colName);
					
					if (fieldType == Integer.class) {
	                    row.put(colName, resultSet.getInt(colName.value()));
	                } else if (fieldType == String.class) {
	                    row.put(colName, resultSet.getString(colName.value()));
	                } else if (fieldType == Boolean.class) {
	                    row.put(colName, resultSet.getBoolean(colName.value()));
	                } else if (fieldType == Double.class) {
	                    row.put(colName, resultSet.getDouble(colName.value()));
	                } else if (fieldType == Date.class) {
	                    row.put(colName, resultSet.getDate(colName.value()));
	                } else if (fieldType == Long.class) {
	                	row.put(colName, resultSet.getLong(colName.value()));
	                } else {
	                
	                    row.put(colName, resultSet.getObject(colName.value())); // Handle unknown types
	                }
				}
				resultObj.add(row);
			}
		}catch(SQLException e) {
			
			System.out.println("SQL Exception occured");
			e.printStackTrace();
		}
		return resultObj;
	}
	
	public int executeUpdate(Query query) {
		int status = -1;
		try(Connection c = Database.getConnection()){
			PreparedStatement ps = c.prepareStatement(query.build());
			status =  ps.executeUpdate();
		}catch(SQLException e) {
			AppLogger.ApplicationLog(e);
			e.printStackTrace();
		}
//		ResourceRemoveNotifier.sendNotification(query);
		return status;
		
	}

	public int executeUpdate(Query query, boolean returnGeneratedKey) throws SQLException {
		
		try(Connection c =Database.getConnection();
				PreparedStatement ps = c.prepareStatement(query.build(),PreparedStatement.RETURN_GENERATED_KEYS)){
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
