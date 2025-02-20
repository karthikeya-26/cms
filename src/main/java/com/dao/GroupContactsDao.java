package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.GroupContactsObj;
import com.dbObjects.ResultObject;
import com.enums.GroupContacts;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;

public class GroupContactsDao {

    // This table doesn't have updates
    public List<GroupContactsObj> getGroupContactIds(Integer groupId) throws DaoException {
        List<GroupContactsObj> groupContacts = new ArrayList<>();
        try {
            Select s = new Select();
            s.table(Table.GroupContacts)
                    .condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString());
            List<ResultObject> resultList = s.executeQuery(GroupContactsObj.class);
            for (ResultObject contact : resultList) {
                groupContacts.add((GroupContactsObj) contact);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching group contact IDs", e);
        }
        return groupContacts;
    }

    // INSERT
    public boolean addContactToGroup(Integer groupId, Integer contactId) throws DaoException {
        try {
            Insert insertContactIntoGroup = new Insert();
            insertContactIntoGroup.table(Table.GroupContacts)
                    .columns(GroupContacts.GROUP_ID, GroupContacts.CONTACT_ID)
                    .values(groupId.toString(), contactId.toString());
            return insertContactIntoGroup.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding contact to group", e);
        }
    }

    // DELETE
    public boolean deleteContactFromGroup(Integer groupId, Integer contactId) throws DaoException {
        try {
            Delete d = new Delete();
            d.table(Table.GroupContacts).condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString())
                    .condition(GroupContacts.CONTACT_ID, Operators.Equals, contactId.toString());
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting contact from group", e);
        }
    }

    // VALIDATION
    public boolean isContactInGroup(Integer groupId, Integer contactId) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.GroupContacts)
                    .condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString())
                    .condition(GroupContacts.CONTACT_ID, Operators.Equals, contactId.toString());
            return s.executeQuery(GroupContactsObj.class).size() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error checking if contact is in group", e);
        }
    }
}
