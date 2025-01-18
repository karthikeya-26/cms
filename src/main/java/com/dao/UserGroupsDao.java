package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.UserGroupsObj;
import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.enums.UserGroups;
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
            return update.executeUpdate() > 0;
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
            List<HashMap<Columns, Object>> userGroups = select.executeQuery();
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
