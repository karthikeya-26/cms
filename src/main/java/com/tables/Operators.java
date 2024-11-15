package com.tables;

public enum Operators {
	
	Equals("="),
	NotEquals("!="),
	LessThan("<"),
	LessThanOrEqualTo("<="),
	GreaterThan(">"),
	GreaterThanOrEqualTo(">="),
	Max("MAX"),
	Min("MIN"),
	Avg("AVG"),
	Count("COUNT");
	
	
	private String operator;
	
	private Operators(String operator) {
		this.operator = operator;
	}
	
	public String value() {
		return this.operator;
	}

}
