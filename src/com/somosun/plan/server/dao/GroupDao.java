package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GroupDao {

	static{
		ObjectifyService.register(Group.class);
	}
	
	public void saveGroup(Group g){
		if(g != null) ofy().save().entity(g).now();
	}

	public List<Group> getGroups(Subject subject) {
		//return ofy().load().type(Group.class).filter("subject", subject).list();
		if(subject.getId() != null)	return ofy().load().type(Group.class).filter("subject.code", subject.getCode()).list();
		else return null;
	}
	
	public Group getGroup(Group g){
		return getGroup(g.getSubject(), g.getSemesterValue(), g.getGroupNumber());
	}

	public Group getGroup(Subject subject, SemesterValue semesterValue, int groupNumber){
		return ofy().load().type(Group.class).filter("groupNumber", groupNumber).filter("subject.code", subject.getCode()).filter("semesterValue.year", semesterValue.getYear()).first().now();
	}
	
	/**
	 * 
	 * 
	 * @param group
	 * @param b
	 * @return
	 */
	public Group getGroupByGroup(Group group, boolean isSiaProxy) {
		Group groupToReturn = getGroup(group);
		if(groupToReturn == null){
			groupToReturn = group;
			saveGroup(groupToReturn);
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
		deleteGroup(group);
		saveGroup(group);
	}

	private void deleteGroup(Group group) {
		if(group.getId()!=null){
			Key<Group> key = Key.create(Group.class, group.getId());
			ofy().delete().key(key).now();
		}
		
	}
	
	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Group> key = f.allocateId(Group.class);
		return key.getId();
	}

	public Group getOrCreateGroup(Subject subject, SemesterValue semesterValue, Integer groupInt) {
		Group group = null;
		
		group = getGroup(subject, semesterValue, groupInt);
		
		if(group == null){
			group = new Group(subject, semesterValue, groupInt);
			group.setId(generateId());
		}
		
		
		return group;
	}

	
}
