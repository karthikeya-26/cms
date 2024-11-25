package com.queryLayer;

import com.enums.Table;

public class Alter {
	
	public String tableName;
	public String from;
	public String to;
	//table
	
	public Alter table(Table table) {
		this.tableName = table.value();
		return this;
	}
	
	
	//columns
}
