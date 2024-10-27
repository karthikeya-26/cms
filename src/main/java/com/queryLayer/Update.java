package com.queryLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dbconn.Database;
import com.queryBuilder.SqlUpdateQueryBuilder;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;

public class Update extends Query {

	public String tableName;
	public List<String> columns;
	public List<String> values;
	public List<Condition> conditions;

	public Update() {
		this.columns = new ArrayList<String>();
		this.values = new ArrayList<String>();
		this.conditions = new ArrayList<Condition>();
	}

	public Update table(Table tableName) {
		this.tableName = tableName.value();
		return this;
	}

	public Update columns(Columns... columns) {
		for (Columns col : columns) {
			this.columns.add(col.getClass().getSimpleName()+"."+col.value());
		}
		return this;
	}
	
	public Update values(String... values) {
		for(String value : values) {
			this.values.add(value);
		}
		return this;
	}

	public Update condition(Columns column, Operators operator, String value) {
		conditions.add(new Condition(column, operator, value));
		return this;
	}
	
	public String build() {
		//update object will be sent to the specific query builder
		Properties prop = Database.prop;
    	if (prop.getProperty("db.name").equals("mysql")) {
    		return new SqlUpdateQueryBuilder(this).build();
    	}
    	if(prop.getProperty("db.name").equals("postgres")) {
    		//pg select query builder
    	}
		return "";
	}

}
