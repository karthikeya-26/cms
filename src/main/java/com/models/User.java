// Source code is decompiled from a .class file using FernFlower decompiler.
package com.models;

import java.util.ArrayList;

public class User {
   private int user_id;
   private String user_name;
   private String primaryEmail;
   private String first_name;
   private String last_name;
   private String account_type;
   private ArrayList<Contact> user_contacts;
   private ArrayList<Contact> group_contacts;
   private ArrayList<String> othermails;
   private ArrayList<String> user_groups;

   public User() {
   }

   public User(int user_id, String user_name, String mailusedforlogin, String first_name, String last_name, String account_type, ArrayList<Contact> contacts, ArrayList<Contact> groupContacts, ArrayList<String> othermails) {
      this.user_contacts = contacts;
      this.user_id = user_id;
      this.user_name = user_name;
      this.first_name = first_name;
      this.last_name = last_name;
      this.account_type = account_type;
      this.setmails(othermails);
      this.setGroup_contacts(groupContacts);
   }

   public String getUser_name() {
      return this.user_name;
   }

   public void setUser_name(String user_name) {
      this.user_name = user_name;
   }

   public String getFirst_name() {
      return this.first_name;
   }

   public void setFirst_name(String first_name) {
      this.first_name = first_name;
   }

   public String getLast_name() {
      return this.last_name;
   }

   public void setLast_name(String last_name) {
      this.last_name = last_name;
   }

   public String getAccount_type() {
      return this.account_type;
   }

   public void setAccount_type(String account_type) {
      this.account_type = account_type;
   }

   public int getUser_id() {
      return this.user_id;
   }

   public void setUser_id(int user_id) {
      this.user_id = user_id;
   }

   public ArrayList<Contact> getUser_contacts() {
      return this.user_contacts;
   }

   public void setUser_contacts(ArrayList<Contact> user_contacts) {
      this.user_contacts = user_contacts;
   }

   public ArrayList<Contact> getGroup_contacts() {
      return this.group_contacts;
   }

   public void setGroup_contacts(ArrayList<Contact> group_contacts) {
      this.group_contacts = group_contacts;
   }

   public ArrayList<String> getmails() {
      return this.othermails;
   }

   public void setmails(ArrayList<String> othermails) {
      this.othermails = othermails;
   }

   public ArrayList<String> getUserGroups() {
      return this.user_groups;
   }

   public void setUserGroups(ArrayList<String> user_groups) {
      this.user_groups = user_groups;
   }

public String getPrimaryEmail() {
	return primaryEmail;
}

public void setPrimaryEmail(String primaryEmail) {
	this.primaryEmail = primaryEmail;
}
}
