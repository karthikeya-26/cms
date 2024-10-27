package com.queryLayer;

import com.tables.Columns;
import com.tables.Operators;

public class AggregateColumn {
	public String Aggregate;
	public String column_name;
	
	public AggregateColumn(Operators agg, Columns col) {
		this.Aggregate = agg.value();
		this.column_name = col.getClass().getSimpleName()+"."+col.value();
	}
	
	public AggregateColumn(Operators agg, String tableAlias, Columns col) {
		this.Aggregate = agg.value();
		this.column_name = tableAlias+"."+col;
		
	}
}
