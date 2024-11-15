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

	public String getColumn() {
		return this.column;
	}

	public void setColumn(Columns column) {
		this.column = column.value();
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(Operators operator) {
		this.operator = operator.value();
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Condition [column=" + column + ", operator=" + operator + ", value=" + value + "]";
	}
	
	
	
	
	
}
