// Source code is decompiled from a .class file using FernFlower decompiler.
package com.dbconn;

public class SqlUser {
   private static final String URL = "jdbc:mysql://localhost:3306/cms";
   private static final String USER = "root";
   private static final String PASSWORD = "karthik@sql";
   private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

   private SqlUser() {
   }

   public static String getDriverClass() {
      return DRIVER_CLASS;
   }

   public static String getPassword() {
      return PASSWORD;
   }

   public static String getUser() {
      return USER;
   }

   public static String getUrl() {
      return URL;
   }
}
