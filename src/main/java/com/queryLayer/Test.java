package com.queryLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbObjects.*;
import com.dbconn.Database;
import com.mchange.v2.sql.filter.SynchronizedFilterDataSource;
import com.tables.*;

public class Test {
	public static void main(String args[]) {
		
		//dao queries
		
//		1.Select queries
		
//		login user method
		Select getuserfromdb = new Select();
		getuserfromdb.table(Table.UserDetails).column(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "?");
//		System.out.println(getuserfromdb.build());
//		System.out.println();
		
		Select getusermails = new Select();
		getusermails.table(Table.UserMails).columns(UserMails.ALL_COLS).condition(UserMails.USER_ID, Operators.Equals, "1");
//		System.out.println(getusermails.build());
//		System.out.println();
		
		Select checkifMailExists = new Select();
		checkifMailExists.table(Table.UserMails).column(UserMails.MAIL).condition(UserMails.USER_ID, Operators.Equals, "1");
//		System.out.println(checkifMailExists.build());
//		System.out.println();
		
		Select userCOntacts = new Select();
		userCOntacts.table(Table.Contacts, "c").columns("c", Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.ADDRESS)
		.groupConcatAs("m", ContactMobileNumbers.NUMBER, "mobile_number", ",")
		.groupConcatAs("cm", ContactMails.MAIL, "emails", ",")
		.join(Joins.LeftJoin, "m", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		.join(Joins.LeftJoin, "cm", ContactMails.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		.condition("c",Contacts.USER_ID, Operators.Equals, "1")
		.groupBy("c", Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
//		System.out.println(userCOntacts.build());
//		System.out.println();
		
		Select getuserGroups = new Select();
		getuserGroups.table(Table.UserGroups).column(UserGroups.GROUP_NAME).condition(UserGroups.USER_ID, Operators.Equals, "1");
//		System.out.println(getuserGroups.build());
//		System.out.println();
		
		Select sa= new Select();
		     sa.table(Table.Contacts, "c")
		    .columns("c",Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.ADDRESS, Contacts.USER_ID)
		    .groupConcatAs("cm",ContactMobileNumbers.NUMBER, "mobile_numbers", ",")
		    .groupConcatAs("ma",ContactMails.MAIL, "mails", ",")
		    .groupConcatAs("ug",UserGroups.GROUP_NAME, "user_groups", ",")
		    .join(Joins.LeftJoin, "cm", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		    .join(Joins.LeftJoin,"ma", ContactMails.CONTACT_ID,Operators.Equals, "c", Contacts.CONTACT_ID)
		    .join(Joins.InnerJoin, "gc", GroupContacts.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		    .join(Joins.InnerJoin, "ug", UserGroups.GROUP_ID, Operators.Equals, "gc",GroupContacts.GROUP_ID)
		    .condition("c",Contacts.USER_ID, Operators.Equals, "?").groupBy("c",Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
		     
		//Insert 
		     
		Insert userdetails = new Insert();
		userdetails.table(Table.UserDetails).columns(UserDetails.USER_NAME,UserDetails.PASSWORD,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
		.values("a","a","a","a","a");
//		System.out.println(userdetails.build());
		
		Select use= new Select();
		use.table(Table.UserDetails)
		.columns(UserDetails.USER_ID,UserDetails.USER_NAME,UserDetails.PASSWORD,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
		.join(Joins.InnerJoin, Table.UserMails,UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID)
		.condition(UserMails.MAIL, Operators.Equals,"j@x.com");
		
//		List<HashMap<String, Object>> result = use.executeQuery();
//		System.out.println(result);
//		HashMap<String, Object> user=  result.get(0);
//		System.out.println(user);
		
//		try(Connection c = Database.getConnection()){
//			String statement = "select u.user_id, u.user_name, u.password, u.first_name, u.last_name, u.contact_type, m.mail from UserDetails u inner join UserMails m on u.user_id = m.user_id where m.mail='j@x.com';";
//			PreparedStatement ps = c.prepareStatement(statement);
//			ResultSet rs= ps.executeQuery();
//			System.out.println();
//			System.out.println("Result set object");
//			System.out.println(rs);
//			System.out.println("ResultSet Metadata :");
//			System.out.println(rs.getMetaData());
//		}catch(SQLException e) {
//			System.out.println("mysql error");
//		}
		
//		Select s = new Select();
//		s.table(Table.UserDetails).columns(UserDetails.ALL_COLS).condition(UserDetails.USER_ID, Operators.Equals,
//				"1");
//		List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
//		System.out.println(users);
//		UserDetailsObj user = (UserDetailsObj) users.get(0);
//		System.out.println(user);
//		
//		List<UserGroupsObj> user_groups = new ArrayList<>() ;
//		Select select_user_groups = new Select();
//		select_user_groups.table(Table.UserGroups).columns(UserGroups.GROUP_ID,UserGroups.GROUP_NAME,UserGroups.USER_ID)
//		.condition(UserGroups.USER_ID, Operators.Equals, "1");
//		List<ResultObject>  usergroups =  select_user_groups.executeQuery(UserGroupsObj.class);
//		for(ResultObject group : usergroups) {
//			user_groups.add((UserGroupsObj) group);
//		}
//		System.out.println(user_groups);
//		Select group_contact = new Select();
//		group_contact.table(Table.GroupContacts).columns(GroupContacts.GROUP_ID,GroupContacts.CONTACT_ID)
//		.condition(GroupContacts.GROUP_ID, Operators.Equals, "11")
//		.condition(GroupContacts.CONTACT_ID, Operators.Equals,"2");
//		List<ResultObject> resultset = group_contact.executeQuery(GroupContactsObj.class);
//		System.out.println(resultset);
		
//		Delete  deletecontactfromgroup = new Delete();
//		deletecontactfromgroup.table(Table.GroupContacts)
//		.condition(GroupContacts.GROUP_ID, Operators.Equals, "11")
//		.condition(GroupContacts.CONTACT_ID, Operators.Equals, "2");
//		System.out.println(deletecontactfromgroup.build());
		Select sessionsfromdb = new Select();
		sessionsfromdb.table(Table.Sessions).columns(Sessions.SESSION_ID,Sessions.USER_ID,Sessions.CREATED_AT,Sessions.LAST_ACCESSED_TIME);
		System.out.println(sessionsfromdb.build());
	}

}
