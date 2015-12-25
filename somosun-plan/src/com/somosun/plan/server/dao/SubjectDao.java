package com.somosun.plan.server.dao;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.somosun.plan.server.control.ScoreServer;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Subject;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectDao implements Dao<Subject> {
	
	private static final Logger log = Logger.getLogger("SubjectDao");
	
	static {
		ObjectifyService.register(Subject.class);
	}
	
	public Subject getById(Long id){
		Subject toReturn = null;
		if(id != null){
			Key<Subject> key = Key.create(Subject.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}
	
	public Subject getByName(String name){
		return (Subject) ofy().load().type(Subject.class).filter("name" , name).first().now();
	}
	
	/**
	 * It search in the Db compare the results from sia, if the sia is on, it
	 * updates the subjects if it is needed and then returns them,
	 * if the sia is off then it return the db information.
	 * 
	 * <br></br>
	 * 
	 * NOTE: this will only return the subjects which have the same name as @param name,
	 * it will not return the subjects which contains @param name. 
	 * 
	 * if @param name is empty, then it will return null
	 * 
	 * @param name
	 * @return
	 */
	public List<Subject> getSubjectsByName(String name){
		return ofy().load().type(Subject.class).filter("name", name).list();
	};
	
	 /* subject has no career field
	public List<Subject> getSubjectsByName(String name, String career){
		//return ofy().load().type(Subject.class).filter("name", name).filter("career", career);
		return null;
	}*/
	
	/**
	 * Sia Code makes reference to the special code that the sia has for the subject
	 * @param siaCode
	 * @return
	 */
	public Subject getBySiaCode(String siaCode) {
		return (Subject) ofy().load().type(Subject.class).filter("siaCode", siaCode).first().now();
	}
	
	public Subject getByCode(String code){
		Subject subjectToReturn = null;
		if(code != null && code.isEmpty() == false){			
			subjectToReturn = (Subject) ofy().load().type(Subject.class).filter("code", code).first().now();
			/**this was causing a infinite loop, a class that is not saved is searched, then the method
			 * getSubjectFromSubject is called, that method calls getSubjectByCode and the last one calls
			 * the sia, the sia recognizes the subject and calls getSubjectBySubject ... etc. 
			 */
			/*if(subjectToReturn == null){
			SiaResultSubjects siaResult = SiaProxy.getSubjects(code, "", "", "", 1, 2, "bog");
			if(siaResult.getSubjectList().isEmpty() == false){
				subjectToReturn = siaResult.getSubjectList().get(0);
				saveSubject(subjectToReturn);
			}
		}*/
		}
		return subjectToReturn;
	}
	
	/**
	 * This method can update the subject if the isSiaProxy is set to true, but it must done only if the request come from the siaProxy class
	 * 
	 * @param subject
	 * @param isSiaProxy: gives rights to update (delete and add) a subject
	 * @return
	 */
	public Subject getBySubject(Subject subject, boolean isSiaProxy){
		Subject subjectToReturn = null;
		if(subject != null){
			subjectToReturn = getByCode(subject.getCode());
			if(subjectToReturn==null){
				if(subject.getId() == null){
					subject.setId(generateId());
				}
				save(subject);
				subjectToReturn = subject;
			} else {
				if(isSiaProxy && subjectToReturn.equals(subject)==false){ //takes care of the update just if the info is coming from the siaProxy class
					subjectToReturn.setCode(subject.getCode());
					subjectToReturn.setCredits(subject.getCredits());
					subjectToReturn.setLocation(subject.getLocation());
					subjectToReturn.setName(subject.getName());
					subjectToReturn.setSiaCode(subject.getSiaCode());
					save(subjectToReturn);
				}
			}
		}
		return subjectToReturn;
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id != null){			
			Key<Subject> key = Key.create(Subject.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
	/**
	 * This will save the @param subject in the db, if it has no code, then subject.setSpecial(true)
	 * 
	 * <br/>
	 * 
	 * @param subject
	 */
	public Long save(Subject subject){
		if(subject != null){
			
			//Checking for any entity repetition
			if(subject.getCode() != null){				
				Subject subjectRepeted = getByCode(subject.getCode());
				if(subjectRepeted != null) {
					delete(subject.getId());
					subject.setId(subjectRepeted.getId());
				}
			}
			
			//Adding the averageGrade if it is empty
			if(subject.getAverageGrade() == null && subject.getId() != null){
				ScoreDao scoreDao = new ScoreDao();
				ScoreServer score = scoreDao.getBySubjectId(subject.getId());
				if(score != null){
					subject.setAverageGrade(score.getTotalAverage());
				}
			}
			
			//Addding the averageGrade if it is empty (this time search for code and not by id)
			if(subject.getAverageGrade() == null && subject.getCode() != null){
				ScoreDao scoreDao = new ScoreDao();
				ScoreServer score = scoreDao.getByCode(subject.getCode());
				if(score != null){
					subject.setAverageGrade(score.getTotalAverage());
				}
			}
			
			if(subject.getCode().isEmpty()){
				subject.setSpecial(true);
			}
			if(subject.getId() == null){
				subject.setId(generateId());
			}
			//OLD subject.setName(SomosUNUtils.standardizeString(subject.getName(), false));
			subject.setName(SomosUNUtils.removeAccents(subject.getName()));
			ofy().save().entity(subject).now();
		}
		
		return subject.getId();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Subject> key = f.allocateId(Subject.class);
		return key.getId();
	}

	public Subject getDummySubjectByCode(String code) {
		return ofy().load().type(Subject.class).filter("isDummy", true).filter("code", code).first().now();
	}

	public void deleteAllSubjects() {
		final List<Subject> list = getAllSubjects();
		for(Subject s : list){
			delete(s.getId());
		}
		log.warning("All subjects deleted");
	}

	private List<Subject> getAllSubjects() {
		return ofy().load().type(Subject.class).list();
	}

}
