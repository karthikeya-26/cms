package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.*;
import com.enums.ContactMails;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.*;


public class ContactMailsDao {
	
	// SELECT 
	public List<ContactMailsObj> getMailsWithContactId(Integer contactId) throws DaoException {
		List<ContactMailsObj> mails = new ArrayList<>();
		try {
			Select s = new Select();
			s.table(Table.ContactMails)
			 .condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString());
			List<ResultObject> resultList = s.executeQuery(ContactMailsObj.class);
			for (ResultObject mail : resultList) {
				mails.add((ContactMailsObj) mail);
			}
		} catch (QueryException e) {
			throw new DaoException("Failed to fetch mails for contact ID: " + contactId, e);
		}
		return mails;
	}
	
	// INSERT
	public boolean addMailToContact(Integer contactId, String mail) throws DaoException {
		try {
			Insert i = new Insert();
			i.table(Table.ContactMails)
			 .columns(ContactMails.CONTACT_ID, ContactMails.MAIL)
			 .values(contactId.toString(), mail);
			return i.executeUpdate() > 0;
		} catch (QueryException e) {
			throw new DaoException("Failed to add mail to contact ID: " + contactId, e);
		}
	}
	
	// UPDATE 
	public boolean updateMailForContact(Integer contactId, String mail) throws DaoException {
		try {
			Update u = new Update();
			u.table(Table.ContactMails)
			 .columns(ContactMails.CONTACT_ID, ContactMails.MAIL)
			 .values(contactId.toString(), mail);
			return u.executeUpdate() == 0;
		} catch (QueryException e) {
			throw new DaoException("Failed to update mail for contact ID: " + contactId, e);
		}
	}
	
	// DELETE
	public boolean deleteMailForContact(Integer contactId, String mail) throws DaoException {
		try {
			Delete d = new Delete();
			d.table(Table.ContactMails)
			 .condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString())
			 .condition(ContactMails.MAIL, Operators.Equals, mail);
			return d.executeUpdate() > 0;
		} catch (QueryException e) {
			throw new DaoException("Failed to delete mail for contact ID: " + contactId, e);
		}
	}
	
	// VALIDATION 
	public boolean checkIfMailExistsForContact(Integer contactId, String mail) throws DaoException {
		try {
			Select s = new Select();
			s.table(Table.ContactMails)
			 .condition(ContactMails.CONTACT_ID, Operators.Equals, contactId.toString())
			 .condition(ContactMails.MAIL, Operators.Equals, mail);
			List<ResultObject> contacts = s.executeQuery(ContactMailsObj.class);
			return contacts.size() > 0;
		} catch (QueryException e) {
			throw new DaoException("Failed to validate existence of mail for contact ID: " + contactId, e);
		}
	}
}
