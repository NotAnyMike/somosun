package com.uibinder.index.server.dao;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Group;
import com.uibinder.index.shared.control.SubjectValues;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValuesDao {
	
	static{
		ObjectifyService.register(SubjectValues.class);
	}

	public void saveSubjectValue(SubjectValues sV){
		ofy().save().entity(sV).now();
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
}
