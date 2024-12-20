package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbObjects.ContactsObj;
import com.dbObjects.ResultObject;
import com.enums.Contacts;
import com.enums.Operators;
import com.enums.Table;
import com.enums.Columns;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class ContactsDao {
	
	//SELECT -> Contact with its ID
	public ContactsObj getContactWithId(Integer contact_id) {
		Select getContact = new Select();
		getContact.table(Table.Contacts)
		.condition(Contacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		List<ResultObject> contacts = getContact.executeQuery(ContactsObj.class);
		ContactsObj contact = (ContactsObj) contacts.get(0);
		return contact;
	}
	
	public ContactsObj getContactwithRefId( String refId) {
		Select s = new Select();
		s.table(Table.Contacts)
		.condition(Contacts.REF_ID, Operators.Equals, refId );
		List<ResultObject> contacts  = s.executeQuery(ContactsObj.class);
		ContactsObj contact = (ContactsObj) contacts.get(0);
		return contact;
	}
	//SELECT -> ALL Contacts with userID
	public List<ContactsObj> getContactsWithUserId(Integer userId){
		List<ContactsObj> userContacts = new ArrayList<ContactsObj>();
		Select s = new Select();
		s.table(Table.Contacts)
		.condition(Contacts.USER_ID, Operators.Equals,userId.toString());

		List<ResultObject> contacts = s.executeQuery(ContactsObj.class);
//		System.out.println(contacts);
		for (ResultObject res : contacts) {
			ContactsObj c = (ContactsObj) res;
			userContacts.add(c);
		}
		return userContacts;
	}
	
	//INSERT
	
	public int addContact( String firstName,  String lastName, Integer userId) {
		Insert i = new Insert();
		i.table(Table.Contacts).columns(Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID)
		.values(firstName,lastName,userId.toString());
		
		return i.executeUpdate(true);
	}
	
	public int syncAddContact(String firstName, String lastName, Integer userId, String refId, Long modifiedAt) {
		Insert i = new Insert();
		i.table(Table.Contacts).columns(Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.REF_ID,Contacts.REFRESH_TOKEN)
		.values(firstName,lastName,userId.toString(),refId,modifiedAt.toString());
		return i.executeUpdate(true);
	}
	
	public static void main(String[] args) {
		ContactsDao dao = new ContactsDao();
		System.out.println(dao.getContactsWithUserId(1));
	}
	
	//UPDATE
	public boolean UpdateContact(Integer contactId, String firstName, String lastName ) {
		Update updateContact = new Update();
		updateContact.table(Table.Contacts)
		.columns(Contacts.FIRST_NAME,Contacts.LAST_NAME)
		.values(firstName,lastName)
		.condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString());
		System.out.println(updateContact.build());
		return updateContact.executeUpdate() == 0;
	}
	
	//DELETE
	public boolean deleteContact(Integer contact_id) {
		Delete deleteContact = new Delete();
		deleteContact.table(Table.Contacts).condition(Contacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		return  deleteContact.executeUpdate() > 0;
	}
	
	//VALIDATION 
	public boolean checkIfContactIsUsers(Integer contactId, Integer userId) {
		Select s = new Select();
		s.table(Table.Contacts)
		.condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(Contacts.USER_ID,Operators.Equals,userId.toString());
		return s.executeQuery(ContactsObj.class).size()>0;
	}

	public HashMap<String, Long> getExistingContacts(Integer userId, String refreshToken) {
		HashMap<String, Long> refIdsandModifiedTimes = new HashMap<String, Long>();
		Select s = new Select();
		s.table(Table.Contacts)
		.columns(Contacts.REF_ID,Contacts.MODIFIED_AT)
		.condition(Contacts.USER_ID, Operators.Equals, userId.toString())
		.condition(Contacts.REFRESH_TOKEN, Operators.Equals, refreshToken);
		List<HashMap<Columns,Object>> result = s.executeQuery();
		System.out.println("result"+result);
		for(HashMap<Columns, Object> row : result) {
			String refId = null;
			Long modifiedTime = null;
			if(row.get(Contacts.REF_ID)!= null) {
				refId = row.get(Contacts.REF_ID).toString();
			}
			if(row.get(Contacts.REFRESH_TOKEN) != null) {
				modifiedTime = (Long) row.get(Contacts.REFRESH_TOKEN);
			}
			
			if(refId !=null && modifiedTime != null) {
				refIdsandModifiedTimes.put(refId, modifiedTime);
			}
			
		}
		return refIdsandModifiedTimes;
	}
}
