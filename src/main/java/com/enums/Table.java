package com.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Table {
	UserDetails("UserDetails", com.enums.UserDetails.getAllCols(), com.enums.UserDetails.class),
	UserGroups("UserGroups", com.enums.UserGroups.getAllCols(), com.enums.UserGroups.class),
	UserMails("UserMails", com.enums.UserMails.getAllCols(), com.enums.UserMails.class),
	Contacts("Contacts", com.enums.Contacts.getAllCols(), com.enums.Contacts.class),
	ContactMobileNumbers("ContactMobileNumbers", com.enums.ContactMobileNumbers.getAllCols(),
			com.enums.ContactMobileNumbers.class),
	ContactMails("ContactMails", com.enums.ContactMails.getAllCols(), com.enums.ContactMails.class),
	GroupContacts("GroupContacts", com.enums.GroupContacts.getAllCols(), com.enums.GroupContacts.class),
	Sessions("Sessions", com.enums.Sessions.getAllCols(), com.enums.Sessions.class),
	Servers("Servers", com.enums.Sessions.getAllCols(), com.enums.Servers.class),
	Passwords("Passwords", com.enums.Passwords.getAllCols(), com.enums.Passwords.class),
	ChangeLog("ChangeLog", com.enums.ChangeLog.getAllCols(), com.enums.ChangeLog.class),
	Configurations("Configurations", com.enums.Configurations.getAllCols(), com.enums.Configurations.class),
	ContactsSync("ContactsSync", com.enums.ContactsSync.getAllCols(), com.enums.ContactsSync.class),
	ClientDetails("ClientDetails", com.enums.ClientDetails.getAllCols(), com.enums.ClientDetails.class),
	RedirectUris("RedirectUris", com.enums.RedirectUris.getAllCols(), com.enums.RedirectUris.class),
	AuthorizationCodes("AuthorizationCodes",com.enums.AuthorizationCodes.getAllCols(),com.enums.AuthorizationCodes.class),
	AccessTokens("AccessTokens",com.enums.AccessTokens.getAllCols(),com.enums.AccessTokens.class),
	RefreshTokens("RefreshTokens", com.enums.RefreshTokens.getAllCols(), com.enums.RefreshTokens.class);
	
	private static final Map<String, Table> TABLE_LOOKUP_MAP = new HashMap<>();

	private final String tableName;
	private final List<Columns> tableColumns;
	private final Class<? extends Columns> columnClass;

	static {
		// Populate the lookup maps
		for (Table table : Table.values()) {
			TABLE_LOOKUP_MAP.put(table.tableName, table);
		}
	}

	Table(String tableName, List<Columns> tableColumns, Class<? extends Columns> columnClass) {
		this.tableName = tableName;
		this.columnClass = columnClass;
		this.tableColumns = tableColumns;
	}

	public String value() {
		return tableName;
	}

	public List<Columns> getColumns() {
		return tableColumns;
	}

	public Class<?> getColumnClass() {
		return columnClass;
	}

	// Lookup method to get the enum constant by table name
	public static Table getTableByName(String tableName) {
		Table table = TABLE_LOOKUP_MAP.get(tableName);
		if (table == null) {
			throw new IllegalArgumentException("Table name " + tableName + " does not exist.");
		}
		return table;
	}

}
