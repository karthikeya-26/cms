// Source code is decompiled from a .class file using FernFlower decompiler.
package com.dao;

import com.dbconn.BCrypt;



import com.dbconn.Database;
import com.loggers.AppLogger;
import com.models.Contact;
import com.models.SessionData;
import com.models.User;
import com.queryLayer.Insert;
import com.queryLayer.Select;
import com.queryLayer.Update;
import com.session.SessionDataManager;
import com.tables.ContactMails;
import com.tables.ContactMobileNumbers;
import com.tables.Contacts;
import com.tables.GroupContacts;
import com.tables.Joins;
import com.tables.Operators;
import com.tables.Table;
import com.tables.UserDetails;
import com.tables.UserGroups;
import com.tables.UserMails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Dao {
   public Dao() {
   }

   public static boolean signUpUser(String user_name, String first_name, String last_name, String email, String password, String account_type) {
	    Connection c = null;
	    PreparedStatement ps1 = null;
	    PreparedStatement ps2 = null;
	    ResultSet key_generated = null;
	    
	    try {
	        c = Database.getConnection();
	        
	        c.setAutoCommit(false);
	        
	        // Insert user details
	        String statement1 = "INSERT INTO user_details (user_name, password, first_name, last_name, contact_type) VALUES (?, ?, ?, ?, ?)";
	        ps1 = c.prepareStatement(statement1, PreparedStatement.RETURN_GENERATED_KEYS);
	        ps1.setString(1, user_name);
	        ps1.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
	        ps1.setString(3, first_name);
	        ps1.setString(4, last_name);
	        ps1.setString(5, account_type);
	        
	        int inserted = ps1.executeUpdate();
	        if (inserted <= 0) {
	            return false;
	        }
	        
	        // Retrieve the generated user_id
	        key_generated = ps1.getGeneratedKeys();
	        if (!key_generated.next()) {
	            return false;
	        }
	        int user_id = key_generated.getInt(1);
	        
	        // Insert user email
	        String statement2 = "INSERT INTO user_mails (mail, user_id) VALUES (?, ?)";
	        ps2 = c.prepareStatement(statement2);
	        ps2.setString(1, email);
	        ps2.setInt(2, user_id);
	        
	        int inserted2 = ps2.executeUpdate();
	        if (inserted2 <= 0) {
	            return false;
	        }
	        
	        // Commit transaction
	        c.commit();
	        return true;
	        
	    } catch (SQLException e) {
	        if (c != null) {
	            try {
	                c.rollback();
	            } catch (SQLException rollbackEx) {
	                System.out.println("Rollback failed: " + rollbackEx.getMessage());
	            }
	        }
	        System.out.println("Error during sign-up: " + e.getMessage());
	        return false;
	        
	    } finally {
	    	
	        try {
	            if (key_generated != null) key_generated.close();
	            if (ps1 != null) ps1.close();
	            if (ps2 != null) ps2.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            System.out.println("Error closing resources: " + e.getMessage());
	        }
	    }
	}


   public static User loginUser(String username, String password){
	   
	    User u = new User();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Database.getConnection();
//	        throw new Exception("chumma ");
	        
	        // SQL query to get user details
	        Select s = new Select();
	        s.table(Table.UserDetails).columns(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserDetails, UserDetails.USER_ID, Operators.Equals, Table.UserMails, UserMails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "?");
	        
//	        String statement = "SELECT * FROM user_details JOIN user_mails ON user_details.user_id = user_mails.user_id WHERE mail = ?";
	        String statement = s.build();
	        ps = c.prepareStatement(statement);
	        ps.setString(1, username);
	        
	        rs = ps.executeQuery();
	        
	        // If no user found, return empty User object
	        if (!rs.next()) {
	            return null;
	        }
	        
	        // Verify password using BCrypt
	        boolean res = BCrypt.checkpw(password, rs.getString("password"));
	        if (res) {
	            // Populate user details if password matches
	            u.setAccount_type(rs.getString("contact_type"));
	            u.setFirst_name(rs.getString("first_name"));
	            u.setLast_name(rs.getString("last_name"));
	            u.setUser_name(rs.getString("user_name"));
	            u.setUser_id(rs.getInt("user_id"));
	            return u;
	        }
	        
	        return u;  // Return empty user if password check fails
	        
	    } catch (SQLException e) {
	        System.out.println("Error in loginUser method:");
	        e.printStackTrace();
	        return u;
	        
	    } catch(Exception e){
	    	e.printStackTrace();
	    	AppLogger.ApplicationLog(e);
	    	
	    }
//	    	finally {
//	    
//	        // Close resources
//	        try {
//	            if (rs != null) rs.close();
//	            if (ps != null) ps.close();
//	            if (c != null) c.close();
//	        } catch (SQLException e) {
//	            System.out.println("Error closing resources: " + e.getMessage());
//	        }
//	    }
		return u;
	}
   public static void main(String[] args) {
	   System.out.println("hi");
	loginUser("a", "x");
	
}
   public static User getUser(int user_id) {
	   User user = new User();
	   Select s = new Select().table(Table.UserDetails).column(UserDetails.ALL_COLS).condition(UserDetails.USER_ID, Operators.Equals, "?");
//	   String statement = "select * from user_details where user_id = ?";
	   String statement = s.build();
	   try(Connection c = Database.getConnection()){
		   
		   PreparedStatement ps = c.prepareStatement(statement);
		   ps.setInt(1, user_id);
		   ResultSet rs = ps.executeQuery();
		   rs.next();
		   user.setUser_id(user_id);
		   user.setFirst_name(rs.getString("first_name"));
		   user.setLast_name(rs.getString("last_name"));
		   user.setAccount_type(rs.getString("contact_type"));
           user.setUser_name(rs.getString("user_name"));
           user.setGroup_contacts(getGroupUserContacts(user));
           user.setUser_contacts(getContacts(user));
           user.setUserGroups(getUserGroups(user));
           user.setmails(getEmails(user));
   
	   }catch(SQLException e) {
		   e.printStackTrace();
	   }
		return user;
	}


   public static ArrayList<String> getEmails(User u) {
	    ArrayList<String> userMails = new ArrayList<>();
	    Select s = new Select();
	    s.table(Table.UserMails).column(UserMails.ALL_COLS).condition(UserMails.USER_ID, Operators.Equals, "?");
//	    String statement = "SELECT * FROM user_mails WHERE user_id = ?";
	    String statement = s.build();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Database.getConnection();
	        ps = c.prepareStatement(statement);
	        ps.setInt(1, u.getUser_id());
	        
	        rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            String email = rs.getString("mail");
	            int is_primary = rs.getInt("is_primary");
	            if (is_primary ==1) {
	            	u.setPrimaryEmail(email);
	            }else {
	            	userMails.add(email);
	            }
	        }
	        
	    } catch (SQLException e) {
	        System.out.println("Error in Dao getUserEmails method:");
	        e.printStackTrace();
	        
	    } finally {
	        // Close resources
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            System.out.println("Error closing resources: " + e.getMessage());
	        }
	    }
	    
	    return userMails;
   }
   
   public static void setPrimaryEmail(String selected_mail, User user) {
		// TODO Auto-generated method stub
	   Update u = new Update();
	   u.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("0").condition(UserMails.USER_ID, Operators.Equals, "?");
	   String statement = "update user_mails set is_primary = 0 where user_id = ?";
	   String statementx = u.build();
	   Update u1 = new Update();
	   u.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("?").condition(UserMails.USER_ID, Operators.Equals, "?").condition(UserMails.MAIL, Operators.Equals, "?");
	   String statement2  = "update user_mails set is_primary = 1 where user_id = ? and mail= ?;";
	   String statementy = u1.build();
	   try(Connection c = Database.getConnection()){
		   
		   c.setAutoCommit(false);
		   
		   PreparedStatement ps = c.prepareStatement(statement);
		   
		   ps.setInt(1, user.getUser_id());
		   System.out.println(ps);
		   int success = ps.executeUpdate();
		   if (success >=0) {
			   PreparedStatement ps1 = c.prepareStatement(statement2);
			   ps1.setInt(1, user.getUser_id());
			   ps1.setString(2, selected_mail);
			   System.out.println(ps1);
			   
			   int succes = ps1.executeUpdate();
			   System.out.println(success);
			   if(succes >=0) {
				   c.commit();
			   }else {
				   c.rollback();
				   return ;
			   }
		   }else {
			   c.rollback();
			   return;
		   }
		   
	   }catch(SQLException e) {
		   e.printStackTrace();
		 
	   }
		
	}


   public static boolean checkifMailExists(User user, String email) {
//	    String statement = "SELECT mail FROM user_mails WHERE user_id = ?";
	   Select s = new Select().table(Table.UserMails).column(UserMails.MAIL).condition(UserMails.USER_ID, Operators.Equals, "?");
	    String statement = s.build();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Database.getConnection();
	        ps = c.prepareStatement(statement);
	        ps.setInt(1, user.getUser_id());
	        rs = ps.executeQuery();

	        // Check if the email exists for the given user_id
	        while (rs.next()) {
	            if (rs.getString("mail").equals(email)) {
	                return true;
	            }
	        }

	        return false;

	    } catch (SQLException e) {
	        System.out.println("Inside Dao: check if mail exists");
	        e.printStackTrace();
	        return false;

	    } finally {
	        // Close resources
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            System.out.println("Error closing resources: " + e.getMessage());
	        }
	    }
	}


   public static void logoutUser(User u) {
      u = null;
   }
   public static void addEmail(User u, String email) {
	    String statement = "INSERT INTO user_mails (mail, user_id) VALUES (?, ?)";
	    Insert i = new Insert();
	    i.table(Table.UserMails).columns(UserMails.MAIL, UserMails.USER_ID).values("?","?");
	    String statementx = i.build();
	    Connection c = null;
	    PreparedStatement ps = null;

	    try {
	        c = Database.getConnection();
	        ps = c.prepareStatement(statement);
	        ps.setString(1, email);
	        ps.setInt(2, u.getUser_id());
	        ps.executeUpdate();
	        
	        System.out.println("Email added successfully");
	        
	    } catch (SQLException e) {
	        System.out.println("Inside Dao: addEmail method");
	        e.printStackTrace();
	        
	    } finally {
	        // Close resources
	        try {
	            if (ps != null) ps.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            System.out.println("Error closing resources: " + e.getMessage());
	        }
	    }
	}
   public static void addContact(int user_id, String first_name, String last_name, String address, ArrayList<String> mobile_no, ArrayList<String> mail_ids) {
	    String contactStatement = "INSERT INTO contacts (first_name, last_name, user_id, address) VALUES (?, ?, ?, ?)";
	    String mobileStatement = "INSERT INTO contact_mobile_numbers (contact_id, number) VALUES (?, ?)";
	    String emailStatement = "INSERT INTO contact_mails (contact_id, mail) VALUES (?,?)";
	    
	    Connection c = null;
	    PreparedStatement contactPs = null;
	    PreparedStatement mobilePs = null;
	    PreparedStatement emailPs = null;
	    ResultSet rs = null;
	    int contact_id = -1;
	    
	    try {
	        c = Database.getConnection();
	        c.setAutoCommit(false);  // Begin transaction

	        // Insert into contacts
	        contactPs = c.prepareStatement(contactStatement, PreparedStatement.RETURN_GENERATED_KEYS);
	        contactPs.setString(1, first_name);
	        contactPs.setString(2, last_name);
	        contactPs.setInt(3, user_id);
	        contactPs.setString(4, address);
	        
	        int success = contactPs.executeUpdate();
	        if (success > 0) {
	            // Retrieve the generated contact_id
	            rs = contactPs.getGeneratedKeys();
	            if (rs.next()) {
	                contact_id = rs.getInt(1);
	            }
	        }

	        // Insert into contact_mobile_numbers if mobiles are provided
	        if (contact_id > 0 && mobile_no.size() > 0) {
	            mobilePs = c.prepareStatement(mobileStatement);
	            for (String mobile : mobile_no) {
	                mobilePs.setInt(1, contact_id);
	                mobilePs.setString(2, mobile);
	                mobilePs.addBatch();
	            }
	            mobilePs.executeBatch();  // Execute batch for mobile numbers
	        }

	        // Insert into contact_mails if emails are provided
	        if (contact_id > 0 && mail_ids.size() > 0) {
	            emailPs = c.prepareStatement(emailStatement);
	            for (String mail : mail_ids) {
	                emailPs.setInt(1, contact_id);
	                emailPs.setString(2, mail);
	                emailPs.addBatch();
	            }
	            emailPs.executeBatch();  // Execute batch for emails
	        }

	        c.commit();  // Commit transaction
	        System.out.println("Contact added successfully");

	    } catch (SQLException e) {
	        // Rollback in case of an error
	        if (c != null) {
	            try {
	                c.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        e.printStackTrace();

	    } finally {
	        // Close all resources
	        try {
	            if (rs != null) rs.close();
	            if (contactPs != null) contactPs.close();
	            if (mobilePs != null) mobilePs.close();
	            if (emailPs != null) emailPs.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
   public static void deleteContact(int contact_id) throws SQLException {
	    String deleteMobileNumbers = "DELETE FROM contact_mobile_numbers WHERE contact_id = ?";
	    String deleteEmails = "DELETE FROM contact_mails WHERE contact_id = ?";
	    String deleteContact = "DELETE FROM contacts WHERE contact_id = ?";

	    Connection c = null;
	    PreparedStatement ps = null;

	    try {
	        c = Database.getConnection();
	        c.setAutoCommit(false);  // Start transaction

	        // Delete from contact_mobile_numbers
	        ps = c.prepareStatement(deleteMobileNumbers);
	        ps.setInt(1, contact_id);
	        ps.executeUpdate();
	        ps.close();
	        System.out.println("Mobile numbers deleted");

	        // Delete from contact_mails
	        ps = c.prepareStatement(deleteEmails);
	        ps.setInt(1, contact_id);
	        ps.executeUpdate();
	        ps.close();
	        System.out.println("Emails deleted");

	        // Delete from contacts
	        ps = c.prepareStatement(deleteContact);
	        ps.setInt(1, contact_id);
	        int success = ps.executeUpdate();
	        ps.close();

	        if (success > 0) {
	            System.out.println("Contact deleted successfully");
	        }

	        c.commit();  // Commit transaction

	    } catch (SQLException e) {
	        if (c != null) {
	            c.rollback();  // Rollback transaction in case of error
	        }
	        e.printStackTrace();

	    } finally {
	        if (ps != null) ps.close();
	        if (c != null) c.close();
	    }
	}

   public static ArrayList<Contact> getContacts(User user) throws SQLException {
	    ArrayList<Contact> userContacts = new ArrayList<>();
	    String statement = "SELECT c.contact_id, c.first_name, c.last_name, c.user_id, c.address, " +
	                       "GROUP_CONCAT(DISTINCT m.number) AS mobile_numbers, " +
	                       "GROUP_CONCAT(DISTINCT cm.mail) AS emails " +
	                       "FROM contacts c " +
	                       "LEFT JOIN contact_mobile_numbers m ON c.contact_id = m.contact_id " +
	                       "LEFT JOIN contact_mails cm ON c.contact_id = cm.contact_id " +
	                       "WHERE c.user_id = ? " +
	                       "GROUP BY c.contact_id " +
	                       "ORDER BY c.first_name;";
	    
	    try (Connection c = Database.getConnection(); 
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setInt(1, user.getUser_id());
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Contact contact = new Contact();
	            contact.setContact_id(rs.getInt("contact_id"));
	            contact.setFirst_name(rs.getString("first_name"));
	            contact.setLast_name(rs.getString("last_name"));
	            contact.setAddress(rs.getString("address"));

	            String mobileNumbers = rs.getString("mobile_numbers");
	            contact.setMobile_numbers(mobileNumbers != null ? mobileNumbers.split(",") : new String[0]);

	            String emails = rs.getString("emails");
	            contact.setMail_ids(emails != null ? emails.split(",") : new String[0]);

	            userContacts.add(contact);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;  // Re-throw the exception to handle it upstream if necessary
	    }

	    return userContacts;
	}


   public static void editContact(Contact contact) throws SQLException {
      //yet to be implemented
   }

   public static ArrayList<Contact> getGroupUserContacts(User u) {
	    String statement = "SELECT c.contact_id, c.first_name, c.last_name, c.address, c.user_id, " +
	                       "GROUP_CONCAT(DISTINCT cm.number SEPARATOR ',') AS mobile_numbers, " +
	                       "GROUP_CONCAT(DISTINCT ma.mail SEPARATOR ',') AS emails, " +
	                       "GROUP_CONCAT(DISTINCT ug.group_name SEPARATOR ',') AS group_names " +
	                       "FROM contacts c " +
	                       "LEFT JOIN contact_mobile_numbers cm ON c.contact_id = cm.contact_id " +
	                       "LEFT JOIN contact_mails ma ON c.contact_id = ma.contact_id " +
	                       "JOIN group_contacts gc ON c.contact_id = gc.contact_id " +
	                       "JOIN user_groups ug ON gc.group_id = ug.group_id " +
	                       "WHERE c.user_id = ? " +
	                       "GROUP BY c.contact_id " +
	                       "ORDER BY c.first_name;";
//	    Select s = new Select();
//	    s.table(Table.Contacts)
//	    .columns(Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.ADDRESS, Contacts.USER_ID)
//	    .groupConcatAs(ContactMobileNumbers.NUMBER, "mobile_numbers", ",")
//	    .groupConcatAs(ContactMails.MAIL, "mails", ",")
//	    .groupConcatAs(UserGroups.GROUP_NAME, "user_groups", ",")
//	    .join(Joins.LeftJoin, Table.Contacts, Contacts.CONTACT_ID, Operators.Equals, Table.ContactMobileNumbers, ContactMobileNumbers.CONTACT_ID)
//	    .join(Joins.LeftJoin,Table.ContactMails, ContactMails.CONTACT_ID,Operators.Equals, Table.Contacts, Contacts.CONTACT_ID)
//	    .join(Joins.InnerJoin, Table.GroupContacts, GroupContacts.CONTACT_ID, Operators.Equals, Table.Contacts, Contacts.CONTACT_ID)
//	    .join(Joins.InnerJoin, Table.UserGroups, UserGroups.USER_ID, Operators.Equals, Table.GroupContacts,GroupContacts.GROUP_ID)
//	    .condition(Contacts.USER_ID, Operators.Equals, "?");

	    ArrayList<Contact> groupContacts = new ArrayList<>();

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setInt(1, u.getUser_id());
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Contact contact = new Contact();
	            contact.setContact_id(rs.getInt("contact_id"));
	            contact.setFirst_name(rs.getString("first_name"));
	            contact.setLast_name(rs.getString("last_name"));
	            contact.setAddress(rs.getString("address"));
	            contact.setMobile_numbers(rs.getString("mobile_numbers") != null ? 
	                                      rs.getString("mobile_numbers").split(",") : new String[0]);
	            contact.setMail_ids(rs.getString("emails") != null ? 
	                                rs.getString("emails").split(",") : new String[0]);
	            contact.setGroups(rs.getString("group_names") != null ? 
	                              rs.getString("group_names").split(",") : new String[0]);
	            groupContacts.add(contact);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return groupContacts;
	}
   
   
   public static boolean checkifGroupExists(User user, String groupName) {
	    String statement = "SELECT group_name FROM user_groups WHERE user_id = ?";

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setInt(1, user.getUser_id());
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            if (rs.getString("group_name").equals(groupName)) {
	                return true; // Group exists
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Inside DAO check if group exists method");
	        e.printStackTrace();
	    }

	    return false; // Group does not exist
	}

   public static void addGroup(User user, String groupName) {
	    String statement = "INSERT INTO user_groups (group_name, user_id) VALUES (?, ?)";

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setString(1, groupName);
	        ps.setInt(2, user.getUser_id());
	        int success = ps.executeUpdate();

	        if (success > 0) {
	            System.out.println("Group added successfully.");
	        } else {
	            System.out.println("Failed to add group.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Inside DAO addGroup method");
	        e.printStackTrace();
	    }
	}
   
   public static ArrayList<String> getUserGroups(User user) {
	    ArrayList<String> groups = new ArrayList<>();
	    String statement = "SELECT group_name FROM user_groups WHERE user_id = ?";

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setInt(1, user.getUser_id());
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            groups.add(rs.getString("group_name"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return groups;
	}
   public static boolean checkIfContactInGroup(User user, String groupName, int contactId) {
	    String statement = "SELECT gc.contact_id FROM user_groups ug " +
	                       "JOIN group_contacts gc ON ug.group_id = gc.group_id " +
	                       "WHERE ug.user_id = ? AND ug.group_name = ? AND gc.contact_id = ?";

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(statement)) {

	        ps.setInt(1, user.getUser_id());
	        ps.setString(2, groupName);
	        ps.setInt(3, contactId);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next(); // Returns true if a row exists
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in checkIfContactInGroup method");
	        e.printStackTrace();
	        return false;
	    }
	}
   public static void addContactToGroup(User user, String selectedGroup, int selectedContactId) {
	    String getGroupIdStatement = "SELECT group_id FROM user_groups WHERE group_name = ? AND user_id = ?";
	    int groupId = -1;

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(getGroupIdStatement)) {

	        ps.setString(1, selectedGroup);
	        ps.setInt(2, user.getUser_id());

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                groupId = rs.getInt("group_id");
	            }
	        }

	        if (groupId == -1) {
	            System.out.println("Group not found.");
	            return; // Handle case where group does not exist
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in fetching group ID");
	        e.printStackTrace();
	        return;
	    }

	    String insertContactStatement = "INSERT INTO group_contacts (group_id, contact_id) VALUES (?, ?)";

	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(insertContactStatement)) {

	        ps.setInt(1, groupId);
	        ps.setInt(2, selectedContactId);
	        int success = ps.executeUpdate();

	        if (success > 0) {
	            System.out.println("Contact added to group successfully.");
	        } else {
	            System.out.println("Failed to add contact to group.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in adding contact to group");
	        e.printStackTrace();
	    }
	}

   public static void deleteContactFromGroup(User user, String selectedGroup, int selectedContactId) {
	    String getGroupIdStatement = "SELECT group_id FROM user_groups WHERE group_name = ? AND user_id = ?";
	    int groupId = -1;

	    // Fetch group ID
	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(getGroupIdStatement)) {
	        
	        ps.setString(1, selectedGroup);
	        ps.setInt(2, user.getUser_id());
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                groupId = rs.getInt("group_id");
	            } else {
	                System.out.println("Group not found.");
	                return; // Handle case where group does not exist
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in fetching group ID");
	        e.printStackTrace();
	        return; // Exit on error
	    }

	    // Delete contact from group
	    String deleteContactStatement = "DELETE FROM group_contacts WHERE group_id = ? AND contact_id = ?";
	    
	    try (Connection c = Database.getConnection();
	         PreparedStatement ps = c.prepareStatement(deleteContactStatement)) {
	        
	        ps.setInt(1, groupId);
	        ps.setInt(2, selectedContactId);
	        int rowsAffected = ps.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("Contact deleted from group successfully.");
	        } else {
	            System.out.println("No contact found to delete in the specified group.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in deleting contact from group");
	        e.printStackTrace();
	    }
	}

   public static void deleteEmail(User user, String mailId) {
	    String statement = "DELETE FROM user_mails WHERE mail = ? AND user_id = ?";

	    try (Connection connection = Database.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
	        
	        preparedStatement.setString(1, mailId);
	        preparedStatement.setInt(2, user.getUser_id());
	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            System.out.println("Email deleted successfully.");
	        } else {
	            System.out.println("No email found with the specified ID.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error in deleting email.");
	        e.printStackTrace();
	    }
	}


   public static void updateSessionsToDatabase(Map<String, SessionData> sessionData) {
	   System.out.println("updatin session details to db");
	   try (Connection connection = Database.getConnection()) {
           connection.setAutoCommit(false); // Start transaction

           String updateQuery = "UPDATE sessions SET last_accessed_time = ?  WHERE session_id = ? and user_id = ?;";

           try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
               for (String sessionid: sessionData.keySet()) {
                   SessionData sessiondata = sessionData.get(sessionid);
                   
                   if (sessiondata != null) {
                       // Set the parameters
                       preparedStatement.setLong(1, sessiondata.getLast_accessed_at());
                       preparedStatement.setString(2, sessionid);
                       preparedStatement.setInt(3,sessiondata.getUser_id());
                       preparedStatement.addBatch();
                   }
               }

               preparedStatement.executeBatch();
               connection.commit(); // Commit the transaction
           } catch (SQLException e) {
               connection.rollback(); // Rollback on error
               e.printStackTrace();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
   

   public static void insertUsertoSession(String s_id, User u) {
	// TODO Auto-generated method stub
	   String statement = "Insert into sessions values (?,?,?,?);";
	   
	   try (Connection c = Database.getConnection();
			   PreparedStatement ps = c.prepareStatement(statement)) {
		
		   ps.setString(1,s_id);
		   ps.setInt(2,u.getUser_id());
		   ps.setLong(3, System.currentTimeMillis());
		   ps.setLong(4, System.currentTimeMillis());
		   
		   int success = ps.executeUpdate();
		   
		   
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	
	}

   public static void inactivateSession(String sid) {
	   String statement = "delete from sessions where session_id = ?;";
	   
	   try(Connection c = Database.getConnection();
			   PreparedStatement ps = c.prepareStatement(statement)){
		   ps.setString(1, sid);
		   int success = ps.executeUpdate();
	   }catch(SQLException e) {
		   e.printStackTrace();
	   }
	   
	}
   
   public static void fetchSessionsFromDatabase() {
	   	Map< String, SessionData> sess_map = SessionDataManager.session_data;
	   	String statement = "Select * from sessions";
	   	try(Connection c = Database.getConnection();
	   			PreparedStatement ps = c.prepareStatement(statement)){
	   		ResultSet rs = ps.executeQuery();
	   		while(rs.next()) {
	   			String session_id = rs.getString("session_id");
	   			int user_id = rs.getInt("user_id");
	   			long created_at = rs.getLong("created_time");
	   			long last_accessed_at = rs.getLong("last_accessed_time");
	   			long expires_at = last_accessed_at + 1800000;
	   			
	   			sess_map.put(session_id, new SessionData(user_id,created_at,last_accessed_at,expires_at));
	   		}
	   		System.out.println(SessionDataManager.session_data);
	   		System.out.println("Retrieved sessions from database succesfully");
	   	}catch(SQLException e) {
	   		e.printStackTrace();
	   	}
   }
   
   public static void removeUserfromSessions(String sid) {
		// TODO Auto-generated method stub
	   String statement = "delete from sessions where session_id = ?";
	   try (Connection c = Database.getConnection();
			  PreparedStatement ps =c.prepareStatement(statement)){
		   ps.setString(1,sid);
		   ps.executeUpdate();
	} catch (SQLException e) {
		// TODO: handle exception
		System.out.println("inside dao remove user from session method");
		e.printStackTrace();
	}
		
	}

}
