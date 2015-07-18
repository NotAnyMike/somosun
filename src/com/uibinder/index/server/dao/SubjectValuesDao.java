package com.uibinder.index.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Group;
import com.uibinder.index.shared.control.SubjectValues;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValuesDao {
	
	static{
		ObjectifyService.register(SubjectValues.class);
	}

	public void saveSubjectValue(SubjectValues sV){
		
		if(sV != null){

			if(sV.getGrade() > 5 || sV.getGrade() < 0){
				sV.setTaken(false);
			}
			
			ofy().save().entity(sV).now();
			
		}
	}
	
	/**
	 * TODO: correct the filter, use an embedded filter
	 * 
	 * @param g
	 * @return
	 */
	public SubjectValues getSubjectValuesByGroup(Group g){
		return (SubjectValues) ofy().load().type(SubjectValues.class).filter("group", g).first().now();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SubjectValues> key = f.allocateId(SubjectValues.class);
		return key.getId();
	}

	public void deleteSubjectValues(Long id) {
		Key<SubjectValues> key = Key.create(SubjectValues.class, id);
		ofy().delete().key(key).now();
	}
}
