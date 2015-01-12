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
	
	public Subject getSubjectBySiaCode(String siaCode) {
		return (Subject) ofy().load().type(Subject.class).filter("siaCode", siaCode).first().now();
	}
	
	public Subject getSubjectByCode(String code){
		return (Subject) ofy().load().type(Subject.class).filter("code", code).first().now();
	}
	
	/**
	 * This method can update the subject if the isSiaProxy is set to true, but it must done only if the request come from the siaProxy class
	 * 
	 * @param subject
	 * @param isSiaProxy: gives rights to update (delete and add) a subject
	 * @return
	 */
	public Subject getSubjectbySubject(Subject subject, boolean isSiaProxy){
		Subject subjectToReturn = null;
		if(subject != null){
			subjectToReturn = getSubjectByCode(subject.getCode());
			if(subjectToReturn==null){
				subjectToReturn = subject;
				addSubject(subject);
			} else {
				if(isSiaProxy && subjectToReturn.equals(subject)==false){ //takes care of the update just if the info is coming from the siaProxy class
					subjectToReturn.setCode(subject.getCode());
					subjectToReturn.setCredits(subject.getCredits());
					subjectToReturn.setLocation(subject.getLocation());
					subjectToReturn.setName(subject.getName());
					subjectToReturn.setSiaCode(subject.getSiaCode());
					updateSubject(subjectToReturn);
				}
			}
		}
		return subjectToReturn;
	}
	
	public List<Subject> getSubjectsByCareer(String career){
		return null;
	}
	
	private void deleteSubject(Long id){
		Key<Subject> key = Key.create(Subject.class, id);
		ofy().delete().key(key).now();
	}
	
	private void updateSubject(Subject s){
		deleteSubject(s.getId());
		addSubject(s);		
	}
	
	private void addSubject(Subject subject){
		if(ofy().load().type(Subject.class).filter("code", subject.getCode()).first().now()==null){
			ofy().save().entity(subject).now();
		}
	}

}
