package com.tables;

public enum ContactMobileNumbers implements Columns {
    CONTACT_ID("contact_id", Integer.class),
    NUMBER("number", Long.class),
	ALL_COLS("*", null);

    private final String columnName;
    private Class<?> dataType = null;

    ContactMobileNumbers(String columnName, Class<?> dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String value() {
        return columnName;
    }

	@Override
	public Class<?> getDataType() {
		// TODO Auto-generated method stub
		return dataType;
	}
}
