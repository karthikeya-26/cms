package com.queryLayer;

import java.util.ArrayList;
import java.util.List;

import com.queryBuilder.SqlInsertQueryBuilder;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;

public class Insert extends Query {
	
	public String tableName;
	public List<String> columns;
	public List<String> values;
	public List<Condition> conditions;
	
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
			this.columns.add(col.toString());
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
	
	@Override
	public String build() {
		
    	if (prop.getProperty("db.name").equals("mysql")) {
    		try {
				return new SqlInsertQueryBuilder(this).build();
			} catch (Exception e) {
				e.getMessage();
			}
    	}
    	if(prop.getProperty("db.name").equals("postgres")) {
    		//pg select query builder
    	}
		return "";
	}
	

}
