package com.dao;
import java.util.List;

import com.dbObjects.AccessTokensObj;
import com.dbObjects.ResultObject;
import com.enums.*;
import com.queryLayer.*;

public class AccessTokensDao {
	//SELECT 
	public AccessTokensObj getAccessTokenObject(String accessToken) throws QueryException {
		Select getaccessTokenObject = new Select();
		getaccessTokenObject.table(Table.AccessTokens).condition(AccessTokens.ACCESS_TOKEN, Operators.Equals, accessToken);
		
		List<ResultObject> resultObjects = getaccessTokenObject.executeQuery(AccessTokensObj.class);
		return resultObjects.size() >0 ? (AccessTokensObj) resultObjects.get(0) : null;
	}
	
	//INSERT 
	public boolean addAccessToken(String accessToken, String scopes, Integer userId, String clientId,Integer refTokenId) throws QueryException {
		Insert addAccessToken = new Insert();
		addAccessToken.table(Table.AccessTokens)
		.columns(AccessTokens.ACCESS_TOKEN,AccessTokens.SCOPES,AccessTokens.USER_ID,AccessTokens.CLIENT_ID)
		.values(accessToken, scopes,userId.toString(),clientId);
		
		if(refTokenId != null) {
			addAccessToken.columns(AccessTokens.REFTOKEN_ID);
			addAccessToken.values(refTokenId.toString());
		}
		return addAccessToken.executeUpdate()>0;

	}
	
	//UPDATE
	public boolean updateAccessToken() {
		return false;
	}
	
	//DELETE 
	public boolean deleteAccessToken(String accessToken) throws QueryException {
		Delete deleteToken = new Delete();
		deleteToken.table(Table.AccessTokens).condition(AccessTokens.ACCESS_TOKEN, Operators.Equals, accessToken);
		return deleteToken.executeUpdate()>0;
	}
//	public static void main(String[] args) throws QueryException {
//		AccessTokensDao dao = new AccessTokensDao();
//		dao.addAccessToken("acc123", "profile", 1, "client123");
//	}
}
