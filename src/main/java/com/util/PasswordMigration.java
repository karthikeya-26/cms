package com.util;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.UserDetailsObj;
import com.enums.Operators;
import com.enums.Passwords;
import com.enums.Table;
import com.enums.UserDetails;
import com.queryLayer.*;

public class PasswordMigration {

//	private static boolean updating_passwords = false;
//
//	public static Integer start;
//	public static Integer end;
//
//	public static List<Query> updates_to_process;
//
//	public static void exportPasswords(int totalUsers) {
//		updates_to_process = new ArrayList<Query>();
//		updating_passwords = true;
//		int totalUpdates = totalUsers / 1000;
//		System.out.println(totalUpdates);
//
//		for (start = 0; start < totalUsers; start += 1000) {
//			Select getUsers = new Select();
//			end = start + 1000;
//			getUsers.table(Table.UserDetails)
//					.condition(UserDetails.USER_ID, Operators.GreaterThanOrEqualTo, start.toString())
//					.condition(UserDetails.USER_ID, Operators.LessThan, end.toString());
//
//			List<ResultObject> users = getUsers.executeQuery(UserDetailsObj.class);
//			for (ResultObject user : users) {
//				UserDetailsObj user_obj = (UserDetailsObj) user;
//				Insert insertPassword = new Insert();
//				insertPassword.table(Table.Passwords)
//						.columns(Passwords.USER_ID, Passwords.PASSWORD, Passwords.PW_VERSION, Passwords.MODIFIED_AT)
//						.values(user_obj.getUser_id().toString(), user_obj.getPassword(),
//								user_obj.getPw_version().toString(), user_obj.getPw_last_changed_at().toString());
//				System.out.println(insertPassword.build());
//
//				if (updates_to_process.size() > 0) {
//					for (Query q : updates_to_process) {
//						if (q.getClass().getSimpleName().equals("Update")) {
//							Update u = (Update) q;
//							if (u.getTableName().equals("UserDetails")) {
//
//								// need to change the column name
//
//								u.table(Table.Passwords);
//								u.executeUpdate();
//							}
//						}
//					}
//				}
//			}
//		}
//
//		updating_passwords = false;
//	}
	
	public static void migrate() {
		//Insert If NOT EXISTS ..................
	}
	
	
}
