package com.queryLayer;

import java.util.ArrayList;

import java.util.List;

import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.loggers.AppLogger;
import com.queryBuilder.SqlDeleteQueryBuilder;
import com.queryBuilder.postgres.DeleteQueryBuilder;
public class Delete extends Query {
	
	private Table tableName;
//	List<String> columns;
//	List<String> values;
	private List<Condition> conditions;
	private String query;
	
	public Delete() {
		this.conditions = new ArrayList<Condition>();
	}
	
	public Delete table(Table tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public Delete condition(Columns column, Operators operator, String value) {
		this.conditions.add(new Condition(column,operator,value));
		return this;
	}

	public String build() {
		if (query == null) {
			if (prop.getProperty("database_name").equals("mysql")) {
				try {
					this.query = new SqlDeleteQueryBuilder(this).build();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					AppLogger.ApplicationLog(e);
					e.printStackTrace();
				}
				return query;
			}
			else if (prop.getProperty("databse_name").equals("postgres")) {
				// pg select query builder
				this.query = new DeleteQueryBuilder(this).build();
				return query;
			}
		}
		return query;
		
		
	}
	public int executeUpdate() {
		
		return super.executeUpdate(this);
	}
	
	public Table getTableName() {
		return this.tableName;
	}
	
	public List<Condition> getConditions(){
		return this.conditions;
	}
	
}
