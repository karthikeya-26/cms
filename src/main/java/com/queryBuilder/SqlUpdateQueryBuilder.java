package com.queryBuilder;

import com.queryLayer.Update;

public class SqlUpdateQueryBuilder implements Builder {
	Update updateObj;

	public SqlUpdateQueryBuilder(Update update) {
		// TODO Auto-generated constructor stub
		this.updateObj = update;
	}
	
	public String build() {
		String query = "";
		
		return query;
	}

}
