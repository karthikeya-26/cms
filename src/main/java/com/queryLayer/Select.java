package com.queryLayer;

import java.util.*;

import com.dbObjects.ResultObject;
import com.enums.*;
import com.queryBuilder.BuildException;
import com.queryBuilder.SqlSelectQueryBuilder;
import com.queryBuilder.postgres.SelectQueryBuilder;

public class Select extends Query {
	private String query;

	private Table table;
//	public String tableAlias;
	private List<Columns> columns;
	private List<AggregateColumn> aggregratecolumns;
	private List<Condition> conditions;
	private List<Join> joins;
	private List<Columns> groupBy;
	private List<Columns> orderBy;
	private List<Columns> groupConcat;
	private int limit = -1;
	
	public HashMap<Columns, Class<?>> fields = new HashMap<Columns, Class<?>>();
	

	public String getQuery() {
		return query;
	}

	public Table getTable() {
		return table;
	}

	public List<Columns> getColumns() {
		return columns;
	}

	public List<AggregateColumn> getAggregratecolumns() {
		return aggregratecolumns;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public List<Columns> getGroupBy() {
		return groupBy;
	}

	public List<Columns> getOrderBy() {
		return orderBy;
	}

	public List<Columns> getGroupConcat() {
		return groupConcat;
	}

	public int getLimit() {
		return limit;
	}

	public HashMap<Columns, Class<?>> getFields() {
		return fields;
	}

	public Select() {
		this.aggregratecolumns = new ArrayList<AggregateColumn>();
		this.columns = new ArrayList<>();
		this.conditions = new ArrayList<>();
		this.joins = new ArrayList<>();
		this.groupBy = new ArrayList<>();
		this.orderBy = new ArrayList<>();
		this.groupConcat = new ArrayList<Columns>();
		
	}

	public Select table(Table table) {
		this.table = table;
		return this;
	}

//	public Select table(Table table, String alias) {
//		this.table = table;
//		this.tableAlias = alias;
//		return this;
//	}

//	public Select column(Columns col) {
//		
//		fields.put(col, col.getDataType());
//		columns.add(col);
//		return this;
//	}

//	public Select columns(String tablealias, Columns col) {
//		
//		fields.put(col, col.getDataType());
//		columns.add(col);
//		return this;
//	}

//	public Select columns(String tableAlias, Columns... cols) {
//		for (Columns col : cols) {
//			
//			fields.put(col, col.getDataType());
//			columns.add(col);
//		}
//
//		return this;
//	}

	public Select columns(Columns... cols) {
		for (Columns col : cols) {
			fields.put(col, col.getDataType());
			columns.add(col);
		}
		return this;
	}

	public Select condition(Columns column, Operators operator, String value) {
		conditions.add(new Condition(column, operator, value));
		return this;
	}

//	public Select condition(String tableAlias, Columns column, Operators operator, String value) {
//		conditions.add(new Condition(tableAlias, column, operator, value));
//		return this;
//	}

	public Select join(Joins type, Table table1, Columns table1col, Operators operator, Table table2,
			Columns table2col) {
		joins.add(new Join(type, table1, table1col, operator, table2, table2col));
		return this;
	}

	// complex queries
//	public Select join(Joins type, String table1alias, Columns table1col, Operators operator, String table2alias,
//			Columns table2col) {
//		joins.add(new Join(type, table1alias, table1col, operator, table2alias, table2col));
//		return this;
//	}

	public Select groupBy(Columns... columns) {
		for (Columns col : columns) {
			fields.put(col, col.getDataType());
			groupBy.add(col);
		}
		return this;
	}

	public Select groupBy(String tableAlias, Columns col) {
		fields.put(col, col.getDataType());
		groupBy.add( col);
		return this;
	}

	public Select orderBy(Columns... columns) {
		for (Columns col : columns) {
			orderBy.add(col);
		}
		return this;
	}

//	public Select orderBy(String tableAlias, Columns column) {
//		orderBy.add(tableAlias + "." + column);
//		return this;
//	}

	public Select groupConcat(Columns col) {
		fields.put(col, col.getDataType());
		columns.add(col);
		return this;
	}

//	public Select groupConcat(String tableAlias, Columns col) {
//		fields.put("GROUP_CONCAT( DISTINCT" + tableAlias + "." + col + " SEPARATOR \" \"", col.getDataType());
//		columns.add("GROUP_CONCAT( DISTINCT" + tableAlias + "." + col + " SEPARATOR \" \"");
//		return this;
//	}

//	public Select groupConcatAs(Columns col, String ColumnAlias) {
//		fields.put(ColumnAlias, col.getDataType());
//		columns.add("GROUP_CONCAT( DISTINCT " + col.getClass().getSimpleName() + "." + col
//				+ " SEPARATOR \" \") AS" + ColumnAlias);
//		return this;
//	}

	public Select groupConcatAs(String tableAlias, Columns col, String ColumnAlias) {
		fields.put(col, col.getDataType());
		columns.add(col);
		return this;
	}

//	public Select groupConcatAs(Columns col, String columnAlias, String seperator) {
//		fields.put(columnAlias, col.getDataType());
//		columns.add("GROUP_CONCAT( DISTINCT " + col.getClass().getSimpleName() + "." + col + " SEPARATOR \""
//				+ seperator + "\" " + ") AS " + columnAlias);
//		return this;
//	}

//	public Select groupConcatAs(String tableAlias, Columns col, String columnAlias, String seperator) {
//		fields.put(columnAlias, col.getDataType());
//		columns.add("GROUP_CONCAT( DISTINCT " + tableAlias + "." + col + " SEPARATOR \"" + seperator + "\" "
//				+ ") AS " + columnAlias);
//		return this;
//	}

	public Select max(Columns column) {
		fields.put(column, column.getDataType());
		aggregratecolumns.add(new AggregateColumn(Operators.Max, column));
		return this;
	}

	public Select max(String tableAlias, Columns column) {
		fields.put(column, column.getDataType());
		aggregratecolumns.add(new AggregateColumn(Operators.Max, tableAlias, column));
		return this;
	}

	public Select avg(Columns column) {
		fields.put(column, column.getDataType());
		aggregratecolumns.add(new AggregateColumn(Operators.Avg, column));
		return this;
	}

	public Select min(Columns column) {
		fields.put(column, column.getDataType());
		aggregratecolumns.add(new AggregateColumn(Operators.Min, column));
		return this;
	}

	public Select count(Columns column) {
		fields.put(column, column.getDataType());
		aggregratecolumns.add(new AggregateColumn(Operators.Count, column));
		return this;
	}
	
	public Select limit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public String build() throws BuildException{
		

// 		proxy object to build query based on the db name
//    	SqlSelectQueryBuilder s = new SqlSelectQueryBuilder(this);
		if (prop.getProperty("database_name").equals("mysql")) {
			this.query = new SqlSelectQueryBuilder(this).build();
			return query;
		}
		if (prop.getProperty("databse_name").equals("postgres")) {
			// pg select query builder
			this.query = new SelectQueryBuilder(this).build();
			return query;
		}
		return query;
	}
	
	public List<ResultObject> executeQuery(Class<? extends ResultObject> clazz) throws QueryException {
		return super.executeQuery(this, clazz);
	}
	
	public List<HashMap<Columns, Object>> executeQuery() throws QueryException{

		return super.executeQuery(this, fields);
	}

}
