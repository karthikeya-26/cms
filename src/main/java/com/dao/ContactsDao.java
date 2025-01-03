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
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class ContactsDao {

    // SELECT -> Contact with its ID
    public ContactsObj getContactWithId(Integer contactId) throws DaoException {
        try {
            Select getContact = new Select();
            getContact.table(Table.Contacts)
                    .condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString());
            List<ResultObject> contacts = getContact.executeQuery(ContactsObj.class);
            if (contacts.isEmpty()) {
                throw new DaoException("No contact found with ID: " + contactId);
            }
            return (ContactsObj) contacts.get(0);
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch contact with ID: " + contactId, e);
        }
    }

    public ContactsObj getContactWithRefId(String refId) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.Contacts)
                    .condition(Contacts.REF_ID, Operators.Equals, refId);
            List<ResultObject> contacts = s.executeQuery(ContactsObj.class);
            if (contacts.isEmpty()) {
                throw new DaoException("No contact found with Ref ID: " + refId);
            }
            return (ContactsObj) contacts.get(0);
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch contact with Ref ID: " + refId, e);
        }
    }

    // SELECT -> ALL Contacts with userID
    public List<ContactsObj> getContactsWithUserId(Integer userId) throws DaoException {
        try {
            List<ContactsObj> userContacts = new ArrayList<>();
            Select s = new Select();
            s.table(Table.Contacts)
                    .condition(Contacts.USER_ID, Operators.Equals, userId.toString());

            List<ResultObject> contacts = s.executeQuery(ContactsObj.class);
            for (ResultObject res : contacts) {
                userContacts.add((ContactsObj) res);
            }
            return userContacts;
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch contacts for User ID: " + userId, e);
        }
    }
    
    // SELECT -> GET CONTACTS WITH USER ID SORTED
    public List<ContactsObj> getContactsWithUserIdSorted(Integer userId) throws DaoException{
    	try {
            List<ContactsObj> userContacts = new ArrayList<>();
            Select s = new Select();
            s.table(Table.Contacts)
                    .condition(Contacts.USER_ID, Operators.Equals, userId.toString())
                    .orderBy(Contacts.FIRST_NAME,Contacts.LAST_NAME);

            List<ResultObject> contacts = s.executeQuery(ContactsObj.class);
            for (ResultObject res : contacts) {
                userContacts.add((ContactsObj) res);
            }
            return userContacts;
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch sorted contacts for User ID: " + userId, e);
        }
    }

    // INSERT
    public int addContact(String firstName, String lastName, Integer userId) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.Contacts).columns(Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.USER_ID)
                    .values(firstName, lastName, userId.toString());
            return i.executeUpdate(true);
        } catch (QueryException e) {
            throw new DaoException("Failed to add contact for User ID: " + userId, e);
        }
    }

    // UPDATE
    public boolean updateContact(Integer contactId, String firstName, String lastName) throws DaoException {
        try {
            Update updateContact = new Update();
            updateContact.table(Table.Contacts)
                    .columns(Contacts.FIRST_NAME, Contacts.LAST_NAME)
                    .values(firstName, lastName)
                    .condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString());
            return updateContact.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Failed to update contact with ID: " + contactId, e);
        }
    }

    // DELETE
    public boolean deleteContact(Integer contactId) throws DaoException {
        try {
            Delete deleteContact = new Delete();
            deleteContact.table(Table.Contacts).condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString());
            return deleteContact.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Failed to delete contact with ID: " + contactId, e);
        }
    }

    // VALIDATION
    public boolean checkIfContactIsUsers(Integer contactId, Integer userId) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.Contacts)
                    .condition(Contacts.CONTACT_ID, Operators.Equals, contactId.toString())
                    .condition(Contacts.USER_ID, Operators.Equals, userId.toString());
            return !s.executeQuery(ContactsObj.class).isEmpty();
        } catch (QueryException e) {
            throw new DaoException("Failed to validate contact ownership for User ID: " + userId, e);
        }
    }

    public HashMap<String, Long> getExistingContacts(Integer userId, String refreshToken) throws DaoException {
        try {
            HashMap<String, Long> refIdsAndModifiedTimes = new HashMap<>();
            Select s = new Select();
            s.table(Table.Contacts)
                    .columns(Contacts.REF_ID, Contacts.MODIFIED_AT)
                    .condition(Contacts.USER_ID, Operators.Equals, userId.toString())
                    .condition(Contacts.REFRESH_TOKEN, Operators.Equals, refreshToken);
            List<HashMap<Columns, Object>> result = s.executeQuery();
            for (HashMap<Columns, Object> row : result) {
                String refId = (String) row.get(Contacts.REF_ID);
                Long modifiedTime = (Long) row.get(Contacts.MODIFIED_AT);
                if (refId != null && modifiedTime != null) {
                    refIdsAndModifiedTimes.put(refId, modifiedTime);
                }
            }
            return refIdsAndModifiedTimes;
        } catch (QueryException e) {
            throw new DaoException("Failed to fetch existing contacts for User ID: " + userId, e);
        }
    }

	public int syncAddContact(String firstName, String lastName, Integer integer, String resourceName,long epochMilli) {
		return 0;
	}
	

}
