package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SubjectValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValueDao {
	
	static{
		ObjectifyService.register(SubjectValue.class);
	}

	public void saveSubjectValue(SubjectValue sV){
		
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
	public SubjectValue getSubjectValuesByGroup(Group g){
		return (SubjectValue) ofy().load().type(SubjectValue.class).filter("group", g).first().now();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SubjectValue> key = f.allocateId(SubjectValue.class);
		return key.getId();
	}

	public void deleteSubjectValue(Long id) {
		Key<SubjectValue> key = Key.create(SubjectValue.class, id);
		ofy().delete().key(key).now();
	}

	public void deleteAllSubjectValues() {
		List<SubjectValue> list = getAllSubjectValues();
		for(SubjectValue sV : list){
			deleteSubjectValue(sV.getId());
		}
	}

	private List<SubjectValue> getAllSubjectValues() {
		return ofy().load().type(SubjectValue.class).list();
	}
}
