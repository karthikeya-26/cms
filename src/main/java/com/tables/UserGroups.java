package com.tables;

public enum UserGroups implements Columns {
    GROUP_ID("group_id",Integer.class),
    GROUP_NAME("group_name",String.class),
    USER_ID("user_id",Integer.class),
	ALL_COLS("*",null);

    private final String columnName;
    private Class<?> dataType;

    UserGroups(String columnName, Class<?> dataType) {
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
