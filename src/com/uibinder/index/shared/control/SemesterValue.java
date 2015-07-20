package com.uibinder.index.shared.control;

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

}
