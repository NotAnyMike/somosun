package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectDao {
	
	static {
		ObjectifyService.register(Subject.class);
	}
	
	/**
	 * It search in the Db compare the results from sia, if the sia is on, it
	 * updates the subjects if it is needed and then returns them,
	 * if the sia is off then it return the db information.
	 * 
	 * if @param name is empty, then it will return null
	 * 
	 * @param name
	 * @return
	 */
	public List<Subject> getSubjectsByName(String name){
		return null;
	};
	
	public List<Subject> getSubjectsByName(String name, String career){
		//return ofy().load().type(Subject.class).filter("name", name).filter("career", career);
		return null;
	}
	
	public Subject getSubjectByCode(String code){
		return (Subject) ofy().load().type(Subject.class).filter("code", code).first().now();
	}
	
	public Subject getSubjectbySubject(Subject subject){
		Subject subjectToReturn = null;
		if(subject != null){
			subjectToReturn = getSubjectByCode(subject.getCode());
			if(subjectToReturn==null){
				subjectToReturn = subject;
				addSubject(subject);
			};
		}
		return subjectToReturn;
	}
	
	public List<Subject> getSubjectsByCareer(){
		return null;
	}
	
	private void addSubject(Subject subject){
		if(ofy().load().type(Subject.class).filter("code", subject.getCode()).first().now()==null){
			ofy().save().entity(subject).now();
		}
	}

}
