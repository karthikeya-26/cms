package com.queryLayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.queryBuilder.SqlInsertQueryBuilder;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;

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
    	}
		return query;
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
