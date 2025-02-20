package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ContactMobileNumbersObj;
import com.dbObjects.ResultObject;
import com.enums.ContactMobileNumbers;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.*;

public class ContactMobileNumbersDao {

    // SELECT
    public List<ContactMobileNumbersObj> getContactMobileNumbers(Integer contactId) throws DaoException {
        List<ContactMobileNumbersObj> numbers = new ArrayList<>();
        Select s = new Select();
        s.table(Table.ContactMobileNumbers)
         .condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString());
        try {
            List<ResultObject> resultlist = s.executeQuery(ContactMobileNumbersObj.class);
            for (ResultObject number : resultlist) {
                numbers.add((ContactMobileNumbersObj) number);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching contact mobile numbers for contactId: " + contactId, e);
        }
        return numbers;
    }

    // INSERT
    public boolean addNumberToContactId(Integer contactId, String number) throws DaoException {
        Insert i = new Insert();
        i.table(Table.ContactMobileNumbers)
         .columns(ContactMobileNumbers.CONTACT_ID, ContactMobileNumbers.NUMBER)
         .values(contactId.toString(), number);
        try {
            return i.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding number to contactId: " + contactId, e);
        }
    }

    // UPDATE
    public boolean updateNumberWithContactId(Integer contactId, String oldNumber, String newNumber) throws DaoException {
        Update u = new Update();
        u.table(Table.ContactMobileNumbers)
         .columns(ContactMobileNumbers.NUMBER)
         .values(newNumber)
         .condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString())
         .condition(ContactMobileNumbers.NUMBER, Operators.Equals, oldNumber);
        try {
            return u.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Error updating number for contactId: " + contactId, e);
        }
    }

    // DELETE
    public boolean deleteNumberWithContactId(Integer contactId, String number) throws DaoException {
        Delete d = new Delete();
        d.table(Table.ContactMobileNumbers)
         .condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString())
         .condition(ContactMobileNumbers.NUMBER, Operators.Equals, number);
        try {
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting number for contactId: " + contactId, e);
        }
    }

    // VALIDATION
    public boolean checkNumberWithContactId(Integer contactId, String number) throws DaoException {
        Select s = new Select();
        s.table(Table.ContactMobileNumbers)
         .columns(ContactMobileNumbers.CONTACT_ID, ContactMobileNumbers.NUMBER)
         .condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString())
         .condition(ContactMobileNumbers.NUMBER, Operators.Equals, number);
        try {
            return s.executeQuery().size() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error validating number for contactId: " + contactId, e);
        }
    }
}
