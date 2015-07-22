package com.uibinder.server.dao;

public class Dao {
	
	/**
	 * This function will delete: <br>
	 * - "´" accents (e.g "é"->"e") <br>
	 * - " " -> ""<br>
	 * will NOT delete:<br>
	 * - "ñ"<br>
	 * - other accents "`", etc.<br>
	 * 
	 * @param s
	 * @return
	 */
	protected static String standardizeString(String s){
		String stringToReturn = s.trim();
		stringToReturn = stringToReturn.toLowerCase()
				.replaceAll("&nbsp;", " ")
				.replaceAll(" ", " ")
				.replaceAll("á", "a")
				.replaceAll("é", "e")
				.replaceAll("í", "i")
				.replaceAll("ó", "o")
				.replaceAll("ú", "u");

		return stringToReturn;
	}

}
