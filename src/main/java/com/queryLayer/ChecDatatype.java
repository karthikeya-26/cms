package com.queryLayer;

public class ChecDatatype {

	public static boolean isDouble(Object value) {
		try {
			Double.parseDouble((String) value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
