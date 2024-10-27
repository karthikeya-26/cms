package com.tables;

public enum ContactMails implements Columns {
    CONTACT_ID("contact_id"),
    MAIL("mail"),
	ALL_COLS("*");
	

    private final String columnName;

    ContactMails(String columnName) {
        this.columnName = columnName;
    }
    @Override
    public String value() {
        return columnName;
    }
}
