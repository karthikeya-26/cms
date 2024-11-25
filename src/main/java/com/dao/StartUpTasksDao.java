package com.dao;

import java.util.HashMap;

import com.enums.Columns;
import com.enums.Configurations;
import com.enums.Table;
import com.queryLayer.Select;

public class StartUpTasksDao {
	
	public void fetchConfig(){
		Select s = new Select();
		s.table(Table.Configurations);
		
	}
	
	
}

