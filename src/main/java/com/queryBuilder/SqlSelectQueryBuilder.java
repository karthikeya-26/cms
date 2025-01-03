package com.queryBuilder;

import java.util.StringJoiner;

import com.enums.Columns;
import com.queryLayer.AggregateColumn;
import com.queryLayer.Condition;
import com.queryLayer.Join;
import com.queryLayer.Select;

public class SqlSelectQueryBuilder  {
	Select selectObj;

	public SqlSelectQueryBuilder(Select select) {
		this.selectObj = select;
	}

	public String build() throws BuildException{

		String query = "SELECT ";
		
		//columns

		// Step 1: Determine which columns to select
		if (this.selectObj.getColumns().isEmpty() && this.selectObj.getAggregratecolumns().isEmpty() && this.selectObj.getGroupConcat().isEmpty()) {
		    // Select all columns if both are empty
		    query += "*";
		} else {
			
			 
		    StringJoiner columnsJoiner = new StringJoiner(", ");
		    
		    // Add aggregate columns if available
		    if (!this.selectObj.getAggregratecolumns().isEmpty()) {
		        // aggColumnsJoiner is assumed to be constructed elsewhere
		    	for(AggregateColumn col : this.selectObj.getAggregratecolumns()) {
		    		columnsJoiner.add(String.format(col.Aggregate.value()+"("+col.column_name+")"));
		    	}

		    }

		    // Add regular columns if available
		    if (!this.selectObj.getColumns().isEmpty()) {
		    	for(Columns col : this.selectObj.getColumns()) {
		    		columnsJoiner.add(col.getClass().getSimpleName()+"."+col.value());
		    	}
		    }
		    
		    query += columnsJoiner.toString(); // Finalize the column selection part
		}
		
		// table
		query += " FROM "+this.selectObj.getTable().value();

		if (!this.selectObj.getJoins().isEmpty()) {
			StringJoiner joinJoiner = new StringJoiner(" ", " ", "");
			for (Join join : this.selectObj.getJoins()) {
//				System.out.println(join);
//				// Splitting to check for alias in join.table1
//		        String table1Alias = (join.table1Alias != null) ? join.table1Alias : join.table1.value();
//		        System.out.println(table1Alias);
		        // Adding the formatted join clause to the joinJoiner
		        joinJoiner.add(String.format("%s %s ON %s.%s %s %s.%s", 
		            join.joinType.value(),        // LEFT JOIN, INNER JOIN, etc.
		            join.table1.value(),          // Table1 with optional alias
		            join.table1.value(),          // Table alias or table name (if no alias)
		            join.table1col.value(),       // Column from table1
		            join.operator.value(),        // = or other join operator
		            join.table2.value(),          // Table2
		            join.table2col.value()));     // Column from table2
			}
				
			query += joinJoiner.toString();
		}
		// where conditions
		if (!this.selectObj.getConditions().isEmpty()) {
			StringJoiner conditionsJoiner = new StringJoiner(" AND ");
			for (Condition condition : this.selectObj.getConditions()) {
				conditionsJoiner.add(String.format("%s %s %s", condition.column.getClass().getSimpleName()+"."+condition.column.value(), condition.operator.value(), ((CheckDataType.isFloat(condition.value)||CheckDataType.isInt(condition.value)||CheckDataType.isLong(condition.value))?condition.value:"'"+condition.value+"'")));
			}
			query += " WHERE " + conditionsJoiner.toString();
		}
		// group by
		if (!this.selectObj.getGroupBy().isEmpty()) {
			StringJoiner groupByJoiner = new StringJoiner(", ");
			for (Columns groupCol : this.selectObj.getGroupBy()) {
				groupByJoiner.add(groupCol.getClass().getSimpleName()+"."+groupCol.value());
			}
			query += " GROUP BY " + groupByJoiner.toString();
		}
		// orderby
		if (!this.selectObj.getOrderBy().isEmpty()) {
			StringJoiner orderByJoiner = new StringJoiner(", ");
			for (Columns col : this.selectObj.getOrderBy()) {
				orderByJoiner.add(col.getClass().getSimpleName()+"."+col.value());
			}
			query += " ORDER BY " + orderByJoiner.toString();
		}

		if (this.selectObj.getLimit() != -1) {
			query += " " + this.selectObj.getLimit();
		}

		return query + ";";

	}

}
