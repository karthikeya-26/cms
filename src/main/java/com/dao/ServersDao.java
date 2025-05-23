package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.enums.Operators;
import com.enums.Servers;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class ServersDao {

	// SELECT -> all servers
	public List<ServersObj> getServers() throws DaoException {
		List<ServersObj> servers = new ArrayList<>();
		try {
			Select s = new Select();
			s.table(Table.Servers);
			List<ResultObject> resultList = s.executeQuery(ServersObj.class);
			for (ResultObject server : resultList) {
				servers.add((ServersObj) server);
			}
		} catch (QueryException e) {
			throw new DaoException("Error fetching servers", e);
		}
		return servers;
	}

	public ServersObj getServer(String ipAddr, Integer port) throws DaoException {
		try {
			Select getServerWithIp = new Select();
			getServerWithIp.table(Table.Servers).condition(Servers.NAME, Operators.Equals, ipAddr)
					.condition(Servers.PORT, Operators.Equals, port.toString());
			List<ResultObject> resultList = getServerWithIp.executeQuery(ServersObj.class);
			return resultList.size() > 0 ? (ServersObj) resultList.get(0) : null;
		} catch (QueryException e) {
			throw new DaoException(e.getMessage());
		}
	}

	// INSERT
	public boolean addServer(String serverName, String port) throws DaoException {
		try {
			Insert i = new Insert();
			i.table(Table.Servers).columns(Servers.NAME, Servers.PORT).values(serverName, port);
			return i.executeUpdate() > 0;
		} catch (QueryException e) {
			throw new DaoException("Error adding server: " + serverName + ":" + port, e);
		}
	}

	// UPDATE -> No update to servers
	public boolean updateStatus(String ipAddress, Integer port, Integer status) throws DaoException {
		try {
			Update updateServerStatus = new Update();
			updateServerStatus.table(Table.Servers)
			.columns(Servers.STATUS)
			.values(status.toString())
			.condition(Servers.NAME,Operators.Equals, ipAddress)
			.condition(Servers.PORT, Operators.Equals, port.toString());
			return updateServerStatus.executeUpdate() >=0 ;
		} catch (QueryException e) {
			throw new DaoException("Error updating servers :" + e.getMessage());
		}
	}
	
	// DELETE
	public boolean removeServer(String serverName, String port) throws DaoException {
		try {
			Delete d = new Delete();
			d.table(Table.Servers).condition(Servers.NAME, Operators.Equals, serverName)
			.condition(Servers.PORT,Operators.Equals, port);
			return d.executeUpdate() > 0;
		} catch (QueryException e) {
			throw new DaoException("Error removing server: " + serverName + ":" + port, e);
		}
	}
}
