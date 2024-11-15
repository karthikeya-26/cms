package com.tables;

public enum Passwords implements Columns{
	ID("id",Integer.class),
	USER_ID("user_id",Integer.class),
	PASSWORD("password",String.class),
	PW_VERSION("pw_version",Integer.class),
	MODIFIED_AT("modified_at", Integer.class);
	
	private final String columnName;
    private Class<?> dataType;

    Passwords(String columnName, Class<?> dataType) {
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
