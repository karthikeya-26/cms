package com.tables;

public enum ContactMails implements Columns {
    CONTACT_ID("contact_id", Integer.class),
    MAIL("mail", String.class),
	ALL_COLS("*", null);
	

    private String columnName;
    private Class<?> dataType = null;

    ContactMails(String columnName, Class<?> dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }
    @Override
    public String value() {
        return columnName;
    }
	@Override
	public Class<?> getDataType() {
		return this.dataType;
	}
   
}
