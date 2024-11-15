package com.queryBuilder;

import java.util.StringJoiner;

import com.queryLayer.Condition;
import com.queryLayer.Update;

public class SqlUpdateQueryBuilder implements Builder {
	Update updateObj;

	public SqlUpdateQueryBuilder(Update update) {
		// TODO Auto-generated constructor stub
		this.updateObj = update;
	}
	
	public String build() throws Exception{
		String query = "UPDATE ";
		
		//tableName
		query += this.updateObj.tableName+ " SET ";	
		
		
		//columns and values
		if (this.updateObj.columns.isEmpty() && this.updateObj.values.isEmpty()) {
			throw new Exception("insufficient column and values data");
		}
		else if (this.updateObj.columns.size() != this.updateObj.values.size()) {
			throw new Exception("unequal columns and values");
		}
		else {
			StringJoiner colAndValueJoiner = new StringJoiner(", ");
			for(int i=0; i<this.updateObj.columns.size(); i++) {
				colAndValueJoiner.add(this.updateObj.columns.get(i) + " = " +
					    (CheckDataType.isFloat(this.updateObj.values.get(i)) || 
					     CheckDataType.isInt(this.updateObj.values.get(i)) || 
					     CheckDataType.isLong(this.updateObj.values.get(i)) 
					     ? this.updateObj.values.get(i) 
					     : "'" + this.updateObj.values.get(i) + "'"));
			}
			query += colAndValueJoiner.toString();
			
		}
		
		if (!this.updateObj.conditions.isEmpty()) {
			StringJoiner conditionJoiner = new StringJoiner(" AND ");
			for(Condition c : this.updateObj.conditions) {
				conditionJoiner.add(String.format("%s %s %s", c.column,c.operator,((CheckDataType.isFloat(c.value)||CheckDataType.isInt(c.value)||CheckDataType.isLong(c.value))?c.value:"'"+c.value+"'")));
			}
			query += " WHERE "+conditionJoiner.toString();
			
		}
		
		return query+";";
	}

}
