package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import com.enums.*;
import com.queryLayer.*;

public class PreExecuteTasks {
	
	private HashMap<String , Object> methodResults = new HashMap<>();
	
	public HashMap<String, Object> getResultMap() {
		return methodResults;
	}
	
	public void addTimeToQueries(Query query) throws Exception {
		if(query instanceof Insert) {
			Insert insertQuery = (Insert) query;
			
			Table table = insertQuery.getTableName();
			
			List<Columns> cols = insertQuery.getColumns();
			
			System.out.println("Table :" +table.value());
			Class<?> columnClass = table.getColumnClass();
			System.out.println(columnClass +" col class");
			Field columnMapField = columnClass.getDeclaredField("LOOKUP_MAP");
			System.out.println(columnMapField);
			columnMapField.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<String, Columns> columnMap = (HashMap<String, Columns>) columnMapField.get(null);
			System.out.println(columnMap);
			if(cols.contains(columnMap.get("created_at"))) {
				System.out.println("QUery already has time, so returning");
				return;
			}
			Long time = Instant.now().toEpochMilli();
			if(columnMap.containsKey("created_at")) {
				insertQuery.columns(columnMap.get("created_at")).values(time.toString());
			}
		}else if(query instanceof Update) {
			Update updateQuery = (Update) query;
			Table table = query.getTableName();
			Class<?> columnClass = table.getColumnClass();
			Field columnMapField = columnClass.getDeclaredField("LOOKUP_MAP");
			columnMapField.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<String, Columns> columnMap = (HashMap<String, Columns>) columnMapField.get(null);
//			System.out.println(columnMap);
			Long time = Instant.now().toEpochMilli();
			if(columnMap.containsKey("modified_at")) {
				updateQuery.columns(columnMap.get("modified_at")).values(time.toString());
			}
		}
	}
	
	public void getRefData(Query query) throws Exception{
		
		if(query instanceof Update) {
			Update updateQuery = (Update) query;
			Table table = updateQuery.getTableName();
			List<Columns> columns = updateQuery.getColumns();
			List<Condition> conditions = updateQuery.getConditions();
			Select s = new Select();
			
			s.table(table).columns(updateQuery.getColumns().toArray(new Columns[0]));
			for(Condition c : conditions) {
				s.condition(c.getColumn(), c.getOperator(), c.getValue());
			}
			List<HashMap<Columns,Object>> result = s.executeQuery();
			System.out.println(result);
			methodResults.put("getRefData", result.get(0));
			
		}else if(query instanceof Delete) {
			Delete deleteQuery = (Delete) query;
			Table table = deleteQuery.getTableName();
			List<Condition> conditions = deleteQuery.getConditions();
			
			//Select query
			Select s = new Select();
			List<Columns> columns = table.getColumns();
			s.table(table).columns(columns.subList(0, columns.size()-1).toArray(new Columns[0]));
			for(Condition c : conditions) {
				s.condition(c.getColumn(), c.getOperator(), c.getValue());
			}
			List<HashMap<Columns,Object>> result = s.executeQuery();
			methodResults.put("getRefData", result.get(0));
		}
	}
	
	

	
}
