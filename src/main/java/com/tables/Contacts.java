package com.tables;

public enum Contacts implements Columns {
    CONTACT_ID("contact_id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    USER_ID("user_id"),
    ADDRESS("address"),
    CREATED_AT("created_at"),
	ALL_COLS("*");

    private final String columnName;

    Contacts(String columnName) {
        this.columnName = columnName;
    }
    @Override
    public String value() {
        return columnName;
    }
}
