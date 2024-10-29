package com.tables;

public enum GroupContacts implements Columns {
    GROUP_ID("group_id", Integer.class),
    CONTACT_ID("contact_id", Integer.class),
	ALL_COLS("*", null);

    private final String columnName;
    private Class<?> dataType;

    GroupContacts(String columnName, Class<?> dataType) {
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

