package com.tables;

public enum UserDetails implements Columns {
    USER_ID("user_id"),
    USER_NAME("user_name"),
    PASSWORD("password"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    CONTACT_TYPE("contact_type"),
	ALL_COLS("*");

    private final String columnName;

    UserDetails(String columnName) {
        this.columnName = columnName;
    }

    public String value() {
        return columnName;
    }
}
