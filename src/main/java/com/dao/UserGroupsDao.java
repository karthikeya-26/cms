package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.UserGroupsObj;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.Select;
import com.queryLayer.Update;
import com.tables.Columns;
import com.tables.Operators;
import com.tables.Table;
import com.tables.UserGroups;

public class UserGroupsDao {
	
	//SELECT -> Groups with userId
	public List<UserGroupsObj> getUserGroups(Integer user_id){
		List<UserGroupsObj> user_groups = new ArrayList<>() ;
		Select select_user_groups = new Select();
		select_user_groups.table(Table.UserGroups).columns(UserGroups.GROUP_ID,UserGroups.GROUP_NAME,UserGroups.USER_ID)
		.condition(UserGroups.USER_ID, Operators.Equals, user_id.toString());
		List<ResultObject>  usergroups =  select_user_groups.executeQuery(UserGroupsObj.class);
		for(ResultObject group : usergroups) {
			user_groups.add((UserGroupsObj) group);
		}
		return user_groups;
	}
	
	//INSERT
	public boolean addGroupToUser(Integer userId,String groupName) {
		Insert i = new Insert();
		i.table(Table.UserGroups).columns(UserGroups.USER_ID,UserGroups.GROUP_NAME)
		.values(userId.toString(), groupName);
		return i.executeUpdate()>0;
	}
	
	//UPDATE
	public boolean updateGroupName(Integer groupId, String groupName) {
		Update u = new Update();
		u.table(Table.UserGroups).columns(UserGroups.GROUP_NAME).values(groupName)
		.condition(UserGroups.GROUP_ID, Operators.Equals, groupId.toString());
		return u.executeUpdate()==0 ;
	}
	
	//DELETE
	public boolean deleteGroupForUser(Integer groupId) {
		Delete deleteGroup = new Delete();
		deleteGroup.table(Table.UserGroups)
		.condition(UserGroups.GROUP_ID, Operators.Equals, groupId.toString());
		deleteGroup.build();
		return  deleteGroup.executeUpdate()>0;
	
	}
	
	//VALIDATION
	
	//TO ADD GROUP
	public boolean checkifGroupExistForUser(Integer userId,String groupName) {
		Select s = new Select();
		s.table(Table.UserGroups).column(UserGroups.GROUP_NAME)
		.condition(UserGroups.USER_ID, Operators.Equals, userId.toString())
		.condition(UserGroups.GROUP_NAME, Operators.Equals, groupName);
		List<HashMap<Columns, Object>> groupsofuser = s.executeQuery();
		return groupsofuser.size()>0;
	}
	
	//TO CHECK IF GROUP ACTUALLY BELONG TO USER wHILE UPDATION
	public static boolean checkIfGroupBelongsToUser(Integer userId, Integer groupId) {
		Select s = new Select();
		s.table(Table.UserGroups)
		.columns(UserGroups.GROUP_NAME)
		.condition(UserGroups.USER_ID, Operators.Equals, userId.toString())
		.condition(UserGroups.GROUP_ID,Operators.Equals, groupId.toString());
		return s.executeQuery().size()>0;
	}
}
