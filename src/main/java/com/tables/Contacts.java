package com.tables;

public enum Contacts implements Columns {
    CONTACT_ID("contact_id",Integer.class),
    FIRST_NAME("first_name",String.class),
    LAST_NAME("last_name",String.class),
    USER_ID("user_id",Integer.class),
    ADDRESS("address",String.class),
    CREATED_AT("created_at",Long.class),
	ALL_COLS("*",null);

    private final String columnName;
    private Class<?> dataType;

    Contacts(String columnName, Class<?> dataType) {
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
