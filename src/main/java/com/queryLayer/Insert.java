package com.queryLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbconn.Database;
import com.loggers.AppLogger;
import com.queryBuilder.SqlInsertQueryBuilder;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;

public class Insert extends Query{
	
	public String tableName;
	public List<String> columns;
	public List<String> values;
	public List<Condition> conditions;
	private String query = null;
	
	
	public Insert(){
		this.columns = new ArrayList<String>();
		this.values = new ArrayList<String>();
		this.conditions = new ArrayList<Condition>();
	}
	
	public Insert table(Table tableName) {
		this.tableName = tableName.toString();
		return this;
	}
	
	public Insert columns (Columns... columns) {
		for(Columns col : columns) {
			this.columns.add(col.value());
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
		
    	if (prop.getProperty("db.name").equals("mysql")) {
    		try {
				 this.query = new SqlInsertQueryBuilder(this).build();
			} catch (Exception e) {
				e.getMessage();
			}
    	}
    	if(prop.getProperty("db.name").equals("postgres")) {
    		System.out.println("currently no support for postgres");
    	}
		return this.query;
	}
	
	
	public int executeUpdate() {
		if (this.query == null) {
			this.query = this.build();
		}
		return super.executeUpdate(this);
	}
	
	public int executeUpdate(boolean returnGeneratedKey) {
		if(this.query == null) {
			this.query = this.build();
		}
		try {
			return super.executeUpdate(this, returnGeneratedKey);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	

}
