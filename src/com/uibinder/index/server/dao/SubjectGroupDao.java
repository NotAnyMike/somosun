package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.SubjectGroup;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectGroupDao extends Dao {

	static{
		ObjectifyService.register(SubjectGroup.class);
	}
	
	public void saveSubjectGroup(SubjectGroup sG){
		if(sG != null)
		{
			sG.setName(standardizeString(sG.getName()));
			ofy().save().entity(sG).now();
		}
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
	
	/**
	 * Will create a unique key if the entity X has an embedded entity Y which has an empty id, this will not allow to save the X entity,
	 * then use this method in order to set the Y's id.
	 * 
	 * @return
	 */
	public Long generateId() {
		
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SubjectGroup> key = f.allocateId(SubjectGroup.class);
		
		return key.getId();
		
	}
	
}
