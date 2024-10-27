package com.tables;

public enum Table {
	UserDetails("UserDetails"),
	UserGroups("UserGroups"),
	UserMails("UserMails"),
	Contacts("Contacts"),
	ContactMobileNumbers("ContactMobileNumbers"),
	ContactMails("ContactMails"),
	GroupContacts("GroupContacts");
	
	
	private String tableName;
	Table(String tableName) {
		this.tableName = tableName;
	}
	
	public String value() {
		return this.tableName;
	}
}
