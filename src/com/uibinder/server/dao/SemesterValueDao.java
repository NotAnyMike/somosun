package com.uibinder.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.shared.control.SemesterValue;

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
	
	public SemesterValue getSemesterValue(int year, int numberSemester){
		return (SemesterValue) ofy().load().type(SemesterValue.class).filter("year", year).filter("numberSemester", numberSemester).first().now();
	}
	
	public SemesterValue getCurrentSemester(){
		SemesterValue semesterValueToReturn = null;
		semesterValueToReturn = getSemesterValue(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
		if(semesterValueToReturn == null){
			semesterValueToReturn = new SemesterValue(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
			saveSemesterValue(semesterValueToReturn);
		}
		return semesterValueToReturn;
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SemesterValue> key = f.allocateId(SemesterValue.class);
		return key.getId();
	}

	
	public SemesterValue getOrCreateSemester(int year, int semester) {
		SemesterValue semesterValue = null;
		
		semesterValue = getSemesterValue(year, semester);
		
		if(semesterValue == null){
			semesterValue = new SemesterValue(year, semester);
			semesterValue.setId(generateId());
			saveSemesterValue(semesterValue);
		}
		
		return semesterValue;
	}
	
}