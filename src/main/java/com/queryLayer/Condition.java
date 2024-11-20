package com.queryLayer;

import com.tables.*;

public class Condition {
	public Columns column;
	public Operators operator;
	public String value;
	
	
	public Condition(Columns column, Operators operator, String value) {
		this.column = column;
		this.operator = operator;
		this.value = value;
	}
	
//	public Condition(String tableAlias, Columns column, Operators operator, String value) {
//		this.column = column;
//		this.operator = operator;
//		this.value = value;
//	}

	public Columns getColumn() {
		return this.column;
	}

	public void setColumn(Columns column) {
		this.column = column;
	}

	public Operators getOperator() {
		return this.operator;
	}

	public void setOperator(Operators operator) {
		this.operator = operator;
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
