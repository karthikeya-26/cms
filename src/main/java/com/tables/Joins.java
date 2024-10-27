package com.tables;

public enum Joins {
	
	InnerJoin("Inner Join"),
	LeftJoin("Left Join"),
	RightJoin("Right Join"),
	FullJoin("Full Join");
	
	
	private String joinType;
	Joins(String joinType){
		this.joinType = joinType;
	}
	
	public String value() {
		return joinType;
	}
}
