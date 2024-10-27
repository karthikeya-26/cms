package com.tables;

public enum ContactMobileNumbers implements Columns {
    CONTACT_ID("contact_id"),
    NUMBER("number"),
	ALL_COLS("*");

    private final String columnName;

    ContactMobileNumbers(String columnName) {
        this.columnName = columnName;
    }

    public String value() {
        return columnName;
    }
}
