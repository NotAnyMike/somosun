package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * This class will save the semester in which the group is.
 * 
 * @author Mike
 *
 */
@Entity
public class SemesterValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id=null;
	@Index private int year;
	@Index private int numberSemester;
	
	public static final int CURRENT_YEAR = 2015;
	public static final int CURRENT_NUMBER_SEMESTER = 2;
	
	public SemesterValue(){
	}
	
	public SemesterValue(int year, int numberSemester){
		this.year = year;
		this.numberSemester = numberSemester;
	}
	
	/**
	 * Will create a semesterValue @param addSemester in the future/past(if negative) of the @semesterValueFrom
	 * @param semesterValueFrom
	 * @param addSemesters
	 */
	public SemesterValue(SemesterValue semesterValueFrom, int addSemesters){
		int year = semesterValueFrom.getYear();
		int numberSemester = semesterValueFrom.getNumberSemester();
		
		if(addSemesters != 0){
			int sign = 1;
			int toAdd = 0;
			if(addSemesters<0) sign = -1;
			numberSemester = ((numberSemester - 1) + sign*(addSemesters%2))%2 + 1;
			if(numberSemester == 1 && sign > 0){
				toAdd = 1;
			}
			year = year + (addSemesters/2) + toAdd;
		}
		
		this.year = year;
		this.numberSemester = numberSemester;
		
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getNumberSemester() {
		return numberSemester;
	}
	
	public void setNumberSemester(int numberSemester) {
		this.numberSemester = numberSemester;
	}
	
	public Long getId() {
		return id;
	}

	public boolean isCurrentSemester() {
		if(this.getNumberSemester() == CURRENT_NUMBER_SEMESTER && this.getYear() == CURRENT_YEAR) return true;
		else return false;
	}
	
	public boolean equals(SemesterValue s){
		if(this.getNumberSemester() == s.getNumberSemester() && this.getYear() == s.getYear()) return true;
		else return false;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * This method will return the amount of semesters between the two semesterValues, negative if the @param semesterValue is higher than this semesterValue
	 * 
	 * @param semesterValue
	 * @return
	 */
	public int minus(SemesterValue semesterValue){
		
		int toReturn = 0;
		
		if(semesterValue != null){
			toReturn = (year - semesterValue.getYear())*2 + (numberSemester - semesterValue.getNumberSemester());
		}
		
		return toReturn;
	}
	
	public String toString(){
		return year + "-" + numberSemester;
	}

	public String toStringDouble() {
		return "" + year + "." + numberSemester;
	}

}
