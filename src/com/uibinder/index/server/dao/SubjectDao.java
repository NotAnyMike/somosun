package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Subject;

public class SubjectDao {
	
	static {
		ObjectifyService.register(Subject.class);
	}
	
	public List<Subject> getSubjectsByName(){
		return null;
	};
	
	public Subject getSubjectByCode(){
		return null;
	}
	
	public List<Subject> getSubjectsByCareer(){
		return null;
	}

}
