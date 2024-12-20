package com.dao;

import java.util.HashMap;
import java.util.List;

import com.dbObjects.ConfigurationsObj;
import com.dbObjects.ResultObject;
import com.enums.Columns;
import com.enums.Configurations;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class ConfigurationsDao {
	
	//Select
	public List<ResultObject> getAppConfig() throws Exception{
		Select s = new Select();
		s.table(Table.Configurations);
		List<ResultObject> result = s.executeQuery(ConfigurationsObj.class);
		return result;
	}
	
	public ConfigurationsObj getConfigWithId(Integer id) throws Exception {
		Select s = new Select();
		s.table(Table.Configurations).condition(Configurations.ID, Operators.Equals,id.toString());
		return (ConfigurationsObj) s.executeQuery(ConfigurationsObj.class).get(0);
	}
	
	//Insert
	public boolean addAppConfig(String name, String value) {
		Insert i = new Insert();
		i.table(Table.Configurations);
		i.columns(Configurations.NAME,Configurations.VALUE).values(name,value);
		return i.executeUpdate() >0;
	}
	
	//Update Config
	public boolean updateConfig(String name, String value) {
		Update u = new Update();
		u.table(Table.Configurations).columns(Configurations.NAME, Configurations.VALUE).values(name,value)
		.condition(Configurations.NAME, Operators.Equals, name);
		return u.executeUpdate() > 0;
	}
	
	// Delete COnfig
	public boolean deleteConfig(Integer id) {
		Delete d = new Delete();
		d.table(Table.Configurations).condition(Configurations.ID, Operators.Equals, id.toString());
		return d.executeUpdate() > 0;
	}
	
}
