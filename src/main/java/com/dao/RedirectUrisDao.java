package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.enums.Operators;
import com.enums.RedirectUris;
import com.enums.Table;
import com.queryBuilder.BuildException;
import com.queryLayer.*;

public class RedirectUrisDao {
		
	//SELECT 
	public List<RedirectUrisObj> getRedirectUris(String clientId) throws QueryException, BuildException{
		
		List<RedirectUrisObj> uriObjects = new ArrayList<RedirectUrisObj>();
		Select getUris = new Select();
		getUris.table(Table.RedirectUris).condition(RedirectUris.CLIENT_ID, Operators.Equals, clientId);
		System.out.println(getUris.build());
		List<ResultObject> uris =  getUris.executeQuery(RedirectUrisObj.class);
		for (ResultObject resultObject : uris) {
			uriObjects.add((RedirectUrisObj) resultObject);
		}
		return uriObjects;
	}
	
	public RedirectUrisObj getRedirectUri(String clientId, String uri) throws QueryException {
		Select getUri = new Select();
		getUri.table(Table.RedirectUris)
		.condition(RedirectUris.URI, Operators.Equals, uri)
		.condition(RedirectUris.CLIENT_ID, Operators.Equals, clientId);
		List<ResultObject> resultSet = getUri.executeQuery(RedirectUrisObj.class);
		
		if(!resultSet.isEmpty()) {
			return (RedirectUrisObj) resultSet.get(0);
		}
		return null;
		
	}
	
	//INSERT
	public boolean addUri(String clientId, String uri) throws QueryException {
		Insert addUri = new Insert();
		addUri.table(Table.RedirectUris).columns(RedirectUris.CLIENT_ID, RedirectUris.URI)
		.values(clientId,uri);
		return addUri.executeUpdate()>0;
	}
	
	//UPDATE 
	public boolean updateUri(Integer uriId, String oldUri, String newUri) throws QueryException{
		Update updateUri = new Update();
		updateUri.table(Table.RedirectUris).columns(RedirectUris.URI).values(newUri)
		.condition(RedirectUris.URI_ID, Operators.Equals, uriId.toString())
		.condition(RedirectUris.URI, Operators.Equals, oldUri);
		return updateUri.executeUpdate()>=0;
	}
	
	public boolean deleteUri(Integer uriId) throws QueryException {
		Delete deleteUri = new Delete();
		deleteUri.table(Table.RedirectUris)
		.condition(RedirectUris.CLIENT_ID, Operators.Equals, uriId.toString());
		return deleteUri.executeUpdate() > 0;
	}
	
	public static void main(String[] args) throws QueryException, BuildException {
		RedirectUrisDao dao = new RedirectUrisDao();
		dao.addUri("faa58ca6-a709-42cb-ae1b-0672c43a1556", "http://localhost:4200/app/callback");
	
	}
}
;