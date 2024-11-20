package com.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public enum Table {
    UserDetails("UserDetails", com.tables.UserDetails.getAllCols()),
    UserGroups("UserGroups", com.tables.UserGroups.getAllCols()),
    UserMails("UserMails", com.tables.UserMails.getAllCols()),
    Contacts("Contacts", com.tables.Contacts.getAllCols()),
    ContactMobileNumbers("ContactMobileNumbers", com.tables.ContactMobileNumbers.getAllCols()),
    ContactMails("ContactMails", com.tables.ContactMails.getAllCols()),
    GroupContacts("GroupContacts", com.tables.GroupContacts.getAllCols()),
    Sessions("Sessions", com.tables.Sessions.getAllCols()),
    Servers("Servers", com.tables.Sessions.getAllCols()),
    Passwords("Passwords", com.tables.Passwords.getAllCols()),
	ChangeLog("ChangeLog", com.tables.ChangeLog.getAllCols());

    private static final Map<String, Table> TABLE_LOOKUP_MAP = new HashMap<>();

    private final String tableName;
    private final List<Columns> tableColumns;

    static {
        // Populate the lookup maps
        for (Table table : Table.values()) {
            TABLE_LOOKUP_MAP.put(table.tableName, table);
        }
    }

    // Constructor for tables with pre/post-update logic
    Table(String tableName, List<Columns> tableColumns) {
        this.tableName = tableName;
        this.tableColumns = tableColumns;
    }

    // Constructor for tables without pre/post-update logic
    Table(String tableName) {
        this(tableName, null);
    }

    public String value() {
        return tableName;
    }
    
    public List<Columns> getColumns(){
    	return tableColumns;
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
