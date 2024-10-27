package com.queryLayer;
import com.tables.*;
import java.util.*;
import com.queryBuilder.SqlSelectQueryBuilder;
import com.queryBuilder.postgres.SelectQueryBuilder;

public class Select extends Query{
	
	public String table;
    public List<String> columns;
    public List<AggregateColumn> aggregratecolumns;
    public List<Condition> conditions;
    public List<Join> joins;
    public List<String> groupBy;
    public List<String> orderBy;
    public int limit =-1;
    boolean allCol;
    
    public Select() {
    	this.aggregratecolumns = new ArrayList<AggregateColumn>();
        this.columns = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.joins = new ArrayList<>();
        this.groupBy = new ArrayList<>();
        this.orderBy = new ArrayList<>();
    }
    
    public Select table(Table table) {
        this.table = table.value();
        return this;
    }
    
    public Select table(Table table, String alias) {
    	this.table = table.value()+" "+alias;
    	return this;
    }
    
    
    public Select column(Columns col) {
    	if(col.value().contains("*"))allCol = true;
    	columns.add(col.getClass().getSimpleName()+"."+col.value());
    	return this;
    }
    
    public Select columns(String tablealias, Columns col) {
    	if(col.value().contains("*"))allCol = true;
    	columns.add(tablealias+"."+col.value());
    	return this;
    }
    
    public Select columns(String tableAlias, Columns... cols) {
    	for(Columns col: cols) {
    		if (col.value().contains("*"))allCol = true;
    		columns.add(tableAlias+"."+col.value());
    	}
    	
    	return this;
    }
    
//    public Select columns(String... cols) {
//    	for(String col : cols) {
//    		columns.add(col);
//    	}
//    	return this;
//    }
    public Select columns(Columns... cols) {
        for (Columns col : cols) {
        	if(col.value().contains("*")) {
        		allCol = true;
        	}
        	columns.add(col.getClass().getSimpleName()+"."+col.value());
        	}
        return this;
    }
    
    public Select condition(Columns column, Operators operator, String value) {
         conditions.add(new Condition(column, operator, value));
        return this;
    }
    
    public Select condition(String tableAlias, Columns column, Operators operator, String value) {
    	conditions.add(new Condition(tableAlias, column,operator,value));
    	return this;
    }

    public Select join(Joins type, Table table1, Columns table1col, Operators operator, Table table2,  Columns table2col) {
        joins.add(new Join(type,table1,table1col,operator,table2,table2col));
        return this;
    }
    //complex queries
    public Select join(Joins type, String table1alias, Columns table1col, Operators operator, String table2alias, Columns table2col) {
    	joins.add(new Join(type,table1alias,table1col,operator,table2alias,table2col));
    	return this;
    }

    public Select groupBy(Columns... columns) {
        for (Columns col : columns) {
            groupBy.add(col.getClass().getSimpleName()+"."+col.value());
        }
        return this;
    }
    
    public Select groupBy(String tableAlias, Columns col) {
    	groupBy.add(tableAlias+"."+col.value());
    	return this;
    }

    public Select orderBy(Columns... columns) {
        for (Columns col : columns) {
            orderBy.add(col.getClass().getSimpleName()+"."+col.value());
        }
        return this;
    }
    
    public Select orderBy(String tableAlias, Columns column) {
    	orderBy.add(tableAlias+"."+column.value());
    	return this;
    }
    public Select groupConcat(Columns col) {
    	columns.add("GROUP_CONCAT( DISTINCT "+col.getClass().getSimpleName()+"."+col.value()+" SEPARATOR \" \")");
    	return this;
    }
    
    public Select groupConcat(String tableAlias, Columns col) {
    	columns.add("GROUP_CONCAT( DISTINCT"+tableAlias+"."+col.value()+" SEPARATOR \" \"");
    	return this;
    }
    public Select groupConcatAs(Columns col, String ColumnAlias) {
    	columns.add("GROUP_CONCAT( DISTINCT "+col.getClass().getSimpleName()+"."+col.value()+" SEPARATOR \" \") AS"+ColumnAlias);
    	return this;
    }
    
    public Select groupConcatAs(String tableAlias, Columns col, String ColumnAlias) {
    	columns.add("GROUP_CONCAT( DISTINCT "+tableAlias+"."+col.value()+" SEPARATOR \" \") AS"+ColumnAlias);
    	return this;
    }
    public Select groupConcatAs(Columns col, String alias, String seperator) {
    	columns.add("GROUP_CONCAT( DISTINCT "+col.getClass().getSimpleName()+"."+col.value()+ " SEPARATOR \""+seperator+"\" "+") AS "+alias);
    	return this;
    }
    public Select groupConcatAs(String tableAlias,Columns col, String columnAlias, String seperator) {
    	columns.add("GROUP_CONCAT( DISTINCT "+tableAlias+"."+col.value()+ " SEPARATOR \""+seperator+"\" "+") AS "+columnAlias);
    	return this;
    }

    public Select max(Columns column) {
    	aggregratecolumns.add(new AggregateColumn(Operators.Max, column));
        return this;
    }
    
    public Select max(String tableAlias, Columns column) {
    	aggregratecolumns.add(new AggregateColumn(Operators.Max,tableAlias, column));
    	return this;
    }

    public Select avg(Columns column) {
    	aggregratecolumns.add(new AggregateColumn(Operators.Avg, column));
        return this;
    }

    public Select min(Columns column) {
    	aggregratecolumns.add(new AggregateColumn(Operators.Min, column));
        return this;
    }
    
    public Select count(Columns column) {
    	aggregratecolumns.add(new AggregateColumn(Operators.Count, column));
    	return this;
    }
    
    @Override
    public String build() {
    	
    	// proxy object to build query based on the db name 
//    	SqlSelectQueryBuilder s = new SqlSelectQueryBuilder(this);
    	if (prop.getProperty("db.name").equals("mysql")) {
    		return new SqlSelectQueryBuilder(this).build();
    	}
    	if(prop.getProperty("db.name").equals("postgres")) {
    		//pg select query builder
    		return new SelectQueryBuilder(this).build();
    	}
		return "";
    }


}
