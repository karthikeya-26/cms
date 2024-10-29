package com.queryBuilder;

public class CheckDataType {
	public static boolean isLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public static boolean isInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public static boolean isFloat(String value) {
		try {
			Float.parseFloat(value);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	
}
