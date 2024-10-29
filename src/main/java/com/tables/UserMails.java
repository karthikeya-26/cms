package com.tables;

public enum UserMails implements Columns {
    MAIL_ID("mail_id", Integer.class),
    MAIL("mail", String.class),
    USER_ID("user_id",Integer.class),
    IS_PRIMARY("is_primary", Integer.class),
	ALL_COLS("*", null);

    private final String columnName;
    private Class<?> dataType;

    UserMails(String columnName, Class<?> dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }
    @Override
    public String value() {
        return columnName;
    }
    @Override
    public Class<?> getDataType(){
    	return dataType;
    }
}
