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
		query += this.updateObj.getTableName()+ " SET ";	
		
		
		//columns and values
		if (this.updateObj.getColumns().isEmpty() && this.updateObj.getValues().isEmpty()) {
			throw new Exception("insufficient column and values data");
		}
		else if (this.updateObj.getColumns().size() != this.updateObj.getValues().size()) {
			throw new Exception("unequal columns and values");
		}
		
		else {
			StringJoiner colAndValueJoiner = new StringJoiner(", ");
			for(int i=0; i<this.updateObj.getColumns().size(); i++) {
				colAndValueJoiner.add(this.updateObj.getColumns().get(i).value() + " = " +
					    (CheckDataType.isFloat(this.updateObj.getValues().get(i)) || 
					     CheckDataType.isInt(this.updateObj.getValues().get(i)) || 
					     CheckDataType.isLong(this.updateObj.getValues().get(i)) 
					     ? this.updateObj.getValues().get(i) 
					     : "'" + this.updateObj.getValues().get(i) + "'"));
			}
			query += colAndValueJoiner.toString();
			
		}
		
		if (!this.updateObj.getConditions().isEmpty()) {
			StringJoiner conditionJoiner = new StringJoiner(" AND ");
			for(Condition c : this.updateObj.getConditions()) {
				conditionJoiner.add(String.format("%s %s %s", c.column.value(),c.operator.value(),((CheckDataType.isFloat(c.value)||CheckDataType.isInt(c.value)||CheckDataType.isLong(c.value))?c.value:"'"+c.value+"'")));
			}
			query += " WHERE "+conditionJoiner.toString();
			
		}
		
		return query+";";
	}

}
