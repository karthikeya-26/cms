package com.tables;

public enum UserMails implements Columns {
    MAIL_ID("mail_id"),
    MAIL("mail"),
    USER_ID("user_id"),
    IS_PRIMARY("is_primary"),
	ALL_COLS("*");

    private final String columnName;

    UserMails(String columnName) {
        this.columnName = columnName;
    }
    @Override
    public String value() {
        return columnName;
    }
}
