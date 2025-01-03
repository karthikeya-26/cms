package com.queryLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dbconn.Database;
import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.queryBuilder.SqlUpdateQueryBuilder;

public class Update extends Query {

	private Table tableName;
	private List<Columns> columns;
	private List<String> values;
	private List<Condition> conditions;
	private String query;

	public Update() {
		this.columns = new ArrayList<>();
		this.values = new ArrayList<>();
		this.conditions = new ArrayList<>();
		this.query = null;
	}

	public Update table(Table tableName) {
		this.tableName = tableName;
		return this;
	}

	public Update columns(Columns... columns) {
		for (Columns col : columns) {
			this.columns.add(col);
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
	
	public String build()  {
		//update object will be sent to the specific query builder
		Properties prop = Database.prop;
    	if (prop.getProperty("database_name").equals("mysql")) {
    		
    		try {
				this.query =  new SqlUpdateQueryBuilder(this).build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return this.query;
    	}
    	if(prop.getProperty("database_name").equals("postgres")) {
    		//pg select query builder
    	}
		return null;
	}
	public int executeUpdate() throws QueryException {
		
		return super.executeUpdate(this);
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
