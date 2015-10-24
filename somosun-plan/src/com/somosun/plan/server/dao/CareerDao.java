package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Plan;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class CareerDao implements Dao<Career>{
	
	static{
		ObjectifyService.register(Career.class);
	}
	
	public boolean delete(Career career) {
		return delete(career.getId());
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id != null)
		{			
			Key<Career> key = Key.create(Career.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public List<Career> getBySede(String sede){
		return ofy().load().type(Career.class).order("name").filter("sede", sede).list();
	}
	
	/**
	 * Will save the career, if the career has Id then this method will update it
	 * 
	 * @param career
	 */
	public Long save(Career career){
		Long toReturn = null;
		if(career != null){
			if(career.getId() == null) career.setId(generateId());
			ofy().save().entity(career).now();
			toReturn = career.getId();
		}
		return toReturn;
	}
	
	public Career getById(String id){
		Key<Career> key = Key.create(Career.class, id);
		return (Career) ofy().load().key(key).now();
	}
	
	public Career getById(Long id){
		Key<Career> key = Key.create(Career.class, id);
		return (Career) ofy().load().key(key).now();
	}
	
	public Career getByCode(String code){
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

	public void resetAllHasAnalysis() {
		List<Career> careers = getBySede("bog");
		for(Career career : careers){
			career.setHasAnalysis(false);
			save(career);
		}
	}

	public void resetCertainHasAnalysis(String careerCode) {
		Career career = getByCode(careerCode);
		career.setHasAnalysis(false);
		save(career);
	}

	public void resetAllHasDefault() {
		List<Career> careers = getBySede("bog");
		for(Career career : careers){
			career.setHasDefault(false);
			save(career);
		}
	}

	public void resetHasDefaultForCareer(String careerCode) {
		Career career = getByCode(careerCode);
		career.setHasDefault(false);
		save(career);
	}

	@Override
	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Career> key = f.allocateId(Career.class);
		return key.getId();
	}

}
