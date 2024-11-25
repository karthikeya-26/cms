// Source code is decompiled from a .class file using FernFlower decompiler.
package com.dto;

public class Contact {
   private int contact_id;
   private String user_id;
   private String first_name;
   private String last_name;
   private String address;
   private String[] mobile_numbers;
   private String[] mail_ids;
   private String[] groups;

   public Contact() {
   }

   public Contact(int contact_id, String first_name, String last_name, String address, String[] mobile_numbers, String[] mail_ids) {
      this.contact_id = contact_id;
      this.first_name = first_name;
      this.last_name = last_name;
      this.address = address;
      this.mobile_numbers = mobile_numbers;
      this.mail_ids = mail_ids;
   }

   public String formattedMobileNumbers() {
      String res = "";
      String[] var5;
      int var4 = (var5 = this.mobile_numbers).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String mob = var5[var3];
         res = res + mob + ", ";
      }

      return res.substring(0, res.length() - 1);
   }

   public int getContact_id() {
      return this.contact_id;
   }

   public void setContact_id(int contact_id) {
      this.contact_id = contact_id;
   }

   public String getUser_id() {
      return this.user_id;
   }

   public void setUser_id(String user_id) {
      this.user_id = user_id;
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

   public String getAddress() {
      return this.address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String[] getMobile_numbers() {
      return this.mobile_numbers;
   }

   public void setMobile_numbers(String[] mobile_numbers) {
      this.mobile_numbers = mobile_numbers;
   }

   public String[] getMail_ids() {
      return this.mail_ids;
   }

   public void setMail_ids(String[] mail_ids) {
      this.mail_ids = mail_ids;
   }

   public String[] getGroups() {
      return this.groups;
   }

   public void setGroups(String[] groups) {
      this.groups = groups;
   }
}
