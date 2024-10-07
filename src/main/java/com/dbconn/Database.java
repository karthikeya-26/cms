// Source code is decompiled from a .class file using FernFlower decompiler.
package com.dbconn;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Database {
   static Context initialContext;
   static DataSource ds;

   static {
      try {
         Class.forName(SqlUser.getDriverClass());
         initialContext = new InitialContext();
         ds = (DataSource)initialContext.lookup("java:comp/env/karthik");
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   public Database() {
   }

   public static Connection getConnection() {
      Connection c = null;

      try {
         c = ds.getConnection();
      } catch (SQLException var2) {
         var2.printStackTrace();
      }

      return c;
   }

   public static void closeConnection(Connection c) {
      if (c != null) {
         try {
            c.close();
         } catch (SQLException var2) {
            var2.printStackTrace();
         }
      }

   }
}
