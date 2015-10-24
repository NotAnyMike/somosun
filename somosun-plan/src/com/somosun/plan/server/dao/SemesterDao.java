package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SubjectValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SemesterDao implements Dao<Semester> {
	
	static{
		ObjectifyService.register(Semester.class);
	}
	
	public Long save(Semester s){
		Long toReturn = null;
		if(s != null) {
			List<SubjectValue> subjectValuesList = s.getSubjects();
			SubjectValueDao sVDao = new SubjectValueDao();
			SemesterValueDao semesterValueDao = new SemesterValueDao();
			GroupDao gDao = new GroupDao();
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			for(SubjectValue sV : subjectValuesList){
				if(sV.getId() == null){
					if(sV.getGroup() != null){
						if(sV.getGroup().getId() == null){
							gDao.save(sV.getGroup());
						}
					}
					if(sV.getComplementaryValues() != null){
						if(sV.getComplementaryValues().getId() == null){
							cVDao.save(sV.getComplementaryValues());
						}
					}
					sVDao.save(sV);
				}
			}
			if(s.getSemesterValue() != null && s.getSemesterValue().getId() == null){
				SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(s.getSemesterValue().getYear(), s.getSemesterValue().getNumberSemester());
				s.setSemesterValue(semesterValue);
			}
			ofy().save().entity(s).now();
			toReturn = s.getId();
		}
		return toReturn;
	}
	
	public Long generateId(){
		
		 ObjectifyFactory f = new ObjectifyFactory();
		 Key<Semester> key = f.allocateId(Semester.class);
		 return key.getId();
		
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<Semester> key = Key.create(Semester.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllSemesters() {
		List<Semester> list = getAllSemesters();
		for(Semester s : list){
			delete(s.getId());
		}
	}

	private List<Semester> getAllSemesters() {
		return ofy().load().type(Semester.class).list(); 
	}

	public Semester getById(Long id){
		Semester toReturn = null;
		if(id != null){
			Key<Semester> key = Key.create(Semester.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}
	
}
