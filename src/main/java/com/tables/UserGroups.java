package com.tables;

public enum UserGroups implements Columns {
    GROUP_ID("group_id"),
    GROUP_NAME("group_name"),
    USER_ID("user_id"),
	ALL_COLS("*");

    private final String columnName;

    UserGroups(String columnName) {
        this.columnName = columnName;
    }

    public String value() {
        return columnName;
    }
}
