package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ContactsSyncObj;
import com.dbObjects.ResultObject;
import com.enums.ContactsSync;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.Select;

public class ContactsSyncDao {
	
	//Select refresh tokens of a user
	
	public List<ContactsSyncObj> getUserSyncTokens(Integer userId){
		List<ContactsSyncObj> tokens = new ArrayList<ContactsSyncObj>();
		Select s = new Select();
		s.table(Table.ContactsSync).condition(ContactsSync.USER_ID, Operators.Equals, userId.toString());
		List<ResultObject> result = s.executeQuery(ContactsSyncObj.class);
		if(result.size()>0) {
			for (ResultObject token : result) {
				tokens.add((ContactsSyncObj)token);
			}
		}
		return tokens;
	}
	
	//INSERT 
	
	public boolean addRefreshTokenToUser(Integer userId, String refreshToken, String provider) {
		Insert i = new Insert();
		i.table(Table.ContactsSync).columns(ContactsSync.USER_ID,ContactsSync.REFRESH_TOKEN, ContactsSync.PROVIDER);
		i.values(userId.toString(),refreshToken,provider);
		return i.executeUpdate()>0;
	}
	
	//NO updates TO tokens
	
	//DELETE
	public boolean deleteRefreshToken(Integer userId,String refreshToken) {
		Delete d = new Delete();
		d.table(Table.ContactsSync).condition(ContactsSync.USER_ID, Operators.Equals, userId.toString())
		.condition(ContactsSync.REFRESH_TOKEN, Operators.Equals, refreshToken);
		return d.executeUpdate()>0;
	}
}
