package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.enums.Operators;
import com.enums.Servers;
import com.enums.Table;
import com.queryLayer.*;

public class ServersDao {
	//SELECT -> all servers
	public List<ServersObj> getServers(){
		List<ServersObj> servers = new ArrayList<ServersObj>();
		Select s = new Select();
		s.table(Table.Servers);
		List<ResultObject> resultList = s.executeQuery(ServersObj.class);
		for(ResultObject server : resultList) {
			servers.add((ServersObj) server);
		}
		return servers;
	}
	
	//INSERT 
	public boolean addServer(String serverName, String port) {
		Insert i = new Insert();
		i.table(Table.Servers).columns(Servers.SERVER_NAME,Servers.PORT).values(serverName,port);
		return i.executeUpdate()>0;
	}
	
	//UPDATE  -> No update to servers
	
	//DELETE 
	public boolean removeServer(String serverName, String port) {
		Delete d = new Delete();
		d.table(Table.Servers)
		.condition(Servers.SERVER_NAME, Operators.Equals, serverName)
		.condition(Servers.PORT, Operators.Equals, port);
		return d.executeUpdate() > 0;
	}
}

