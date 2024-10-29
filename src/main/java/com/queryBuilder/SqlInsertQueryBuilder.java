package com.queryBuilder;

import java.util.StringJoiner;

import com.queryLayer.Insert;

public class SqlInsertQueryBuilder implements Builder{
	Insert insertObj;
	String query = null;
	
	public SqlInsertQueryBuilder(Insert insert) {
		this.insertObj = insert;
	}
	
	public String build() throws Exception {
		query = "Insert into ";
		
		//table name
		query += this.insertObj.tableName;
		 
		//columns if columns not present then values
		if (this.insertObj.columns.isEmpty()&& this.insertObj.values.isEmpty()) {
			throw new Exception("insufficient data of columns and values");
		}
		
		else if (this.insertObj.columns.isEmpty() && ! this.insertObj.values.isEmpty()) {
			StringJoiner valueJoiner = new StringJoiner(",","(",")");
			for(String val : this.insertObj.values) {
				valueJoiner.add(val);
			}
			query += "VALUES "+valueJoiner.toString();
		}                                                                                                                                                                                                                                                        
		else if (!this.insertObj.columns.isEmpty() && !this.insertObj.values.isEmpty()) {
			if (this.insertObj.columns.size() != this.insertObj.values.size()) {
				throw new Exception("column and values size didn't match please check your query");
			}else {
				StringJoiner columnJoiner = new StringJoiner(",","(",")");
				for(String col : this.insertObj.columns) {
					columnJoiner.add(col);
				}
				StringJoiner valueJoiner = new StringJoiner(",","(",")");
				for(String val : this.insertObj.values) {
					valueJoiner.add((CheckDataType.isFloat(val)||CheckDataType.isInt(val)||CheckDataType.isLong(val))?val:"'"+val+"'");
				}
				query += columnJoiner+" VALUES "+valueJoiner.toString();
			}
		}
		return query+";";
		
	}
	
}
