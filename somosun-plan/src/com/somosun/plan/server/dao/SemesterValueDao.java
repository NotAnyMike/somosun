package com.somosun.plan.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SemesterValueDao implements Dao<SemesterValue> {

	static{
		ObjectifyService.register(SemesterValue.class);
	}
	
	public Long save(int year, int numberSemester){
		return save(new SemesterValue(year, numberSemester));
	}
	
	public Long save(SemesterValue sV){
		Long toReturn = null;
		if(sV != null){
			if(sV.getId() == null) sV.setId(generateId());
			ofy().save().entity(sV).now();
			toReturn = sV.getId();
		}
		return toReturn;
	}
	
	public SemesterValue getById(Long id){
		SemesterValue toReturn = null;
		if(id != null){
			Key<SemesterValue> key = Key.create(SemesterValue.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id != null){
			Key<SemesterValue> key = Key.create(SemesterValue.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
	public SemesterValue get(int year, int numberSemester){
		return (SemesterValue) ofy().load().type(SemesterValue.class).filter("year", year).filter("numberSemester", numberSemester).first().now();
	}
	
	public SemesterValue getCurrentSemester(){
		SemesterValue semesterValueToReturn = null;
		semesterValueToReturn = get(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
		if(semesterValueToReturn == null){
			semesterValueToReturn = new SemesterValue(SemesterValue.CURRENT_YEAR, SemesterValue.CURRENT_NUMBER_SEMESTER);
			save(semesterValueToReturn);
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
		
		semesterValue = get(year, semester);
		
		if(semesterValue == null){
			semesterValue = new SemesterValue(year, semester);
			semesterValue.setId(generateId());
			save(semesterValue);
		}
		
		return semesterValue;
	}
	
}
