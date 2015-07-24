package com.uibinder.shared;

import java.util.Arrays;

import com.uibinder.shared.control.Career;

/**
 * This class will contain methods useful to every class in every side of the app
 * 
 * @author MW
 *
 */
public class SomosUNUtils {

	public static final String LIBRE_CODE = "libre";
	public static final String LIBRE_NAME = "libre eleccion";
	public static final String OPTATIVA_CODE ="opativa";
	public static final String OPTATIVA_NAME ="opativa";
	
	public static final String COLOR_CO = "9C27B0";
	public static final String COLOR_PRE = "E91E63";
	
	/**
	 * This method will recive a career and return a string of the code followed by it name<br></br>
	 * e.g. @return "(2522) Administración de empresas" <br></br>
	 * 
	 * @param c : a career
	 * @return the code of the career inside parenthesis followed by it name
	 */
	public static String addCarrerCodeToString(Career c){
		String toReturn = null;
		if(c != null){
			toReturn = "(" + c.getCode() + ") " + c.getName().toUpperCase().charAt(0) + c.getName().substring(1);
		}
		return toReturn;
	}
	
	/** 
	 * This function will delete: <br>
	 * - WHITE SPACES!!!!<br>
	 * - "´" accents (e.g "é"->"e") <br>
	 * - "-" -> ""<br>
	 * - " " -> ""<br>
	 * - "s" -> ""<br>
	 * - "/" -> ""<br>
	 * will NOT delete:<br>
	 * - "ñ"<br>
	 * 
	 * @param s
	 * @return
	 */
	public static String standardizeString(String s, boolean removeS){
		String stringToReturn = null;
		if(s != null){
			stringToReturn = s.trim();
			stringToReturn = stringToReturn.toLowerCase()
					.replaceAll("&nbsp;", "")
					.replaceAll(" ", "") //this space is different from the below one, this is un html (the above one) DO NOT DELETE!
					.replaceAll(" ", "");
			stringToReturn = removeAccents(stringToReturn);
			stringToReturn
					.replaceAll("-", "")
					.replaceAll("/", "");
			if(removeS == true) stringToReturn = stringToReturn.replaceAll("s", "");
		}
		
		return stringToReturn;
	}

	/**
	 * This will make it to lowercase and remove all accents from the vocals
	 * 
	 * @param s
	 * @return
	 */
	public static String removeAccents(String s) {
		String sToReturn = null;
		if(s != null)
		{			
			sToReturn = s.toLowerCase()
					.replaceAll("á", "a")
					.replaceAll("é", "e")
					.replaceAll("í", "i")
					.replaceAll("ó", "o")
					.replaceAll("ú", "u")
					.replaceAll("à", "a")
					.replaceAll("è", "e")
					.replaceAll("ì", "i")
					.replaceAll("ò", "o")
					.replaceAll("ù", "u");

		}
		return sToReturn;
	}

	public static int getFirstNumberFromString(String s) {
		int toReturn = -1;
		s = s.replaceAll("[^0-9]+", " ");
		s = Arrays.asList(s.trim().split(" ")).get(0);
		toReturn = Integer.valueOf(s);
		
		return toReturn;
	}
	
	/**
	 * 
	 * Will transform the @param t into a reading typology Type
	 * 
	 * @param t
	 * @return
	 */
	public static String getTypology(String t){
		String tToReturn = t;
		if(t!= null){			
			switch(tToReturn){
				case "d":
					tToReturn ="c";
					break;
				case "n":
					tToReturn = "p";
					break;
				case "f":
				case "fundamental":
					tToReturn = "b";
					break;
				case "l":
					tToReturn = "l";
					break;
			}
		}
		return tToReturn;
	}

}
