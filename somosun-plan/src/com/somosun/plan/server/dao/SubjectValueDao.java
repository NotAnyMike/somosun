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
			
			GroupDao groupDao = new GroupDao();
			if(sV.getGroup() == null){
				sV.setGroup(groupDao.getOrCreateGroup(sV.getComplementaryValue().getSubject(), null, null));
			}
			
			if(sV.getGroup().getId() == null){
				sV.getGroup().setId(groupDao.save(sV.getGroup()));
			}

			if(sV.getGrade() > 5 || sV.getGrade() < 0){
				sV.setTaken(false);
			}
			
			if(sV.getComplementaryValue() != null && sV.getComplementaryValue().getId() == null){
				ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
				sV.getComplementaryValue().setId(complementaryValueDao.save(sV.getComplementaryValue()));
			}
			
			if(sV.getId() == null) sV.setId(generateId());
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
