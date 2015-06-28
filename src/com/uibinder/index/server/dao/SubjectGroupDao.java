package com.uibinder.index.server.dao;

import java.util.List;

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
	
	public SubjectGroup getSubjectGroup(String name, boolean isFundamental, String careerCode){
		return (SubjectGroup) ofy().load().type(SubjectGroup.class).filter("name", name).filter("career.code", careerCode).filter("fundamental", isFundamental).first().now();
	}
	
	public List<SubjectGroup> getSubjectGroups(String careerCode){
		List<SubjectGroup> toReturn = ofy().load().type(SubjectGroup.class).filter("career.code", careerCode).list();
		return toReturn;
	}
	
	public void deleteSubjectGroup(String name, boolean isFundamental, String careerCode){
		SubjectGroup toDelete = getSubjectGroup(name, isFundamental, careerCode);
		if(toDelete != null)
		{
			ofy().delete().entity(toDelete).now();
		}
	}
	
}
