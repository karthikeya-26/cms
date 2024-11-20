package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.GroupContactsObj;
import com.dbObjects.ResultObject;
import com.queryLayer.*;
import com.tables.GroupContacts;
import com.tables.Operators;
import com.tables.Table;

public class GroupContactsDao {
	//this table doesn't have updates
	public List<GroupContactsObj> getGroupContactIds(Integer groupId){
		List<GroupContactsObj> groupContacts = new ArrayList<GroupContactsObj>();
		Select s = new Select();
		s.table(Table.GroupContacts)
		.condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString());
		List<ResultObject> resultList = s.executeQuery(GroupContactsObj.class);
		for(ResultObject contact : resultList) {
			groupContacts.add((GroupContactsObj) contact);
		}
		resultList = null;
		return groupContacts;
	}
	
	// INSERT
	public boolean addContactToGroup(Integer groupId, Integer contactId, Long addedAt) {
		Insert insertContactIntoGroup = new Insert();
		insertContactIntoGroup.table(Table.GroupContacts)
				.columns(GroupContacts.GROUP_ID, GroupContacts.CONTACT_ID)
				.values(groupId.toString(), contactId.toString());
		return insertContactIntoGroup.executeUpdate() > 0;

	}
	// DELETE
	public boolean deleteContactFromGroup(Integer groupId, Integer contactId) {
		Delete d = new Delete();
		d.table(Table.GroupContacts).condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString())
				.condition(GroupContacts.CONTACT_ID, Operators.Equals, contactId.toString());
		return d.executeUpdate() > 0;
	}
	
	//VALIDATION
	public boolean isContactInGroup(Integer groupId, Integer contactId) {
		Select s = new Select();
		s.table(Table.GroupContacts)
		.condition(GroupContacts.GROUP_ID, Operators.Equals, groupId.toString())
		.condition(GroupContacts.CONTACT_ID, Operators.Equals, contactId.toString());
		return s.executeQuery(GroupContactsObj.class).size()>0;
	}

}

