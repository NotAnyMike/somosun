package com.somosun.plan.server.expensiveOperation.Codes;

public class SubjectValueExpensiveOperationsCodes {

	public final static String NOTHING = "0";
	public final static String DELETE_ALL = "1";
	
	public static String getValidValue(String action){
		 String toReturn = NOTHING;
		 
		 if(action.matches(NOTHING + "|" + DELETE_ALL)){
			 toReturn = action;
		 }
		 
		 return toReturn;
	}
}
