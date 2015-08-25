package com.somosun.plan.shared.values;

public class TypologyCodes {

	/**
	 * nivelación code
	 */
	public static final String NIVELACION = "p";
	
	/**
	 * Libre elección code
	 */
	public static final String LIBRE_ELECCION = "l";

	/**
	 * Disciplinar / Profesional
	 */
	public static final String PROFESIONAL = "c";
	
	/**
	 * Fundamentación
	 */
	public static final String FUNDAMENTACION = "b";
	
	public TypologyCodes(){
	}
	
	public static String getTypology(String t){
		String tToReturn = t;
		switch(tToReturn){
		case "d":
			tToReturn = PROFESIONAL;
			break;
		case "n":
			tToReturn = NIVELACION;
			break;
		case "f":
		case "fundamental":
			tToReturn = FUNDAMENTACION;
			break;
		case "l":
			tToReturn = LIBRE_ELECCION;
			break;
		}
		return tToReturn;
	}
}
