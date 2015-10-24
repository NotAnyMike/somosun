package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SubjectValue;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValueDao implements Dao<SubjectValue> {
	
	static{
		ObjectifyService.register(SubjectValue.class);
	}
	
	public SubjectValue getById(Long id){
		SubjectValue toReturn = null;
		if(id != null){
			Key<SubjectValue> key = Key.create(SubjectValue.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}

	public Long save(SubjectValue sV){
		Long toReturn = null;
		if(sV != null){

			if(sV.getGrade() > 5 || sV.getGrade() < 0){
				sV.setTaken(false);
			}
			
			ofy().save().entity(sV).now();
			toReturn = sV.getId();
			
		}
		return toReturn;
	}
	
	/**
	 * TODO: correct the filter, use an embedded filter
	 * 
	 * @param g
	 * @return
	 */
	public SubjectValue getByGroup(Group g){
		return (SubjectValue) ofy().load().type(SubjectValue.class).filter("group", g).first().now();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SubjectValue> key = f.allocateId(SubjectValue.class);
		return key.getId();
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){
			Key<SubjectValue> key = Key.create(SubjectValue.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllSubjectValues() {
		List<SubjectValue> list = getAllSubjectValues();
		for(SubjectValue sV : list){
			delete(sV.getId());
		}
	}

	private List<SubjectValue> getAllSubjectValues() {
		return ofy().load().type(SubjectValue.class).list();
	}
}
