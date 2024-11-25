package com.dao;

import com.dbObjects.*;


import com.dbconn.*;
import com.dto.*;
import com.enums.*;
import com.filters.SessionFilter;
import com.queryLayer.*;
import com.session.*;
//import com.startup.RegServer;

import java.util.*;

public class NewDao {
	
	
	// All these are moved to their respective files 
	@Deprecated
	public static boolean signUpUser(String user_name, String first_name, String last_name, String email,
			String password, String account_type) {
		Insert userDetails = new Insert().table(Table.UserDetails)
				.columns(UserDetails.USER_NAME, UserDetails.FIRST_NAME, UserDetails.LAST_NAME,
						UserDetails.CONTACT_TYPE)
				.values(user_name, BCrypt.hashpw(password, BCrypt.gensalt()), first_name, last_name, account_type);
		Integer user_id = userDetails.executeUpdate(true);
		Insert userMail = new Insert().table(Table.UserMails).columns(UserMails.MAIL, UserMails.USER_ID).values(email,
				user_id.toString());
		int success = userMail.executeUpdate();
		if (success >= 0) {
			return true;
		}
		return false;
	}
	@Deprecated
	public static UserDetailsObj loginUser(String email) {
		UserDetailsObj user = null;
		Select s = new Select();
		s.table(Table.UserDetails)
				.columns(UserDetails.USER_ID, UserDetails.USER_NAME, UserDetails.FIRST_NAME,
						UserDetails.LAST_NAME, UserDetails.CONTACT_TYPE,UserDetails.CREATED_AT, UserDetails.MODIFIED_AT)
				.join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails,
						UserDetails.USER_ID)
				.condition(UserMails.MAIL, Operators.Equals, email);
		
		List<ResultObject> users = (List<ResultObject>)s.executeQuery(UserDetailsObj.class);
		if(users.size() >0) {
			user = (UserDetailsObj) users.get(0);
		}
		
