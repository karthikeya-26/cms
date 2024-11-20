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
		query += this.deleteObj.getTableName();
		
		
		//conditions
		if (this.deleteObj.getConditions().isEmpty()) {
			throw new Exception("insufficient data to build the delete statement");
		}
		else {
			StringJoiner conditionJoiner = new StringJoiner(" AND ");
			for (Condition condition : this.deleteObj.getConditions()) {
				conditionJoiner.add(String.format("%s %s %s", condition.column.value(), condition.operator.value(), ((CheckDataType.isFloat(condition.value)||CheckDataType.isInt(condition.value)||CheckDataType.isLong(condition.value))?condition.value:"'"+condition.value+"'")));
			}
			query += " WHERE " +conditionJoiner.toString();
		}
		
		return query+";";
	}
	
	
	
}
