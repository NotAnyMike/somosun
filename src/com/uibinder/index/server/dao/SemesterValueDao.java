package com.uibinder.index.server.dao;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.SemesterValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SemesterValueDao {

	static{
		ObjectifyService.register(SemesterValue.class);
	}
	
	public void saveSemesterValue(SemesterValue semesterValue){
		ofy().save().entity(semesterValue).now();
	}
	
	public void saveSemesterValue(int year, int numberSemester){
		ofy().save().entity(new SemesterValue(year, numberSemester)).now();
	}
	
	public SemesterValue getSemester(int year, int numberSemester){
		return (SemesterValue) ofy().load().type(SemesterValue.class).filter("year", year).filter("numberSemester", numberSemester).first().now();
	}
	
	public SemesterValue getCurrentSemester(){
		SemesterValue semesterValueToReturn = null;
		semesterValueToReturn = getSemester(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
		if(semesterValueToReturn == null){
			semesterValueToReturn = new SemesterValue(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
			saveSemesterValue(semesterValueToReturn);
		}
		return semesterValueToReturn;
	}
	
}
