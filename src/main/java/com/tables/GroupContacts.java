package com.tables;

public enum GroupContacts implements Columns {
    GROUP_ID("group_id"),
    CONTACT_ID("contact_id"),
	ALL_COLS("*");

    private final String columnName;

    GroupContacts(String columnName) {
        this.columnName = columnName;
    }

    public String value() {
        return columnName;
    }
}

