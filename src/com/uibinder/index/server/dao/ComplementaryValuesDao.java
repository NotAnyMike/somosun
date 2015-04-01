package com.uibinder.index.server.dao;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Subject;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ComplementaryValuesDao {

	static{
		ObjectifyService.register(ComplementaryValues.class);
	}
	
	public void saveComplementaryValues(ComplementaryValues cV){
		ofy().save().entity(cV).now();
	}
	
	public ComplementaryValues getComplementaryValues(Career career, Subject subject){
		if(subject == null || career == null){
			return null;
		}else{			
			return (ComplementaryValues) ofy().load().type(ComplementaryValues.class).filter("subject.code", subject.getCode()).filter("career.code", career.getCode()).first().now();
		}
	}
	
}
