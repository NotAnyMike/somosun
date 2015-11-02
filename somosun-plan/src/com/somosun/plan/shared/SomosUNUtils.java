package com.somosun.plan.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.i18n.client.NumberFormat;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.values.MessageTypeCodes;
import com.somosun.plan.shared.values.SubjectCodes;

/**
 * This class will contain methods useful to every class in every side of the app
 * 
 * @author MW
 *
 */
public class SomosUNUtils {
	
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
	 * @param removePunctuation 
	 * @return
	 */
	public static String standardizeString(String s, boolean removeS, boolean removePunctuation){
		String stringToReturn = null;
		if(s != null){
			stringToReturn = s.trim();
			stringToReturn = stringToReturn.toLowerCase()
					.replaceAll("&nbsp;", "")
					.replaceAll(" ", "") //this space is different from the below one, this is un html (the above one) DO NOT DELETE!
					.replaceAll(" ", "");
			stringToReturn = removeAccents(stringToReturn);
			stringToReturn = stringToReturn
					.replaceAll("-", "")
					.replaceAll("/", "");
			if(removeS == true) stringToReturn = stringToReturn.replaceAll("s", "");
			if(removePunctuation == true) stringToReturn = removePunctuation(stringToReturn);
		}
		
		return stringToReturn;
	}
	
	public static String removePunctuation(String s){
		String toReturn = null;
		toReturn = s;
	
		if(s!=null){
			toReturn = toReturn.replaceAll("(;)|(,)|(\\.)","");
		}
		
		return toReturn;
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
	
	/**
	 * This method will count how many characters as !"·$%&/()=?¿^*¨Ç_:;,.-ç´`+¡'º contains
	 * 
	 * @param s
	 * @return
	 */
	public int oddCharacters(String s){
		return 0;
	}
	
	/**
	 * This will round @param d and return a string in the form as 12.3
	 * @param d
	 * @return
	 */
	public static String getOneDecimalPointString(Double d){
		String toReturn = null;
		if(d != null){
			double gradeFixed = (double) Math.round(d * 10) / 10;
			toReturn = NumberFormat.getFormat("0.0").format(gradeFixed);
		}
		return toReturn;
	}

	/**
	 * If the type is not a MessageTypeCodes then this method will return MessageTypeCodes.SUGGESTION
	 * @param type
	 * @return
	 */
	public static String setCorrectType(String type) {
		String toReturn = type;
		
		if(type != null){
			if(type.equals(MessageTypeCodes.ERROR) == false && type.equals(MessageTypeCodes.SUGGESTION) == false && type.equals(MessageTypeCodes.OTHER) == false){
				type = MessageTypeCodes.SUGGESTION;
			}
		}
		
		return toReturn;
		
	}
	
	/**
	 * This method will return the subject which match the subject code, if the subjectCode is Optativa 
	 * or FreeElection (generic codes) or has an empty code it will use the id and the name 
	 * (if it's not a default subject) to compare, if the ids match then it returns that subject, null if there is nothing else
	 * <br></br>
	 * This method will use SomosUNUtils.standardizeString(@param subjectName, true, true) to compare the two names 
	 * @param subject
	 * @param subjectId
	 * @param subjectName
	 * @param mandatoryComplementaryValues
	 */
	public static Subject getSubjectFromListByCode(String subjectCode, Long subjectId, String subjectName, List<ComplementaryValue> list) {
		Subject toReturn = null;
		
		for(ComplementaryValue cV : list){
			if(cV.getSubject().isDefault() == false && cV.getSubject().getCode().trim().equals("") == false && subjectCode != null && subjectCode.isEmpty() == false){
				if(cV.getSubject().getCode().equals(subjectCode)){
					toReturn = cV.getSubject();
					break;
				}				
			}else{
				//if could be a free election or an opative (the code is empty or idDefault = true
				if(cV.getSubject().isDefault() == false && SomosUNUtils.standardizeString(cV.getSubject().getName(),true,true).equals(SomosUNUtils.standardizeString(subjectName, true, true)) == true && cV.getSubject().getCode().trim().equals(subjectCode.trim()) == true){
					toReturn = cV.getSubject();
					break;
				}
				else if(cV.getSubject().getId().equals(subjectId) == true && cV.getSubject().getCode().trim().equals(subjectCode.trim()) == true){
					toReturn = cV.getSubject();
					break;
				}
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Returns null if there is no subjectGroup in @param set which has the same @param name
	 * 
	 * @param name
	 * @param keySet
	 * @return
	 */
	public static SubjectGroup getSubjectGroupInSetByName(String name, Set<SubjectGroup> set) {
		SubjectGroup toReturn = null;
		
		for(SubjectGroup sG : set){
			if(sG.getName().equals(name)){
				toReturn = sG;
				break;
			}
		}
		
		return toReturn;
	}

	/**
	 * This method will return null if the code is empty or is a free election or optative generic code
	 * 
	 * @param code
	 * @param list
	 * @return
	 */
	public static ComplementaryValue getSubjectByCodeInList(String code, List<ComplementaryValue> list) {
		ComplementaryValue toReturn = null;
		
		if(code.trim().isEmpty() == false && code.matches("(" + SubjectCodes.LIBRE_CODE + ")|("+ SubjectCodes.LIBRE_NAME + ")|(" + SubjectCodes.OPTATIVA_NAME + ")|(" + SubjectCodes.OPTATIVA_CODE + ")") == false){
			if(list != null && list.isEmpty() == false){
				for(ComplementaryValue cV : list){
					if(cV.getSubject().getCode().equals(code)){
						toReturn = cV;
						break;
					}
				}
			}
		}
			
			
		return toReturn;
	}
	
	/**
	 * This method will only take into account only the no mandatory ones and depending of the @param countDefaultSubjects it will take or not take into account
	 * the default subjects
	 * 
	 * @param list
	 * @return
	 */
	public static Map<SubjectGroup, Integer> getMapWithNumberOfCreditsForEachSubjectGroup(List<ComplementaryValue> list, boolean countDefaultSubjects){
		
		Map<SubjectGroup, Integer> toReturn = new HashMap<SubjectGroup, Integer>();
		
		for(ComplementaryValue cVToSG : list){
			if(cVToSG.isMandatory() == false && cVToSG.getSubject().isDefault() == countDefaultSubjects){
				SubjectGroup sG_temporary = SomosUNUtils.getSubjectGroupInSetByName(cVToSG.getSubjectGroup().getName(), toReturn.keySet());
				int x = -1;
				
				if(sG_temporary != null) {
					x = toReturn.get(sG_temporary);
				} else {
					x = 0;
					sG_temporary = cVToSG.getSubjectGroup();
				}
				
				x += cVToSG.getSubject().getCredits();
				
				toReturn.put(sG_temporary, x);
				
			}
		}
		
		return toReturn;
	}

}
