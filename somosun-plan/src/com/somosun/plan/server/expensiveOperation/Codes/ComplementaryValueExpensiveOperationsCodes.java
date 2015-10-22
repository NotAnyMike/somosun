package com.somosun.plan.server.expensiveOperation.Codes;

public class ComplementaryValueExpensiveOperationsCodes {

	public final static String NOTHING = "0";
	public final static String DELETE_ALL = "1";
	public final static String DELETE_FOR_CAREER = "2";
	
	
	public static boolean isValid(String code) {
		boolean toReturn = false;
		if(code.matches(NOTHING + "|" + DELETE_ALL + "|" + DELETE_FOR_CAREER)){
			toReturn = true;
		}
		return toReturn;
	}
	
	public static String getValidValue(String code) {
		String toReturn = null;
		if(code.matches(NOTHING + "|" + DELETE_ALL + "|" + DELETE_FOR_CAREER)){
			toReturn = code;
		}else{
			toReturn = NOTHING;
		}
		return toReturn;
	}
	
}
