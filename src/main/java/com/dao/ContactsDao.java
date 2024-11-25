package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ContactsObj;
import com.dbObjects.ResultObject;
import com.enums.Contacts;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
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
	
	//SELECT -> ALL Contacts with userID
	public List<ContactsObj> getContactsWithUserId(Integer userId){
		List<ContactsObj> userContacts = new ArrayList<ContactsObj>();
		Select s = new Select();
		s.table(Table.Contacts).columns(Contacts.CONTACT_ID,Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.ADDRESS, Contacts.CREATED_AT,Contacts.USER_ID)
		.condition(Contacts.USER_ID, Operators.Equals,userId.toString());

		List<ResultObject> contacts = s.executeQuery(ContactsObj.class);

		for (ResultObject res : contacts) {
			ContactsObj c = (ContactsObj) res;
			userContacts.add(c);
		}
		return userContacts;
	}
	
	//UPDATE
	public boolean UpdateContact(Integer contactId, String firstName, String lastName, String address ) {
		Update updateContact = new Update();
		updateContact.table(Table.Contacts)
		.columns(Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.ADDRESS)
		.values(firstName,lastName,address)
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
}
