package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dbObjects.ResultObject;
import com.dbObjects.UserGroupsObj;
import com.enums.Columns;
import com.enums.GroupContacts;
import com.enums.Joins;
import com.enums.Operators;
import com.enums.Table;
import com.enums.UserGroups;
import com.queryBuilder.BuildException;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class UserGroupsDao {

    // SELECT -> Groups with userId
    public List<UserGroupsObj> getUserGroups(Integer userId) throws DaoException {
        List<UserGroupsObj> userGroups = new ArrayList<>();
        try {
            Select selectUserGroups = new Select();
            selectUserGroups.table(Table.UserGroups)
                            .condition(UserGroups.USER_ID, Operators.Equals, userId.toString());
            List<ResultObject> userGroupsList = selectUserGroups.executeQuery(UserGroupsObj.class);
            for (ResultObject group : userGroupsList) {
                userGroups.add((UserGroupsObj) group);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching user groups for userId: " + userId, e);
        }
        return userGroups;
    }
    //Select -> contact groups
    public List<UserGroupsObj> getContactGroups(int contactId) throws DaoException, BuildException{
    	List<UserGroupsObj> contactGroups =new ArrayList<UserGroupsObj>();
    	Select getContactGroups = new Select();
    	getContactGroups.table(Table.UserGroups)
    	.join(Joins.InnerJoin, Table.GroupContacts, GroupContacts.GROUP_ID, Operators.Equals, Table.UserGroups, UserGroups.GROUP_ID)
    	.condition(GroupContacts.CONTACT_ID, Operators.Equals, String.valueOf(contactId));
    	
    	System.out.println(getContactGroups.build());
    	try {
			List<ResultObject> resultList = getContactGroups.executeQuery(UserGroupsObj.class);
			for (ResultObject group : resultList) {
                contactGroups.add((UserGroupsObj) group);
            }
		} catch (QueryException e) {
			e.printStackTrace();
            throw new DaoException("Error fetching user groups for contact groups: " + contactId, e);
		}
    	return contactGroups;
    }
    
    public static void main(String[] args) throws DaoException, BuildException {
		UserGroupsDao dao = new UserGroupsDao();
		System.out.println(dao.getContactGroups(2));
	}
    // INSERT
    public boolean addGroupToUser(Integer userId, String groupName) throws DaoException {
        try {
            Insert insert = new Insert();
            insert.table(Table.UserGroups)
                  .columns(UserGroups.USER_ID, UserGroups.GROUP_NAME)
                  .values(userId.toString(), groupName);
            return insert.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding group to user", e);
        }
    }

    // UPDATE
    public boolean updateGroupName(Integer userId,Integer groupId, String groupName) throws DaoException {
    	if(!checkIfGroupBelongsToUser(userId, groupId)) {
    		throw new DaoException("Group doesn't belong to user");
    	}
        try {
            Update update = new Update();
            update.table(Table.UserGroups)
                  .columns(UserGroups.GROUP_NAME)
                  .values(groupName)
                  .condition(UserGroups.GROUP_ID, Operators.Equals, groupId.toString());
            return update.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Error updating group name for groupId: " + groupId, e);
        }
    }

    // DELETE
    public boolean deleteGroupForUser(Integer userId,Integer groupId) throws DaoException {
        if(!checkIfGroupBelongsToUser(userId, groupId)) {
        	throw new DaoException("Group doesn't belong to the user");
        }
    	try {
            Delete deleteGroup = new Delete();
            deleteGroup.table(Table.UserGroups)
                        .condition(UserGroups.GROUP_ID, Operators.Equals, groupId.toString());
            return deleteGroup.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting group for groupId: " + groupId, e);
        }
    }

    // VALIDATION

    // TO ADD GROUP
    public boolean checkIfGroupExistsForUser(Integer userId, String groupName) throws DaoException {
        try {
            Select select = new Select();
            select.table(Table.UserGroups)
                  .columns(UserGroups.GROUP_NAME)
                  .condition(UserGroups.USER_ID, Operators.Equals, userId.toString())
                  .condition(UserGroups.GROUP_NAME, Operators.Equals, groupName);
            List<Map<Columns, Object>> userGroups = select.executeQuery();
            return !userGroups.isEmpty();
        } catch (QueryException e) {
            throw new DaoException("Error checking if group exists for userId: " + userId, e);
        }
    }

    // TO CHECK IF GROUP ACTUALLY BELONGS TO USER while updating
    public static boolean checkIfGroupBelongsToUser(Integer userId, Integer groupId) throws DaoException {
        try {
            Select select = new Select();
            select.table(Table.UserGroups)
                  .columns(UserGroups.GROUP_NAME)
                  .condition(UserGroups.USER_ID, Operators.Equals, userId.toString())
                  .condition(UserGroups.GROUP_ID, Operators.Equals, groupId.toString());
            return !select.executeQuery().isEmpty();
        } catch (QueryException e) {
            throw new DaoException("Error checking if group belongs to userId: " + userId + " and groupId: " + groupId, e);
        }
    }

}
