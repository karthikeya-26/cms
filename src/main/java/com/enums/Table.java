package com.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Table {
	UserDetails("user_details", com.enums.UserDetails.getAllCols(), com.enums.UserDetails.class),
	UserGroups("user_groups", com.enums.UserGroups.getAllCols(), com.enums.UserGroups.class),
	UserMails("user_mails", com.enums.UserMails.getAllCols(), com.enums.UserMails.class),
	Contacts("contacts", com.enums.Contacts.getAllCols(), com.enums.Contacts.class),
	ContactMobileNumbers("contact_mobile_numbers", com.enums.ContactMobileNumbers.getAllCols(),
			com.enums.ContactMobileNumbers.class),
	ContactMails("contact_mails", com.enums.ContactMails.getAllCols(), com.enums.ContactMails.class),
	GroupContacts("group_contacts", com.enums.GroupContacts.getAllCols(), com.enums.GroupContacts.class),
	Sessions("sessions", com.enums.Sessions.getAllCols(), com.enums.Sessions.class),
	Servers("servers", com.enums.Servers.getAllCols(), com.enums.Servers.class),
	Passwords("passwords", com.enums.Passwords.getAllCols(), com.enums.Passwords.class),
	ChangeLog("change_log", com.enums.ChangeLog.getAllCols(), com.enums.ChangeLog.class),
	Configurations("configurations", com.enums.Configurations.getAllCols(), com.enums.Configurations.class),
	ContactsSync("contacts_sync", com.enums.ContactsSync.getAllCols(), com.enums.ContactsSync.class),
	ClientDetails("client_details", com.enums.ClientDetails.getAllCols(), com.enums.ClientDetails.class),
	RedirectUris("redirect_uris", com.enums.RedirectUris.getAllCols(), com.enums.RedirectUris.class),
	AuthorizationCodes("authorization_codes", com.enums.AuthorizationCodes.getAllCols(),
			com.enums.AuthorizationCodes.class),
	AccessTokens("access_tokens", com.enums.AccessTokens.getAllCols(), com.enums.AccessTokens.class),
	RefreshTokens("refresh_tokens", com.enums.RefreshTokens.getAllCols(), com.enums.RefreshTokens.class);

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
