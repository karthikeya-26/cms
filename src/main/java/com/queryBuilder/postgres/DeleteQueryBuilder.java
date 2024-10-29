package com.queryBuilder.postgres;

import com.queryLayer.Delete;

public class DeleteQueryBuilder {
	Delete delete;
	
	public DeleteQueryBuilder(Delete deleteObj) {
		this.delete = deleteObj;
	}
	
	public String build() {
		return "Still under development";
	}

}
