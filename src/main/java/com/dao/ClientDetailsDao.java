package com.dao;

import java.util.*;
import com.dbObjects.*;
import com.enums.ClientDetails;
import com.enums.Operators;
import com.enums.Table;
import com.filters.SessionFilter;
import com.queryBuilder.BuildException;
import com.queryLayer.*;
public class ClientDetailsDao {

	// SELECT
	
	public ClientDetailsObj getClient(String clientId) throws QueryException {
		Select getClientWithId = new Select();
		getClientWithId.table(Table.ClientDetails).condition(ClientDetails.CLIENT_ID, Operators.Equals, clientId);
		List<ResultObject> resultSet = getClientWithId.executeQuery(ClientDetailsObj.class);
		return resultSet.size()> 0 ? (ClientDetailsObj) resultSet.get(0) : null;
	}
	
	public static void main(String[] args) throws QueryException {
		ClientDetailsObj client = new ClientDetailsDao().getClient("faa58ca6-a709-42cb-ae1b-0672c43a1556");
		System.out.println(client.getScopes().isEmpty());
	}
	
	public List<ClientDetailsObj> getClients() throws DaoException{
		List<ClientDetailsObj> clients = new ArrayList<ClientDetailsObj>();
		Select getAllClients = new Select();
		getAllClients.table(Table.ClientDetails);
		List<ResultObject> allClients = null;
		try {
			allClients = getAllClients.executeQuery(ClientDetailsObj.class);
		} catch (QueryException e) {

			e.printStackTrace();
			throw new DaoException("Failed to get Clients",e);
		}
		
		for (ResultObject resultObject : allClients) {
			ClientDetailsObj client = (ClientDetailsObj) resultObject;
			clients.add(client);
		}
		return clients;
	}
	
	public List<ClientDetailsObj> getUserClients(Integer userId) throws DaoException{
		List<ClientDetailsObj> userClients = new ArrayList<ClientDetailsObj>();
		Select getUserClients = new Select();
		getUserClients.table(Table.ClientDetails).condition(ClientDetails.USER_ID, Operators.Equals, userId.toString());
		
		List<ResultObject> uClients = null;
		try {
			uClients = getUserClients.executeQuery(ClientDetailsObj.class);
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DaoException("Failed to get Clients of user",e);
		}
		for (ResultObject client : uClients) {
			userClients.add((ClientDetailsObj) client);
		}
		return userClients;
	}
	
	//INSERT
	public boolean addClient(String clientId, Integer userId, String clientName, String clientType, String clientSecret, String scopes ) throws DaoException {
		Insert i = new Insert();
		i.table(Table.ClientDetails).columns(ClientDetails.CLIENT_ID,ClientDetails.USER_ID, ClientDetails.CLIENT_NAME, ClientDetails.CLIENT_TYPE, ClientDetails.CLIENT_SECRET,ClientDetails.SCOPES);
		i.values(clientId, userId.toString(), clientName, clientType , clientSecret, scopes);
		try {
			return i.executeUpdate()>0;
		} catch (QueryException e) {
			throw new DaoException("Failed to add Client", e);
		}
	}
	
	
	public boolean updateClient(String clientId, Integer userId, String clientName , String clientType, String clientSecret) throws DaoException {
		Update u = new Update();
		u.table(Table.ClientDetails);
		u.columns(ClientDetails.CLIENT_ID, ClientDetails.USER_ID, ClientDetails.CLIENT_NAME, ClientDetails.CLIENT_TYPE, ClientDetails.CLIENT_SECRET);
		u.values(clientId,userId.toString(), clientName,clientType, clientSecret);
		try {
			return u.executeUpdate() > 0;
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DaoException("Failed to update client details of client :"+clientId);
		}
		
	}
	
	public boolean deleteClient(String clientId) throws DaoException {
		Delete deleteClient = new Delete();
		deleteClient.table(Table.ClientDetails)
		.condition(ClientDetails.CLIENT_ID, Operators.Equals, clientId)
		.condition(ClientDetails.USER_ID, Operators.Equals, SessionFilter.user_id.get().toString());
		try {
			return deleteClient.executeUpdate() > 0;
		} catch (QueryException e) {
			throw new DaoException("Failed to Delete client :"+clientId, e);
		}
	}
	
	

	
}
