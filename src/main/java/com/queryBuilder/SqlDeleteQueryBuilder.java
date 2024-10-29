package com.queryBuilder;

import java.util.StringJoiner;

import com.queryLayer.Condition;
import com.queryLayer.Delete;

public class SqlDeleteQueryBuilder {
	Delete deleteObj;
	
	public SqlDeleteQueryBuilder(Delete delete) {
		this.deleteObj = delete;
	}

	public String build()  throws Exception{
		String query = "DELETE FROM ";
		
		// tableName
		query += this.deleteObj.tableName;
		
		
		//conditions
		if (this.deleteObj.conditions.isEmpty()) {
			throw new Exception("insufficient data to build the delete statement");
		}
		else {
			StringJoiner conditionJoiner = new StringJoiner(" AND ");
			for (Condition condition : this.deleteObj.conditions) {
				conditionJoiner.add(String.format("%s %s %s", condition.column, condition.operator, ((CheckDataType.isFloat(condition.value)||CheckDataType.isInt(condition.value)||CheckDataType.isLong(condition.value))?condition.value:"'"+condition.value+"'")));
			}
			query += " WHERE " +conditionJoiner.toString();
		}
		
		return query+";";
	}
	
	
	
}
