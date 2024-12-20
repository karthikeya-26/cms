package com.queryLayer;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.ContactMailsDao;
import com.dao.ContactMobileNumbersDao;
import com.dao.ContactsDao;
import com.dao.NewDao;
import com.dao.UserDetailsDao;
import com.dbObjects.*;
import com.dbconn.Database;
import com.enums.*;
import com.filters.SessionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handlers.LoginHandler;
import com.loggers.AppLogger;
import com.util.PasswordMigration;
import com.util.PreExecuteTasks;

public class Test {
	public static void main(String args[]) {
		
		//dao queries
		
//		1.Select queries
		
//		login user method
//		Select getuserfromdb = new Select();
//		getuserfromdb.table(Table.UserDetails).columns(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "?");
//		System.out.println(getuserfromdb.build());
//		System.out.println();
		
//		Select getusermails = new Select();
//		getusermails.table(Table.UserMails).columns(UserMails.ALL_COLS).condition(UserGroups.USER_ID, Operators.Equals, "1");
//		System.out.println(getusermails.build());
//		System.out.println();
		
//		Select checkifMailExists = new Select();
//		checkifMailExists.table(Table.UserMails).columns(UserMails.MAIL).condition(UserGroups.USER_ID, Operators.Equals, "1");
//		System.out.println(checkifMailExists.build());
//		System.out.println();
		
//		Select userCOntacts = new Select();
//		userCOntacts.table(Table.Contacts).columns("c", Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.ADDRESS)
//		.groupConcatAs("m", ContactMobileNumbers.NUMBER, "mobile_number", ",")
//		.groupConcatAs("cm", ContactMails.MAIL, "emails", ",")
//		.join(Joins.LeftJoin, "m", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//		.join(Joins.LeftJoin, "cm", ContactMails.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//		.condition("c",Contacts.USER_ID, Operators.Equals, "1")
//		.groupBy("c", Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
//		System.out.println(userCOntacts.build());
//		System.out.println();
		
		Select s = new Select();
		s.table(Table.UserDetails).columns(UserDetails.USER_NAME,UserGroups.GROUP_NAME).join(Joins.InnerJoin, Table.UserGroups, UserGroups.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID);
//		System.out.println(s.build());
		Select getuserGroups = new Select();
		getuserGroups.table(Table.UserGroups).columns(UserGroups.GROUP_NAME).condition(UserGroups.USER_ID, Operators.Equals, "1");
//		System.out.println(getusermails.build());
//		System.out.println(getuserGroups.build());
//		System.out.println();
		
		Select sa= new Select();
//		     sa.table(Table.Contacts, "c")
//		    .columns("c",Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.ADDRESS, Contacts.USER_ID)
//		    .groupConcatAs("cm",ContactMobileNumbers.NUMBER, "mobile_numbers", ",")
//		    .groupConcatAs("ma",ContactMails.MAIL, "mails", ",")
//		    .groupConcatAs("ug",UserGroups.GROUP_NAME, "user_groups", ",")
//		    .join(Joins.LeftJoin, "cm", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//		    .join(Joins.LeftJoin,"ma", ContactMails.CONTACT_ID,Operators.Equals, "c", Contacts.CONTACT_ID)
//		    .join(Joins.InnerJoin, "gc", GroupContacts.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//		    .join(Joins.InnerJoin, "ug", UserGroups.GROUP_ID, Operators.Equals, "gc",GroupContacts.GROUP_ID)
//		    .condition("c",Contacts.USER_ID, Operators.Equals, "?").groupBy("c",Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
		     
		//Insert 
		     
//		Insert userdetails = new Insert();
//		userdetails.table(Table.UserDetails).columns(UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
//		.values("a","a","a","a");
//		System.out.println(userdetails.build());
		
//		Select use= new Select();
//		use.table(Table.UserDetails)
//		.columns(UserDetails.USER_ID,UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
//		.join(Joins.InnerJoin, Table.UserMails,UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID)
//		.condition(UserMails.MAIL, Operators.Equals,"j@x.com");
		
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
//		Select sessionsfromdb = new Select();
//		sessionsfromdb.table(Table.Sessions).columns(Sessions.SESSION_ID,Sessions.USER_ID,Sessions.CREATED_TIME,Sessions.LAST_ACCESSED_TIME);
//		System.out.println(sessionsfromdb.build());
//		System.out.println(sessionsfromdb.build());
		
//		Query query = new Insert().table(Table.UserDetails).columns(UserDetails.CONTACT_TYPE).values("public");
//		System.out.println(query.build());
//		Select sessionsfromdb = new Select();
//		sessionsfromdb.table(Table.Sessions).columns(Sessions.SESSION_ID,Sessions.USER_ID,Sessions.CREATED_TIME,Sessions.LAST_ACCESSED_TIME);
//		System.out.println(sessionsfromdb.build());
//		List<ResultObject> resultset  = sessionsfromdb.executeQuery(SessionObj.class);
//		System.out.println(resultset);
//		if(resultset.size() > 0) {
//			for(ResultObject row : resultset) {
//				SessionObj session = (SessionObj) row;
//				if(System.currentTimeMillis() <= session.getLast_accessed_at()+ 1000*60*30) {
//				}else {
//				}
//				
//			}
//		}
//		Update update_session = new Update();
//		update_session.table(Table.Sessions).columns(Sessions.LAST_ACCESSED_TIME).values("71323423423")
//		.condition(Sessions.SESSION_ID, Operators.Equals, "xyzzzz");
//		System.out.println(update_session.build());
//		NewDao.fetchSessionFromDb("6hkvm3cb36056mmfgnlab6ngq5");
//		System.out.println(NewDao.getRegisteredServers());
//		System.out.println(RegServer.getServerName());
//		SessionmapUpdateNotifier.sendSessionUpdate("a", System.currentTimeMillis());
//		
//		Query q = new Select().table(Table.ContactMails);
//		System.out.println(q.build());
//		HttpURLConnection connection = null;
//		try {
//			List<HashMap<String,Object>> servers = NewDao.getRegisteredServers();
//			System.out.println(servers);
//		
//				URL url = new URL("http://"+"localhost"+":"+"8080"+"/contacts/sru?action=SessionResourceDelete"+"&session_id="+"dadfgsfsdgfdbvbdver3y");
//				connection = (HttpURLConnection) url.openConnection();
//				connection.setRequestMethod("POST");
//				Integer response = connection.getResponseCode();
//				System.out.println(url.toString());
//			
//		}catch(Exception e) {
//			AppLogger.ApplicationLog(e);
//			
//		}
//		Update updateUser = new Update();
//		updateUser.table(Table.UserDetails).columns(UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
//		.values("jd","jd","jd","jd").condition(UserDetails.USER_ID, Operators.Equals, "1");
//		System.out.println(updateUser.build());
		
//		System.out.println(ContactDao.getContactById(2));
//		ContactDao.UpdateContact(2, "Emma", "Watson", "proddatur");
		
//		PasswordMigration.exportPasswords(13);	
		
//		Update u = new Update();
//		u.table(Table.UserDetails).columns(UserDetails.USER_NAME).values("karthik_04");
//		PreExecuteTasks p = new PreExecuteTasks();
//		Method[] meth = p.getClass().getDeclaredMethods();
//		
//		for(Method m: meth) {
//			System.out.println("Method name :"+m.getName());
//			try {
//				Class<?> [] params = m.getParameterTypes();
//				if(params.length==1 && Query.class.isAssignableFrom(params[0])) {
//					m.invoke(p, u);
//				}
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println(p.getResultMap());
//		System.out.println("hi");
//		System.out.println(LoginHandler.validateUser("j@x.com", "password123"));
//		UserDetailsDao dao = new UserDetailsDao();
//		System.out.println(dao.getUserWithMail("j@x.com"));
//		Update u = new Update();
//		u.table(Table.UserGroups).columns(UserGroups.GROUP_NAME).values("karthik").condition(UserGroups.GROUP_ID,Operators.Equals,"22");
//		u.executeUpdate();
//		Update a = new Update();
//		a.table(Table.UserDetails).columns(UserDetails.FIRST_NAME).values("sai thar").condition(UserDetails.USER_ID, Operators.Equals, "13");
//		a.executeUpdate();
//		Delete d = new Delete();
//		d.table(Table.UserGroups).condition(UserGroups.GROUP_ID, Operators.Equals, "22");
//		d.executeUpdate();
//		Insert i = new Insert();
//		i.table(Table.UserMails).columns(UserMails.MAIL,UserMails.USER_ID,UserMails.IS_PRIMARY).values("kart","13","0");
//		i.executeUpdate();
//		
//		Update u = new Update();
//		u.table(Table.UserDetails).columns(UserDetails.USER_NAME).values("jayanth").condition(UserDetails.USER_ID, Operators.Equals, "1");
//		u.executeUpdate();
//		Update u = new Update();handleNumbers
//		u.table(Table.UserDetails)
//		.columns(UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
//		.values("jd","jackie","chan","public")
//		.condition(UserDetails.USER_ID, Operators.Equals,"1" );
//		u.executeUpdate();
		ContactsDao dao = new ContactsDao();
//		System.out.println(dao.getExistingContacts(1));
		PreExecuteTasks p = new PreExecuteTasks();
		Insert i = new Insert();
		i.table(Table.Contacts).columns(Contacts.CREATED_AT).values("qe34241");
		try {
			p.addTimeToQueries(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		private void createContact(JsonObject contact) {
//			String resourceName = contact.get("resourceName").getAsString();
////			String etag = contact.get("etag").getAsString();
//			JsonArray names = null;
//			if (contact.get("names") != null) {
//				names = contact.get("names").getAsJsonArray();
//			}
//			JsonArray phoneNumbers = null;
//			if (contact.get("phoneNumbers") != null) {
//				phoneNumbers = contact.get("phoneNumbers").getAsJsonArray();
//			}
//			JsonArray emailAddresses = null;
//			if (contact.get("emailAddresses") != null) {
//				emailAddresses = contact.get("emailAddresses").getAsJsonArray();
//			}
//			JsonArray photos = null;
//			if (contact.get("photos") != null) {
//				photos = contact.get("photos").getAsJsonArray();
//			}
//			System.out.println(resourceName);
//			System.out.println(names);
//
//			JsonObject name = (names != null && names.size() > 0 && names.get(0).isJsonObject())
//					? names.get(0).getAsJsonObject()
//					: new JsonObject();
//
//			String displayName = name.get("displayName") != null ? name.get("displayName").getAsString() : "No name";
//
//			// Split displayName by spaces into an array
//			String[] nameParts = displayName.split(" ");
//
//			// Get the first name as the first part
//			String firstName = nameParts[0];
//
//			// Set last name as the second part if present, otherwise use firstName as
//			// lastName
//			String lastName = nameParts.length > 2 ? nameParts[2] : firstName;
//
//			JsonObject metaData = (JsonObject) contact.get("metadata");
//
//			JsonArray sources = metaData.get("sources").getAsJsonArray();
//
//			String modifiedTimeString = ((JsonObject) sources.get(0)).get("updateTime").getAsString();
//
//			OffsetDateTime modifiedTime = OffsetDateTime.parse(modifiedTimeString);
//			ContactsDao contactDao = new ContactsDao();
//			int contactId = contactDao.syncAddContact(firstName, lastName, SessionFilter.user_id.get(), resourceName,
//					modifiedTime.toInstant().toEpochMilli());
//
//			if (phoneNumbers != null && phoneNumbers.size() > 0) {
//				ContactMobileNumbersDao mobileDao = new ContactMobileNumbersDao();
//				for (JsonElement element : phoneNumbers) {
//					JsonObject number = (JsonObject) element;
//
//					mobileDao.addNumbertoContactId(contactId, number.get("value").getAsString());
//				}
//			}
//
//			if (emailAddresses != null && emailAddresses.size() > 0) {
//				ContactMailsDao mailDao = new ContactMailsDao();
//				for (JsonElement element : emailAddresses) {
//					JsonObject mail = (JsonObject) element;
//					mailDao.addMailToContact(contactId, mail.get("value").getAsString());
////					addEmail(contactId, mail.get("value").getAsString());
//				}
//			}
//
//			// photos to be processed later
//		}
	}

}