		return user;
	}
	
	public static boolean new_loginUser(String email, String password) {
		
		return false;
	}
	
	public static UserDetailsObj getUser(Integer user_id) {
		Select s = new Select();
		s.table(Table.UserDetails).columns(UserDetails.ALL_COLS).condition(UserDetails.USER_ID, Operators.Equals,
				user_id.toString());
		List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
		UserDetailsObj user = (UserDetailsObj) users.get(0);
		return user;
	}

	public static List<UserMailsObj> getUserMails(Integer user_id) {
		Select s = new Select();
		s.table(Table.UserMails).columns(UserMails.ALL_COLS).condition(UserMails.USER_ID, Operators.Equals,
				user_id.toString());
		List<ResultObject> mails = s.executeQuery(UserMailsObj.class);
		List<UserMailsObj> user_mails = new ArrayList<UserMailsObj>();
		for (ResultObject mail : mails) {
			user_mails.add((UserMailsObj) mail);
		}
		return user_mails;
	}
	
	public static List<UserGroupsObj> getUserGroups(Integer user_id){
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
	public static List<ContactsObj> getGroupContacts(Integer group_id) {
		List<ContactsObj> group_contacts = new ArrayList<ContactsObj>();
		Select get_group_contacts = new Select();
		get_group_contacts.table(Table.Contacts).columns(Contacts.CONTACT_ID,Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.ADDRESS,Contacts.CREATED_AT)
		.join(Joins.InnerJoin, Table.GroupContacts, GroupContacts.CONTACT_ID, Operators.Equals, Table.Contacts, Contacts.CONTACT_ID)
		.condition(GroupContacts.GROUP_ID, Operators.Equals, group_id.toString());
		List<ResultObject> resultset = get_group_contacts.executeQuery(ContactsObj.class);
		for(ResultObject contact : resultset) {
			group_contacts.add((ContactsObj)contact);
		}
		return group_contacts;
	}
	
	public static boolean checkifMailbelongstoUser(Integer user_id, String mail_id) {
		Select checkuserandmail = new Select();
		checkuserandmail.table(Table.UserMails).condition(UserMails.USER_ID, Operators.Equals, user_id.toString())
		.condition(UserMails.MAIL_ID, Operators.Equals, mail_id);
		List<HashMap<Columns, Object>> res = checkuserandmail.executeQuery();
		System.out.println(res);
		return res.size() >0;
	}
	
	public static int setPrimaryMail(Integer user_id, String mail_id) {
		if(checkifMailbelongstoUser(user_id, mail_id)) {
			Update u = new Update();
			u.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("0").condition(UserMails.USER_ID,
					Operators.Equals, user_id.toString());

			int success = u.executeUpdate();
			if (success >= 0) {
				Update setprimary = new Update();
				setprimary.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("1")
						.condition(UserMails.USER_ID, Operators.Equals, user_id.toString())
						.condition(UserMails.MAIL_ID, Operators.Equals, mail_id);

				 return  setprimary.executeUpdate();
			}
		}
		return -1;
	}

	public static boolean checkIfMailExists(String email) {
		Select checkMail = new Select();
		checkMail.table(Table.UserMails).columns(UserMails.MAIL).condition(UserMails.MAIL, Operators.Equals, email);

		List<HashMap<Columns, Object>> resultset = checkMail.executeQuery();
		if (resultset.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void logoutUser(User u) {
		SessionDataManager.users_data.remove(u.getUser_id());
		u = null;
	}

	// can return boolean , show can show reason to the user
	public static void addEmail(Integer user_id, String mail) {
		if (!checkIfMailExists(mail)) {
			Insert addmail = new Insert();
			addmail.table(Table.UserMails).columns(UserMails.USER_ID, UserMails.MAIL).values(user_id.toString(), mail);
			int addedmail = addmail.executeUpdate();
		}
	}

	public static void addContact(Integer user_id, String first_name, String last_name, String address,
			ArrayList<Long> mobile_numbers, ArrayList<String> mails) {
		Insert addcontact = new Insert();
		Long currenttimeinmillis = System.currentTimeMillis();
		addcontact.table(Table.Contacts)
				.columns(Contacts.USER_ID, Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.ADDRESS,
						Contacts.CREATED_AT)
				.values(user_id.toString(), first_name, last_name, address, currenttimeinmillis.toString());
		Integer contact_id = addcontact.executeUpdate(true);
		for (Long mobile : mobile_numbers) {
			Insert addmobilenumber = new Insert();
			addmobilenumber.table(Table.ContactMobileNumbers)
					.columns(ContactMobileNumbers.CONTACT_ID, ContactMobileNumbers.NUMBER)
					.values(contact_id.toString(), mobile.toString());
			int addednumber = addmobilenumber.executeUpdate();
			if (addednumber > 0) {
				continue;
			} else {
				break;
			}
		}
		if (contact_id > 0) {
			for (String mail : mails) {
				Insert addmail = new Insert();
				addmail.table(Table.ContactMails).columns(ContactMails.CONTACT_ID, ContactMails.MAIL)
						.values(contact_id.toString(), mail);

				int addedmail = addmail.executeUpdate();
				if (addedmail > 0) {
					continue;
				} else {
					break;
				}
			}
		}
	}
	
	public static boolean checkIfContactInGroups(Integer contact_id) {
		Select contactIngroup = new Select();
		contactIngroup.table(Table.GroupContacts)
		.columns(GroupContacts.CONTACT_ID)
		.condition(GroupContacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		
		List<HashMap<Columns, Object>> resultset = contactIngroup.executeQuery();
		
		return resultset.size()>0;
	}
	
	public static void deleteContact(Integer contact_id) {
//		if (checkIfContactInGroups(contact_id)) {
//			Delete deleteContactInGroup = new Delete();
//			deleteContactInGroup.table(Table.GroupContacts).condition(GroupContacts.CONTACT_ID, Operators.Equals, contact_id.toString());
//			int deletedcontactfromGroups = deleteContactInGroup.executeUpdate();
//		}
		Delete deleteContact = new Delete();
		deleteContact.table(Table.Contacts).condition(Contacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		int deletedContact = deleteContact.executeUpdate();
//		
//		Delete deletecontactmobiles = new Delete();
//		deletecontactmobiles.table(Table.ContactMobileNumbers).condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contact_id.toString());
//		int deltedcontactmobiles = deletecontactmobiles.executeUpdate();
//		
//		Delete deletecontactmails = new Delete();
//		deletecontactmails.table(Table.ContactMails).condition(ContactMails.CONTACT_ID, Operators.Equals, contact_id.toString());
//		int deletedcontactmails = deletecontactmails.executeUpdate();
		
	}
	
	public static List<ContactsObj> getUserContacts(Integer user_id) {
		List<ContactsObj> user_contacts = new ArrayList<ContactsObj>();
		Select s = new Select();
		s.table(Table.Contacts).columns(Contacts.CONTACT_ID,Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.ADDRESS, Contacts.CREATED_AT,Contacts.USER_ID)
		.condition(Contacts.USER_ID, Operators.Equals,user_id.toString());

		List<ResultObject> contacts = s.executeQuery(ContactsObj.class);

		for (ResultObject res : contacts) {
			ContactsObj c = (ContactsObj) res;
			user_contacts.add(c);
		}
		return user_contacts;
	}
	
	
	public static List<ContactMobileNumbersObj> getContactMobileNumbers(Integer contact_id){
		List<ContactMobileNumbersObj> contact_numbers = new ArrayList<ContactMobileNumbersObj>();
		Select getMobileNumberswithContactId = new Select();
		getMobileNumberswithContactId.table(Table.ContactMobileNumbers).columns(ContactMobileNumbers.CONTACT_ID,ContactMobileNumbers.NUMBER)
		.condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contact_id.toString());
		List<HashMap<Columns, Object>> mobileNumbers = getMobileNumberswithContactId.executeQuery();
		for(HashMap<Columns, Object> number :mobileNumbers) {
			ContactMobileNumbersObj n = new ContactMobileNumbersObj();
			n.setContactId((Integer)number.get(ContactMobileNumbers.CONTACT_ID));
			n.setMobileNumber((Long)number.get(ContactMobileNumbers.CONTACT_ID));
			contact_numbers.add(n);
		}
		return contact_numbers;
	}
	
	public static List<ContactMailsObj> getContactMails(Integer contact_id){
		List<ContactMailsObj> contact_mails = new ArrayList<>();
		Select get_contactMails  = new Select();
		get_contactMails.table(Table.ContactMails).columns(ContactMails.CONTACT_ID,ContactMails.MAIL)
		.condition(ContactMails.CONTACT_ID, Operators.Equals, contact_id.toString());
		List<HashMap<Columns, Object>> mails = get_contactMails.executeQuery();
		for(HashMap<Columns,Object> mailObj : mails) {
			ContactMailsObj o = new ContactMailsObj();
			o.setContactId((Integer)mailObj.get(ContactMails.CONTACT_ID));
			o.setEmail((String) mailObj.get(ContactMails.MAIL));
			contact_mails.add(o);
		}
		
		return contact_mails;
	}
	
	public static List<String> getContactGroups(Integer contact_id){
		List<String> group_names = new ArrayList<String>();
		Select getGroup_ids = new Select();
		getGroup_ids.table(Table.GroupContacts).columns(GroupContacts.GROUP_ID)
		.condition(GroupContacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		
		List<HashMap<Columns, Object>> resultset = getGroup_ids.executeQuery();
		if(resultset.size() >0) {
			for(HashMap<Columns, Object> row: resultset){
				Integer group_id =(Integer) row.get(GroupContacts.GROUP_ID);
				Select g_name = new Select();
				g_name.table(Table.UserGroups).columns(UserGroups.GROUP_NAME)
				.condition(UserGroups.GROUP_ID, Operators.Equals, group_id.toString());
				List<HashMap<Columns, Object>> name = g_name.executeQuery();
				group_names.add((String)name.get(0).get(UserGroups.GROUP_NAME));
			}
		}
		return group_names;
	}
	
	
	public static boolean checkIfGroupExistsForUser(Integer user_id, String group_name) {
		Select checkgroupforuser = new Select();
		checkgroupforuser.table(Table.UserGroups).columns(UserGroups.GROUP_NAME)
		.condition(UserGroups.USER_ID, Operators.Equals, user_id.toString())
		.condition(UserGroups.GROUP_NAME, Operators.Equals, group_name);
		List<HashMap<Columns, Object>> groupsofuser = checkgroupforuser.executeQuery();
		System.out.println(checkgroupforuser.build());
		System.out.println(groupsofuser);
		return groupsofuser.size()>0;
	}
	//this can be boolean to show user info about this operation
	public static void addGroupForUser(Integer user_id, String group_name) {
		if (!checkIfGroupExistsForUser(user_id, group_name)) {
			Insert addgroup = new Insert();
			addgroup.table(Table.UserGroups).columns(UserGroups.USER_ID,UserGroups.GROUP_NAME)
			.values(user_id.toString(), group_name);
			int added = addgroup.executeUpdate();
		}
	}
	
	public static void deleteGroupForUser(Integer user_id,Integer group_id) {
		
			Delete deleteGroup = new Delete();
			deleteGroup.table(Table.UserGroups)
			.condition(UserGroups.USER_ID, Operators.Equals, user_id.toString())
			.condition(UserGroups.GROUP_ID, Operators.Equals, group_id.toString());
			deleteGroup.build();
			int status = deleteGroup.executeUpdate();
		
		
	}
	
	public static boolean checkIfContactInGroup(Integer group_id, Integer contact_id) {
		Select group_contact = new Select();
		group_contact.table(Table.GroupContacts).columns(GroupContacts.GROUP_ID,GroupContacts.CONTACT_ID)
		.condition(GroupContacts.GROUP_ID, Operators.Equals, group_id.toString())
		.condition(GroupContacts.CONTACT_ID, Operators.Equals, contact_id.toString());
		List<ResultObject> resultset = group_contact.executeQuery(GroupContactsObj.class);
		return resultset.size() > 0;
	}
	//can return boolean or String indcating the status of operation
	public static void addContacttoGroup(Integer group_id, Integer contact_id) {
		if(!checkIfContactInGroup(group_id,contact_id)) {
			Insert insertContactIntoGroup = new Insert();
			insertContactIntoGroup.table(Table.GroupContacts).columns(GroupContacts.GROUP_ID,GroupContacts.CONTACT_ID)
			.values(group_id.toString(),contact_id.toString());
			
			int added = insertContactIntoGroup.executeUpdate();
		}
	}
	
	public static int deletContactfromGroup(Integer group_id, Integer contact_id) {
		if(checkIfContactInGroup(group_id, contact_id)) {
			Delete  deletecontactfromgroup = new Delete();
			deletecontactfromgroup.table(Table.GroupContacts)
			.condition(GroupContacts.GROUP_ID, Operators.Equals, group_id.toString())
			.condition(GroupContacts.CONTACT_ID, Operators.Equals, contact_id.toString());
			
			return deletecontactfromgroup.executeUpdate();
		}else {
			return -1;
		}
	}
	
	public static SessionData fetchSessionFromDb(String session_id) {
		SessionData session = new SessionData();
		Select sessionDatafromdb = new Select();
		sessionDatafromdb.table(Table.Sessions).columns(Sessions.USER_ID,Sessions.CREATED_TIME,Sessions.LAST_ACCESSED_TIME)
		.condition(Sessions.SESSION_ID, Operators.Equals, session_id);
		List<HashMap<Columns,Object>> session_objects = sessionDatafromdb.executeQuery();
		if(session_objects.size()==0) return null;
		HashMap<Columns,Object> session_object = session_objects.get(0);
		session.setCreated_time((Long) session_object.get(Sessions.CREATED_TIME));
		session.setUser_id((Integer) session_object.get(Sessions.USER_ID));
		session.setLast_accessed_at((Long) session_object.get(Sessions.LAST_ACCESSED_TIME));
		return session;
	}
	
	public static void fetchSessionsFromDb() {
		Map<String, SessionData> sess_map = SessionDataManager.session_data;
		Select sessionsfromdb = new Select();
		sessionsfromdb.table(Table.Sessions).columns(Sessions.SESSION_ID,Sessions.USER_ID,Sessions.CREATED_TIME,Sessions.LAST_ACCESSED_TIME);
		
		List<ResultObject> resultset  = sessionsfromdb.executeQuery(SessionsObj.class);
		
		if(resultset.size() > 0) {
			for(ResultObject row : resultset) {
				SessionsObj session = (SessionsObj) row;
				if(System.currentTimeMillis() <= session.getLastAccessedTime()+ 1000*60*30) {
					sess_map.put(session.getSessionId(), new SessionData(session.getUserId(),session.getCreatedTime(),session.getLastAccessedTime(),session.getLastAccessedTime()+30*60*1000));
				}
			}
		}
		System.out.println("retrived sessions from db at startup");
		System.out.println(sess_map);
	}

	public static int removeSessionfromDb(String session_id) {
		Delete deleteSess = new Delete();
		deleteSess.table(Table.Sessions).condition(Sessions.SESSION_ID, Operators.Equals, session_id);
		int x =  deleteSess.executeUpdate();
		return x;
	}

	public static int insertUserInSessions(String session_id, Integer user_id, Long created_at,Long last_accessed_at) {
		Insert insertSess = new Insert();
		insertSess.table(Table.Sessions).columns(Sessions.SESSION_ID,Sessions.USER_ID,Sessions.CREATED_TIME,Sessions.LAST_ACCESSED_TIME).values(session_id,user_id.toString(),created_at.toString(),last_accessed_at.toString());
		return insertSess.executeUpdate();
	}
	
	public static void updateSessionsToDatabase(Map<String,SessionData> sessions) {
		
		for(Map.Entry<String, SessionData> session : sessions.entrySet()) {
			String session_id = session.getKey();
			Update update_session = new Update();
			update_session.table(Table.Sessions).columns(Sessions.LAST_ACCESSED_TIME).values(session.getValue().getLast_accessed_time().toString())
			.condition(Sessions.SESSION_ID, Operators.Equals, session_id);
			update_session.executeUpdate();
		}
		System.out.println("Completed updating sessions to database");
	}

	public static void registerServer(String servername, String port) {
		Insert insertServerInDatabase = new Insert();
		insertServerInDatabase.table(Table.Servers).columns(Servers.SERVER_NAME,Servers.PORT).values(servername,port);
		insertServerInDatabase.executeUpdate();
	}
	
	public static void deregisterServer(String servername, String port) {
		Delete removeServerInDatabase =  new Delete();
		removeServerInDatabase.table(Table.Servers).condition(Servers.SERVER_NAME, Operators.Equals, servername)
		.condition(Servers.PORT, Operators.Equals, port);
		System.out.println(removeServerInDatabase.build());
		removeServerInDatabase.executeUpdate();
	}
	
	
	@Deprecated
	public static List<HashMap<Columns,Object>> getRegisteredServers() {
		Select getservers = new Select();
		getservers.table(Table.Servers).columns(Servers.SERVER_NAME,Servers.PORT)
		.condition(Servers.PORT, Operators.NotEquals, "8080");
		System.out.println(getservers.build());
		List<HashMap<Columns, Object>> resultset = getservers.executeQuery();
		return resultset;
	}

	public static void updateUserDetails(String userName, String firstName, String lastName, String contactType) {
		Update updateUser = new Update();
		updateUser.table(Table.UserDetails).columns(UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE)
		.values(userName,firstName,lastName,contactType)
		.condition(UserDetails.USER_ID, Operators.Equals, SessionFilter.user_id.get().toString());
		int status = updateUser.executeUpdate();
		
	}

	public static PasswordsObj getPasswordWithUserId(Integer user_id) {
		Select passwordObj = new Select();
		passwordObj.table(Table.Passwords)
		.condition(Passwords.USER_ID, Operators.Equals, user_id.toString());
		List<ResultObject> passwords = passwordObj.executeQuery(PasswordsObj.class);
		PasswordsObj password = (PasswordsObj) passwords.get(0);
		return password;
	}


}
