package com.queryLayer;

import java.util.ArrayList;
import java.util.List;

import com.tables.Columns;
import com.tables.Operators;

public class Delete extends Query {
	
	String tableName;
//	List<String> columns;
//	List<String> values;
	List<Condition> conditions;
	
	public Delete() {
		this.conditions = new ArrayList<Condition>();
	}
	
	public Delete table(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public Delete condition(Columns column, Operators operator, String value) {
		this.conditions.add(new Condition(column,operator,value));
		return this;
	}

	@Override
	public String build() {
		
		
		return null;
	}
	
}
