package com.dao;

import java.util.*;

import com.queryLayer.*;

import com.dbObjects.*;
import com.enums.AuthorizationCodes;
import com.enums.Operators;
import com.enums.Table;

public class AuthorizationCodesDao {
	
	//SELECT 
	public AuthorizationCodesObj getAuthorizationCodesObj(String authCode) throws QueryException {
		Select getAuthCode = new Select();
		getAuthCode.table(Table.AuthorizationCodes)
		.condition(AuthorizationCodes.AUTHORIZATION_CODE, Operators.Equals, authCode);
		List<ResultObject> authCodeObjects = getAuthCode.executeQuery(AuthorizationCodesObj.class);
		return authCodeObjects.isEmpty() == true ? null : (AuthorizationCodesObj) authCodeObjects.get(0);
	}
	
	//INSERT
	public boolean addAuthorizationCode(String authorizationCode, String clientId, Integer userId, String scopes, String accessType) throws QueryException {
		Insert addCode = new Insert();
		addCode.table(Table.AuthorizationCodes).columns(AuthorizationCodes.AUTHORIZATION_CODE,AuthorizationCodes.CLIENT_ID,AuthorizationCodes.USER_ID,AuthorizationCodes.SCOPES,AuthorizationCodes.ACCESS_TYPE)
		.values(authorizationCode,clientId,userId.toString(),scopes,accessType);
		return addCode.executeUpdate() >0;
	}
	
	public static void main(String[] args) throws QueryException {
		AuthorizationCodesDao dao = new AuthorizationCodesDao();
		System.out.println(dao.getAuthorizationCodesObj("karthik"));
		
		
	}
	
	//UPDATE --> Not Supported
	
	//DELETE
	public boolean deleteAuthCode(String authorizationCode) throws QueryException {
		Delete deleteCode = new Delete();
		deleteCode.table(Table.AuthorizationCodes).condition(AuthorizationCodes.AUTHORIZATION_CODE, Operators.Equals, authorizationCode);
		return deleteCode.executeUpdate()>0;
	}
	
	public boolean deleteUserAuthCodes(Integer userId) throws QueryException{
		Delete deleteUserCodes = new Delete();
		deleteUserCodes.table(Table.AuthorizationCodes).condition(AuthorizationCodes.USER_ID, Operators.Equals, userId.toString());
		return deleteUserCodes.executeUpdate()>0;
	}
	
	public boolean deleteClientCodes(Integer userId) throws QueryException{
		Delete deleteClientCodes = new Delete();
		deleteClientCodes.table(Table.AuthorizationCodes).condition(AuthorizationCodes.USER_ID, Operators.Equals, userId.toString());
		return deleteClientCodes.executeUpdate()>0;
	}
}
