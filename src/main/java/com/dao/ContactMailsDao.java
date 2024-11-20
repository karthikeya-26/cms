package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.queryLayer.*;
import com.tables.ContactMails;
import com.tables.Operators;
import com.tables.Table;
import com.tables.UserMails;

public class ContactMailsDao {
	
	//SELECT 
	public List<ContactMailsObj> getMailsWithContactId(Integer contactId){
		List<ContactMailsObj> mails = new ArrayList<ContactMailsObj>();
		Select s = new Select();
		s.table(Table.ContactMails)
		.condition(UserMails.USER_ID, Operators.Equals, contactId.toString());
		List<ResultObject> resultList = s.executeQuery(UserMailsObj.class);
		for(ResultObject mail : resultList) {
			mails.add((ContactMailsObj) mail);
		}
		return mails;
	}
	
	//INSERT
	public boolean addMailToContact(Integer contactId, String mail) {
		Insert i = new Insert();
		i.table(Table.ContactMails)
		.columns(ContactMails.CONTACT_ID,ContactMails.MAIL)
		.values(contactId.toString(),mail);
		return i.executeUpdate()>0;
	}
	
	//UPDATE 
	public boolean updateMailForContact(Integer contactId, String mail) {
		Update u = new Update();
		u.table(Table.ContactMails)
		.columns(ContactMails.CONTACT_ID,ContactMails.MAIL)
		.values(contactId.toString(),mail);
		return u.executeUpdate()==0;
		
	}
	
	//DELETE
	public boolean deleteMailForContact(Integer contactId, String mail) {
		Delete d = new Delete();
		d.table(Table.ContactMails)
		.condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(ContactMails.MAIL, Operators.Equals, mail);
		return d.executeUpdate() > 0;
	}
	
	//VALIDATION 
	public boolean checkIfMailExistsForContact(Integer contactId, String mail) {
		Select s = new Select();
		s.table(Table.ContactMails)
		.condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(ContactMails.MAIL, Operators.Equals, mail);
		List<ResultObject> contacts = s.executeQuery(ContactMailsObj.class);
		return contacts.size()>0;
	}
}
