package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SubjectValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SemesterDao {
	
	static{
		ObjectifyService.register(Semester.class);
	}
	
	public void saveSemester(Semester s){
		if(s != null) {
			List<SubjectValue> subjectValuesList = s.getSubjects();
			SubjectValueDao sVDao = new SubjectValueDao();
			GroupDao gDao = new GroupDao();
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			for(SubjectValue sV : subjectValuesList){
				if(sV.getId() == null){
					if(sV.getGroup() != null){
						if(sV.getGroup().getId() == null){
							gDao.saveGroup(sV.getGroup());
						}
					}
					if(sV.getComplementaryValues() != null){
						if(sV.getComplementaryValues().getId() == null){
							cVDao.saveComplementaryValues(sV.getComplementaryValues());
						}
					}
					sVDao.saveSubjectValue(sV);
				}
			}
			ofy().save().entity(s).now();
		}
	}
	
	public Long generateId(){
		
		 ObjectifyFactory f = new ObjectifyFactory();
		 Key<Semester> key = f.allocateId(Semester.class);
		 return key.getId();
		
	}

	public void deleteSemester(Long id) {
		Key<Semester> key = Key.create(Semester.class, id);
		ofy().delete().key(key).now();
	}

	public void deleteAllSemesters() {
		List<Semester> list = getAllSemesters();
		for(Semester s : list){
			deleteSemester(s.getId());
		}
	}

	private List<Semester> getAllSemesters() {
		return ofy().load().type(Semester.class).list(); 
	}

	
}
