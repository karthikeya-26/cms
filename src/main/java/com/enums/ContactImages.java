package com.enums;

import java.sql.Blob;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ContactImages implements Columns{
	
	CONTACT_ID("contact_id",Integer.class),
	LOW_RES("low_res",Blob.class),
	HIGH_RES("high_res", Blob.class),
	ALL_COLS("*",null);

	private static final Map<String,ContactImages> LOOKUP_MAP = new HashMap<String, ContactImages>();
	
	static {
		for(ContactImages image : ContactImages.values()) {
			LOOKUP_MAP.put(image.value(), image);
		}
	}
	private final String colName;
	private final Class<?> dataType;
	
	private ContactImages(String colName, Class<?> dataType) {
		this.colName = colName;
		this.dataType = dataType;
	}
	
	@Override
	public String value() {
		return colName;
	}

	@Override
	public Class<?> getDataType() {
		return dataType;
	}
	
	public static ContactImages getCol(String colName) {
		ContactImages images = LOOKUP_MAP.get(colName);
		if(images == null) {
			throw new IllegalArgumentException("Column name "+ colName +" does not exist.");
		}
		return images;
	}
	
	public static List<Columns> getAllCols() {
		return Arrays.asList(ContactImages.values());
	}
	
	
}
