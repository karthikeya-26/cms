package com.queryLayer;

import com.tables.*;

public class Test {
	public static void main(String args[]) {
		
		//dao queries
		
//		1.Select queries
		
//		login user method
		Select getuserfromdb = new Select();
		getuserfromdb.table(Table.UserDetails).column(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "?");
		System.out.println(getuserfromdb.build());
		System.out.println();
		
		Select getusermails = new Select();
		getusermails.table(Table.UserMails).columns(UserMails.ALL_COLS).condition(UserMails.USER_ID, Operators.Equals, "1");
		System.out.println(getusermails.build());
		System.out.println();
		
		Select checkifMailExists = new Select();
		checkifMailExists.table(Table.UserMails).column(UserMails.MAIL).condition(UserMails.USER_ID, Operators.Equals, "1");
		System.out.println(checkifMailExists.build());
		System.out.println();
		
		Select userCOntacts = new Select();
		userCOntacts.table(Table.Contacts, "c").columns("c", Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.ADDRESS)
		.groupConcatAs("m", ContactMobileNumbers.NUMBER, "mobile_number", ",")
		.groupConcatAs("cm", ContactMails.MAIL, "emails", ",")
		.join(Joins.LeftJoin, "m", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		.join(Joins.LeftJoin, "cm", ContactMails.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
		.condition("c",Contacts.USER_ID, Operators.Equals, "1")
		.groupBy("c", Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
		System.out.println(userCOntacts.build());
		System.out.println();
		
		Select getuserGroups = new Select();
		getuserGroups.table(Table.UserGroups).column(UserGroups.GROUP_NAME).condition(UserGroups.USER_ID, Operators.Equals, "1");
		System.out.println(getuserGroups.build());
		
		
//		Select s = new Select();
//		s.table(UserDetails.name).columns(UserDetails.user_name,).max("user_id").join("InnerJoin", "user_details", "user_id", "=", "contacts", "user_id")
//		.join("OuterJoin", "user_details", "user_id", "=", "user_groups", "user_id").groupBy("user_name").orderBy("user_id");
//		String statement = s.buildQuery();
//		System.out.println(statement);
		
		
		//dao get contacts 
//		Select getContacts = new Select().table(Contacts.name).columns(Contacts.contact_id,Contacts.first_name,Contacts.last_name,Contacts.user_id,Contacts.address,"GROUP_CONCAT(DISTINCT "+ContactMobileNumbers.number+")","GROUP_CONCAT(DISTINCT "+ContactMails.mail+")").join("LEFTJOIN", Contacts.name, Contacts.contact_id, "=", ContactMobileNumbers.name, ContactMobileNumbers.contact_id)
//				.join("LEFTJOIN", Contacts.name, Contacts.contact_id, "=", ContactMails.name, ContactMails.contact_id).condition(Contacts.user_id, "=", "?").groupBy(Contacts.contact_id).orderBy(ContactCols.FIRST_NAME.getColumnName());
//		
//		String statement = getContacts.build();
//		System.out.println(statement);
		
//		Select s = new Select();
//        s.table(Table.UserDetails).columns(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserDetails, UserDetails.USER_ID, Operators.Equals, Table.UserMails, UserMails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "j@x.com");
//        Select s = new Select();
//     s.table(Table.UserDetails).columns(UserDetails.ALL_COLS).join(Joins.InnerJoin, Table.UserDetails, UserDetails.USER_ID, Operators.Equals, Table.UserMails, UserMails.USER_ID).condition(UserMails.MAIL, Operators.Equals, "?");
//        s.table(Table.Contacts, "c")
//	    .columns("c",Contacts.FIRST_NAME, Contacts.LAST_NAME, Contacts.ADDRESS, Contacts.USER_ID)
//	    .groupConcatAs("cm",ContactMobileNumbers.NUMBER, "mobile_numbers", ",")
//	    .groupConcatAs("ma",ContactMails.MAIL, "mails", ",")
//	    .groupConcatAs("ug",UserGroups.GROUP_NAME, "user_groups", ",")
//	    .join(Joins.LeftJoin, "cm", ContactMobileNumbers.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//	    .join(Joins.LeftJoin,"ma", ContactMails.CONTACT_ID,Operators.Equals, "c", Contacts.CONTACT_ID)
//	    .join(Joins.InnerJoin, "gc", GroupContacts.CONTACT_ID, Operators.Equals, "c", Contacts.CONTACT_ID)
//	    .join(Joins.InnerJoin, "ug", UserGroups.GROUP_ID, Operators.Equals, "gc",GroupContacts.GROUP_ID)
//	    .condition("c",Contacts.USER_ID, Operators.Equals, "?").groupBy("c",Contacts.CONTACT_ID).orderBy("c", Contacts.FIRST_NAME);
//        
//		s.table(Table.Contacts).columns(Contacts.FIRST_NAME,Contacts.LAST_NAME,Contacts.USER_ID,Contacts.ADDRESS).groupConcat(ContactMails.MAIL).groupConcatAs(ContactMobileNumbers.NUMBER,"Mobiles",",").join(Joins.LeftJoin, Table.Contacts, Contacts.CONTACT_ID, Operators.Equals, Table.ContactMobileNumbers, ContactMobileNumbers.CONTACT_ID)
//		.join(Joins.LeftJoin, Table.Contacts, Contacts.CONTACT_ID, Operators.Equals, Table.ContactMails, ContactMails.CONTACT_ID).groupBy(Contacts.CONTACT_ID).orderBy(Contacts.FIRST_NAME).condition(Contacts.USER_ID, Operators.Equals, "1");
//		String statement = s.build();
//		System.out.println(statement);
//
//		   Update u = new Update();
//		   u.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("0").condition(UserMails.USER_ID, Operators.Equals, "?");
		
		
	}

}
