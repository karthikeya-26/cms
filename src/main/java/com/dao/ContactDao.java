package com.dao;

import java.util.List;

import com.dbObjects.ContactsObj;
import com.dbObjects.ResultObject;
import com.queryLayer.Select;
import com.queryLayer.Update;
import com.tables.Contacts;
import com.tables.Operators;
import com.tables.Table;

public class ContactDao {
	
	public static ContactsObj getContactById(Integer contact_id) {
		Select getContact = new Select();
		getContact.table(Table.Contacts)
		.condition(Contacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		List<ResultObject> contacts = getContact.executeQuery(ContactsObj.class);
		ContactsObj contact = (ContactsObj) contacts.get(0);
		return contact;
	}
	
	public static void UpdateContact(Integer contactId, String firstName, String lastName, String address ) {
		Update updateContact = new Update();
		updateContact.table(Table.Contacts)
		.columns(Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.ADDRESS)
		.values(firstName,lastName,address)
		.condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString());
		System.out.println(updateContact.build());
	}
	
}
