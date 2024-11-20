package com.queryBuilder;

import java.util.StringJoiner;

import com.queryLayer.Insert;
import com.tables.Columns;

public class SqlInsertQueryBuilder implements Builder{
	Insert insertObj;
	String query = null;
	
	public SqlInsertQueryBuilder(Insert insert) {
		this.insertObj = insert;
	}
	
	public String build() throws Exception {
		query = "Insert into ";
		
		//table name
		query += this.insertObj.getTableName()+" ";
		 
		//columns if columns not present then values
		if (this.insertObj.getColumns().isEmpty()&& this.insertObj.getValues().isEmpty()) {
			throw new Exception("insufficient data of columns and values");
		}
		
		else if (this.insertObj.getColumns().isEmpty() && ! this.insertObj.getValues().isEmpty()) {
			StringJoiner valueJoiner = new StringJoiner(",","(",")");
			for(String val : this.insertObj.getValues()) {
				valueJoiner.add(val);
			}
			query += "VALUES "+valueJoiner.toString();
		}                                                                                                                                                                                                                                                        
		else if (!this.insertObj.getColumns().isEmpty() && !this.insertObj.getValues().isEmpty()) {
			if (this.insertObj.getColumns().size() != this.insertObj.getValues().size()) {
				throw new Exception("column and values size didn't match please check your query");
			}else {
				StringJoiner columnJoiner = new StringJoiner(",","(",")");
				for(Columns col : this.insertObj.getColumns()) {
					columnJoiner.add(col.value());
				}
				StringJoiner valueJoiner = new StringJoiner(",","(",")");
				for(String val : this.insertObj.getValues()) {
					valueJoiner.add((CheckDataType.isFloat(val)||CheckDataType.isInt(val)||CheckDataType.isLong(val))?val:"'"+val+"'");
				}
				query += columnJoiner+" VALUES "+valueJoiner.toString();
			}
		}
		return query+";";
		
	}
	
}
