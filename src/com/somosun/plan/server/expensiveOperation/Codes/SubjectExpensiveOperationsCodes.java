package com.somosun.plan.server.expensiveOperation.Codes;

public class SubjectExpensiveOperationsCodes {

	public static final String NOTHING = "0";
	public static final String DELETE_ALL = "1";
	public static final String EDIT = "2";
	
	public static String getValidValue(String option){
		String toReturn = NOTHING;
		
		if(option.matches(NOTHING  + "|"+ DELETE_ALL + "|" + EDIT)){
			toReturn = option;
		}
		
		return toReturn;
	}
	
	public static boolean isValidValue(String option){
		
		boolean toReturn = false;
		
		if(option.matches(NOTHING  + "|"+ DELETE_ALL + "|" + EDIT)){			
			toReturn = true;
		}
		
		return toReturn;
	}
	
}
