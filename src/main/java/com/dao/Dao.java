// Source code is decompiled from a .class file using FernFlower decompiler.
package com.dao;

import com.dbconn.BCrypt;
import com.dbconn.Database;
import com.models.Contact;
import com.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;

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
	        // Close resources
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


   public static User loginUser(String username, String password) throws NamingException, SQLException {
	    User u = new User();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Database.getConnection();
	        
	        // SQL query to get user details
	        String statement = "SELECT * FROM user_details JOIN user_mails ON user_details.user_id = user_mails.user_id WHERE mail = ?";
	        ps = c.prepareStatement(statement);
	        ps.setString(1, username);
	        
	        rs = ps.executeQuery();
	        
	        // If no user found, return empty User object
	        if (!rs.next()) {
	            return u;
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
	            u.setMail(rs.getString("mail"));
	            return u;
	        }
	        
	        return u;  // Return empty user if password check fails
	        
	    } catch (SQLException e) {
	        System.out.println("Error in loginUser method:");
	        e.printStackTrace();
	        return u;
	        
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

   public static ArrayList<String> getUserEmails(User u, String login_mail) {
	    ArrayList<String> userMails = new ArrayList<>();
	    String statement = "SELECT * FROM user_mails WHERE user_id = ?";
	    
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
	            // Add email to list if it's not the login mail
	            if (!email.equals(login_mail)) {
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


   public static boolean checkifMailExists(User user, String email) {
	    String statement = "SELECT mail FROM user_mails WHERE user_id = ?";
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

}
