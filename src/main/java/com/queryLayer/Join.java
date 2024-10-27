package com.queryLayer;
import com.tables.*;

public class Join {
	
	public String table1;
	public String table2;
	public String joinType;
	public String table1col;
	public String table2col;
	public String operator;
	
	public Join(Joins type, Table table1, Columns table1col, Operators operator, Table table2, Columns table2col) {
		// TODO Auto-generated constructor stub
		this.joinType = type.value();
		this.table1 = table1.value();
		this.table1col = table1col.value();
		this.operator = operator.value();
		this.table2 = table2.value();
		this.table2col = table2col.value();
	}
	
	public Join(Joins type, String table1alias, Columns table1col, Operators operator, String table2alias, Columns table2col) {
		this.joinType = type.value();
		this.table1 = table1col.getClass().getSimpleName()+" "+table1alias;
		this.table1col = table1col.value();
		this.operator = operator.value();
		this.table2 = table2alias;
		this.table2col = table2col.value();
	}
	
	@Override
	public String toString() {
		System.out.println("join type"+this.joinType);
		System.out.println("table1"+this.table1);
		System.out.println("table1col"+table1col);
		System.out.println("operator :"+this.operator);
		System.out.println("table2 :"+this.table2);
		System.out.println("table2col :"+ this.table2col);
		return "hi from join object";
	}
}
