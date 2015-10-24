package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GroupDao implements Dao<Group> {

	static{
		ObjectifyService.register(Group.class);
	}
	
	public Long save(Group g){
		Long toReturn = null; 
		if(g != null) {
			ofy().save().entity(g).now();
			toReturn = g.getId();
		}
		return toReturn;
	}
	
	public Group getById(Long id){
		Group toReturn = null;
		if(id != null){
			Key<Group> key = Key.create(Group.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}

	public List<Group> getGroups(Subject subject) {
		//return ofy().load().type(Group.class).filter("subject", subject).list();
		if(subject.getId() != null)	return ofy().load().type(Group.class).filter("subject.code", subject.getCode()).list();
		else return null;
	}
	
	public Group get(Group g){
		return get(g.getSubject(), g.getSemesterValue(), g.getGroupNumber());
	}

	public Group get(Subject subject, SemesterValue semesterValue, int groupNumber){
		return ofy().load().type(Group.class).filter("groupNumber", groupNumber).filter("subject.code", subject.getCode()).filter("semesterValue.year", semesterValue.getYear()).first().now();
	}
	
	/**
	 * 
	 * 
	 * @param group
	 * @param b
	 * @return
	 */
	public Group getByGroup(Group group, boolean isSiaProxy) {
		Group groupToReturn = get(group);
		if(groupToReturn == null){
			groupToReturn = group;
			save(groupToReturn);
		}else{
			if(groupToReturn.equals(group) == false && isSiaProxy == true){
				groupToReturn.setFreePlaces(group.getFreePlaces());
				groupToReturn.setTotalPlaces(group.getTotalPlaces());
				groupToReturn.setSchedule(group.getSchedule());
				groupToReturn.setTeacher(group.getTeacher());
				updateGroup(groupToReturn);
			}
		}
		return groupToReturn;
	}

	private void updateGroup(Group group) {
		delete(group);
		save(group);
	}

	private boolean delete(Group group) {
		boolean toReturn = false;
		if(group != null) toReturn = delete(group.getId());
		return toReturn;
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id!=null){
			Key<Group> key = Key.create(Group.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Group> key = f.allocateId(Group.class);
		return key.getId();
	}

	public Group getOrCreateGroup(Subject subject, SemesterValue semesterValue, Integer groupInt) {
		Group group = null;
		
		group = get(subject, semesterValue, groupInt);
		
		if(group == null){
			group = new Group(subject, semesterValue, groupInt);
			group.setId(generateId());
		}
		
		
		return group;
	}

	public void deleteAll() {
		List<Group> list = ofy().load().type(Group.class).list();
		for(Group g : list){
			delete(g);
		}
	}
	
}
