package com.uibinder.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.uibinder.shared.control.Career;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class CareerDao {
	
	static{
		ObjectifyService.register(Career.class);
	}
	
	private void deleteCareer(Career career) {
		if(career != null)
		{			
			Key<Career> key = Key.create(Career.class, career.getId());
			ofy().delete().key(key).now();
		}
	}

	public List<Career> getCareersBySede(String sede){
		return ofy().load().type(Career.class).order("name").filter("sede", sede).list();
	}
	
	/**
	 * Will save the career, if the career has Id then this method will update it
	 * 
	 * @param career
	 */
	public void saveCareer(Career career){
		if(career != null){
			ofy().save().entity(career).now();
		}
	}
	
	public Career getCareerById(String id){
		Key<Career> key = Key.create(Career.class, id);
		return (Career) ofy().load().key(key).now();
	}
	
	public Career getCareerByCode(String code){
		return ofy().load().type(Career.class).filter("code", code).first().now();
	}

	public String fixName(String name, String code) {
		String s = name;
		if(s.contains(code))
		{
			s = s.replace(code, "").toLowerCase().replace("(", "").replace(")", "");
			while(s.indexOf(" ") == 0)
			{
				s = s.replaceFirst(" ", "");
			}
			
		}
		
		return s;
	}

}
