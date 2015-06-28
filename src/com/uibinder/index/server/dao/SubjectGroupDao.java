package com.uibinder.index.server.dao;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.SubjectGroup;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectGroupDao {

	static{
		ObjectifyService.register(SubjectGroup.class);
	}
	
	public void saveSubjectGroup(SubjectGroup sG){
		if(sG != null)	ofy().save().entity(sG).now();
	}
	
	public SubjectGroup getSubjectGroup(String name, String careerCode){
		return (SubjectGroup) ofy().load().type(SubjectGroup.class).filter("name", name).filter("career.code", careerCode).first().now();
	}
	
}
