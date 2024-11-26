package com.queryLayer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.dbObjects.ResultObject;
import com.dbconn.Database;
import com.enums.*;
import com.loggers.AppLogger;
import com.util.PostExecuteTasks;
import com.util.PreExecuteTasks;

public class Query {
	
	private static List<Table> tablesWithoutTasks;
	static {
		tablesWithoutTasks = new ArrayList<Table>();
		tablesWithoutTasks.add(Table.ChangeLog);
		tablesWithoutTasks.add(Table.Servers);
		tablesWithoutTasks.add(Table.Sessions);
	}
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
	public Table getTableName() {
		return null;
	}
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
		if(query instanceof Insert || query instanceof Update || query instanceof Delete) {
			executeUpdate(query);
			return null;
		}

		 List<ResultObject> resultList = new ArrayList<>();
		
		try(Connection c = Database.getConnection()){
				PreparedStatement ps = c.prepareStatement(query.build());
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
		
		if(query instanceof Insert || query instanceof Update || query instanceof Delete) {
			executeUpdate(query);
			return null;
		}
		
		List<HashMap<Columns,Object>> resultObj = new ArrayList<HashMap<Columns,Object>>();
		try(Connection c = Database.getConnection();
			PreparedStatement ps = c.prepareStatement(query.build())){
			
			ResultSet resultSet = ps.executeQuery();
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
		System.out.println(query.getTableName());
		PreExecuteTasks pretasks=null;
		if(!(query.getTableName().equals(Table.ChangeLog))){
			pretasks = new PreExecuteTasks();
			
			Method[] preexMethods = pretasks.getClass().getDeclaredMethods();
			for(Method m : preexMethods) {
				m.setAccessible(true);
				if(m.getParameterCount() == 1 ) {
					try {
						m.invoke(pretasks, query);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						AppLogger.ApplicationLog(e);
					}
				}
			}
		}
		
		int status = -1;
		try(Connection c = Database.getConnection()){
			PreparedStatement ps = c.prepareStatement(query.build());
			status =  ps.executeUpdate();
		}catch(SQLException e) {
			AppLogger.ApplicationLog(e);
			e.printStackTrace();
		}
//		System.out.println(query.build());
		if(pretasks!=null) {
			System.out.println(pretasks.getResultMap());
		}
		
		if(status >=0 && !(query.getTableName().equals(Table.ChangeLog))) {
			System.out.println("Status :"+status +" "+query.getTableName());
			PostExecuteTasks posttasks = new PostExecuteTasks();
			Method[] methods = posttasks.getClass().getDeclaredMethods();
			for(Method m: methods) {
				try {
					System.out.println(m.getName());
					System.out.println(m.getParameterCount());
					m.setAccessible(true);
					m.invoke(posttasks, query, pretasks.getResultMap());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					AppLogger.ApplicationLog(e);
					System.out.println(m.getClass().getSimpleName());
					System.out.println("Method name:"+m.getName());
					System.out.println(m.toString());
					System.out.println("Method param count:"+m.getParameterCount());
					for(Class<?> claz : m.getParameterTypes()) {
						System.out.println("param type :"+claz.getName());
					}
					
					e.printStackTrace();
				}
			}
//			try {
//				posttasks.audit(query, pretasks.getResultMap());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return status;
		
	}

	public int executeUpdate(Query query, boolean returnGeneratedKey) throws SQLException {
		
		PreExecuteTasks pretasks=null;
		if(!(query.getTableName().equals(Table.ChangeLog))){
			pretasks = new PreExecuteTasks();
			
			Method[] preexMethods = pretasks.getClass().getDeclaredMethods();
			for(Method m : preexMethods) {
				m.setAccessible(true);
				if(m.getParameterCount() == 1 ) {
					try {
						m.invoke(pretasks, query);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						AppLogger.ApplicationLog(e);
					}
				}
			}
		}
		int genKey = -1;
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
		if(pretasks!=null) {
			System.out.println(pretasks.getResultMap());
		}
		
		if(genKey >=0 && !(query.getTableName().equals(Table.ChangeLog))) {
			
			PostExecuteTasks posttasks = new PostExecuteTasks();
			Method[] methods = posttasks.getClass().getDeclaredMethods();
			for(Method m: methods) {
				try {
					m.setAccessible(true);
					m.invoke(posttasks, query, pretasks.getResultMap());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					AppLogger.ApplicationLog(e);
					e.printStackTrace();
				}
			}
		}
		return genKey;
	}

	

	

	
}
