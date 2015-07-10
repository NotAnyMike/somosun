package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.uibinder.index.shared.control.Career;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class CareerDao {
	
	static{
		ObjectifyService.register(Career.class);
	}
	
	/**
	 * Only when some admin is going to update the careers the second if must be changed to true,
	 * otherwise it is not going to UPDATE the db.
	 * 
	 * @param career
	 */
	public void updateCareer(Career career){
		Career careerFromDB = getCareerByCode(career.getCode());
		if(careerFromDB == null){
			saveCareer(career);
		}else{
			updateCareerTransaction(careerFromDB, career);
		}
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
	 * This method must be used only by this class, it could create some
	 * big security problems if some other class uses it
	 * 
	 * @param career
	 */
	private void saveCareer(Career career){
		if(career != null){
			ofy().save().entity(career).now();
		}
	}
	
	public Career getCareerById(String Id){
		Key<Career> key = Key.create(Career.class, Id);
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

	private void updateCareerTransaction(final Career oldCareer, final Career newCareer) {
		ofy().transact(new VoidWork() {
			public void vrun(){
				deleteCareer(oldCareer);
				saveCareer(newCareer);
			}
		});
	}

}
