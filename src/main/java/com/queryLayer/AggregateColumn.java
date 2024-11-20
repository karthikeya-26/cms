package com.queryLayer;

import com.tables.Columns;
import com.tables.Operators;

public class AggregateColumn {
	public Operators Aggregate;
	public Columns column_name;
	String tableAlias;
	
	public AggregateColumn(Operators agg, Columns col) {
		this.Aggregate = agg;
		this.column_name = col;
	}
	
	public AggregateColumn(Operators agg, String tableAlias, Columns col) {
		this.Aggregate = agg;
		this.tableAlias = tableAlias;
		this.column_name = col;
		
	}
}
