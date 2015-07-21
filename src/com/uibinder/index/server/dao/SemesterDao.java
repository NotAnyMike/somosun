package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Semester;
import com.uibinder.index.shared.control.SubjectValues;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SemesterDao {
	
	static{
		ObjectifyService.register(Semester.class);
	}
	
	public void saveSemester(Semester s){
		if(s != null) {
			List<SubjectValues> subjectValuesList = s.getSubjects();
			SubjectValuesDao sVDao = new SubjectValuesDao();
			GroupDao gDao = new GroupDao();
			ComplementaryValuesDao cVDao = new ComplementaryValuesDao();
			for(SubjectValues sV : subjectValuesList){
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

	
}
