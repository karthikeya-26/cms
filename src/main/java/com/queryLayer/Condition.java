package com.queryLayer;

import com.tables.*;

public class Condition {
	public String column;
	public String operator;
	public String value;
	
	
	public Condition(Columns column, Operators operator, String value) {
		this.column = column.getClass().getSimpleName()+"."+column.value();
		this.operator = operator.value();
		this.value = value;
	}
	
	public Condition(String tableAlias, Columns column, Operators operator, String value) {
		this.column = tableAlias+"."+column.value();
		this.operator = operator.value();
		this.value = value;
	}
	
	
	
	
}
