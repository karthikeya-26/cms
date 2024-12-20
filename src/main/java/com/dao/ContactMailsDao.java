package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.enums.ContactMails;
import com.enums.Operators;
import com.enums.Table;
import com.enums.UserMails;
import com.queryLayer.*;

public class ContactMailsDao {
	
	//SELECT 
	public List<ContactMailsObj> getMailsWithContactId(Integer contactId) throws Exception{
		List<ContactMailsObj> mails = new ArrayList<ContactMailsObj>();
		Select s = new Select();
		s.table(Table.ContactMails)
		.condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString());
		System.out.println(s.build());
		List<ResultObject> resultList = s.executeQuery(ContactMailsObj.class);
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
	public boolean checkIfMailExistsForContact(Integer contactId, String mail) throws Exception {
		Select s = new Select();
		s.table(Table.ContactMails)
		.condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(ContactMails.MAIL, Operators.Equals, mail);
		List<ResultObject> contacts = s.executeQuery(ContactMailsObj.class);
		return contacts.size()>0;
	}
}
