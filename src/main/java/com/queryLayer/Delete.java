package com.queryLayer;

import java.util.ArrayList;

import java.util.List;

import com.loggers.AppLogger;
import com.queryBuilder.SqlDeleteQueryBuilder;
import com.queryBuilder.postgres.DeleteQueryBuilder;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;
public class Delete extends Query {
	
	public String tableName;
//	List<String> columns;
//	List<String> values;
	public List<Condition> conditions;
	private String query;
	
	public Delete() {
		this.conditions = new ArrayList<Condition>();
	}
	
	public Delete table(Table tableName) {
		this.tableName = tableName.value();
		return this;
	}
	
	public Delete condition(Columns column, Operators operator, String value) {
		this.conditions.add(new Condition(column,operator,value));
		return this;
	}

	public String build() {
		if (prop.getProperty("db.name").equals("mysql")) {
			try {
				this.query = new SqlDeleteQueryBuilder(this).build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				AppLogger.ApplicationLog(e);
				e.printStackTrace();
			}
			return query;
		}
		if (prop.getProperty("db.name").equals("postgres")) {
			// pg select query builder
			this.query = new DeleteQueryBuilder(this).build();
			return query;
		}
		return query;
		
		
	}
	public int executeUpdate() {
		if (this.query == null) {
			this.query = this.build();
		}
		return super.executeUpdate(this.query);
	}
	
}
