package com.queryBuilder;

import java.util.StringJoiner;


import com.queryLayer.AggregateColumn;
import com.queryLayer.Condition;
import com.queryLayer.Join;
import com.queryLayer.Select;
import com.tables.Table;
import com.tables.UserDetails;

public class SqlSelectQueryBuilder implements Builder {
	Select selectObj;

	public SqlSelectQueryBuilder(Select select) {
		// TODO Auto-generated constructor stub
		this.selectObj = select;
	}

	public String build() {

		String query = "SELECT ";
		
		//columns

		// Step 1: Determine which columns to select
		if (this.selectObj.columns.isEmpty() && this.selectObj.aggregratecolumns.isEmpty()) {
		    // Select all columns if both are empty
		    query += "*";
		} else {
			
			 
		    StringJoiner columnsJoiner = new StringJoiner(", ");
		    
		    // Add aggregate columns if available
		    if (!this.selectObj.aggregratecolumns.isEmpty()) {
		        // aggColumnsJoiner is assumed to be constructed elsewhere
		    	for(AggregateColumn col : this.selectObj.aggregratecolumns) {
		    		columnsJoiner.add(String.format(col.Aggregate+"("+col.column_name+")"));
		    	}

		    }

		    // Add regular columns if available
		    if (!this.selectObj.columns.isEmpty()) {
		    	for(String col : this.selectObj.columns) {
		    		columnsJoiner.add(col.toString());
		    	}
		    }
		    
		    query += columnsJoiner.toString(); // Finalize the column selection part
		}
		
		// table
		query += " FROM "+this.selectObj.table;

		if (!this.selectObj.joins.isEmpty()) {
			StringJoiner joinJoiner = new StringJoiner(" ", " ", "");
			for (Join join : this.selectObj.joins) {
				
				// Splitting to check for alias in join.table1
		        String[] table1Parts = join.table1.split(" ");
		        String table1Alias = (table1Parts.length == 2) ? table1Parts[1] : table1Parts[0];

		        // Adding the formatted join clause to the joinJoiner
		        joinJoiner.add(String.format("%s %s ON %s.%s %s %s.%s", 
		            join.joinType,        // LEFT JOIN, INNER JOIN, etc.
		            join.table1,          // Table1 with optional alias
		            table1Alias,          // Table alias or table name (if no alias)
		            join.table1col,       // Column from table1
		            join.operator,        // = or other join operator
		            join.table2,          // Table2
		            join.table2col));     // Column from table2
			}
				
			query += joinJoiner.toString();
		}
		// where conditions
		if (!this.selectObj.conditions.isEmpty()) {
			StringJoiner conditionsJoiner = new StringJoiner(" AND ");
			for (Condition condition : this.selectObj.conditions) {

				conditionsJoiner.add(String.format("%s %s %s", condition.column, condition.operator, ((CheckDataType.isFloat(condition.value)||CheckDataType.isInt(condition.value)||CheckDataType.isLong(condition.value))?condition.value:"'"+condition.value+"'")));
			}
			query += " WHERE " + conditionsJoiner.toString();
		}
		// group by
		if (!this.selectObj.groupBy.isEmpty()) {
			StringJoiner groupByJoiner = new StringJoiner(", ");
			for (String groupCol : this.selectObj.groupBy) {
				groupByJoiner.add(groupCol.toString());
			}
			query += " GROUP BY " + groupByJoiner.toString();
		}
		// orderby
		if (!this.selectObj.orderBy.isEmpty()) {
			StringJoiner orderByJoiner = new StringJoiner(", ");
			for (String col : this.selectObj.orderBy) {
				orderByJoiner.add(col.toString());
			}
			query += " ORDER BY " + orderByJoiner.toString();
		}

		if (this.selectObj.limit != -1) {
			query += " " + this.selectObj.limit;
		}

		return query + ";";

	}

	public static void main(String[] args) {
		String s = new Select().table(Table.UserDetails).columns(UserDetails.USER_ID, UserDetails.LAST_NAME).build();
		System.out.println(s);
	}

}
