package com.queryLayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.queryBuilder.SqlInsertQueryBuilder;

public class Insert extends Query{
	
	private Table tableName;
	private List<Columns> columns;
	private List<String> values;
	private List<Condition> conditions;
	private String query = null;
	
	
	public Insert(){
		this.columns = new ArrayList<>();
		this.values = new ArrayList<>();
		this.conditions = new ArrayList<>();
	}
	
	public Insert table(Table tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public Insert columns (Columns... columns) {
		for(Columns col : columns) {
			this.columns.add(col);
		}
		return this;
	}
	
	public Insert values(String... values) {
		for(String val : values) {
			this.values.add(val);
		}
		return this;
	}
	
	public Insert condition(Columns column, Operators operator, String value) {
		conditions.add(new Condition(column, operator, value));
		return this;
	}
	
	public String build() {
    	if(query == null) {
    		
    		if (prop.getProperty("database_name").equals("mysql")) {
        		try {
    				 this.query = new SqlInsertQueryBuilder(this).build();
    				 System.out.println(query);
    			} catch (Exception e) {
    				System.out.println(e);
    			}
        	}
        	if(prop.getProperty("database_name").equals("postgres")) {
        		System.out.println("currently no support for postgres");
        	}
    	}
		return query;
	}
	
	
	public int executeUpdate() {
		
		return super.executeUpdate(this);
	}
	
	public int executeUpdate(boolean returnGeneratedKey) {
		
		try {
			return super.executeUpdate(this, returnGeneratedKey);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}

	public Table getTableName() {
		return tableName;
	}

	public List<Columns> getColumns() {
		return columns;
	}

	public List<String> getValues() {
		return values;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

}
