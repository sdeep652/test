package com.tcts.foresight.util;

public class StringUtil {

	public static boolean isNotNullNotEmpty(String value) {
		boolean isNotNullNotEmpty = false;

		if (value != null && !value.trim().equals("")) {
			isNotNullNotEmpty = true;
		}

		return isNotNullNotEmpty;
	}

	public static boolean isNullOrEmpty(String value) {
		boolean isNullOrEmpty = false;

		if (value == null || value.trim().equals("")) {
			isNullOrEmpty = true;
		}

		return isNullOrEmpty;
	}

	
	public static String splitCamelCase(String s) {
		if(isNotNullNotEmpty(s))
		{
			String returnStr = s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
					"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
			returnStr = returnStr.substring(0, 1).toUpperCase() + returnStr.substring(1);
			return returnStr;
		}
		
		return null;
		
	}
	
}
