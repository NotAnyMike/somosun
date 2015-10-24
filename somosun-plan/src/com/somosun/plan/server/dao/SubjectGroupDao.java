package com.somosun.plan.server.dao;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.values.SubjectGroupCodes;
import com.somosun.plan.shared.values.TypologyCodes;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectGroupDao implements Dao<SubjectGroup> {

	private static final Logger log = Logger.getLogger("SubjectGroupDao");
	
	static{
		ObjectifyService.register(SubjectGroup.class);
	}
	
	public Long save(SubjectGroup sG){
		Long toReturn = null;
		if(sG != null)
		{
			// OLD sG.setName(SomosUNUtils.standardizeString(sG.getName(), false));
			sG.setName(SomosUNUtils.removeAccents(sG.getName()));
			ofy().save().entity(sG).now();
			toReturn = sG.getId();
		}
		return toReturn;
	}
	
	public SubjectGroup get(String name, String careerCode){
		return (SubjectGroup) ofy().load().type(SubjectGroup.class).filter("name", name).filter("career.code", careerCode).first().now();
	}
	
	public SubjectGroup get(String name, boolean isFundamental, String careerCode){
		return (SubjectGroup) ofy().load().type(SubjectGroup.class).filter("name", name).filter("career.code", careerCode).filter("fundamental", isFundamental).first().now();
	}
	
	public List<SubjectGroup> getList(String careerCode){
		List<SubjectGroup> toReturn = ofy().load().type(SubjectGroup.class).filter("career.code", careerCode).list();
		return toReturn;
	}
	
	public boolean delete(String name, boolean isFundamental, String careerCode){
		boolean toReturn = false;
		SubjectGroup toDelete = get(name, isFundamental, careerCode);
		if(toDelete != null)
		{
			toReturn = delete(toDelete.getId());
		}
		return toReturn;
	}
	
	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<SubjectGroup> key = Key.create(SubjectGroup.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
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

	public SubjectGroup getById(String id) {
		SubjectGroup sG = null;
		if(id!= null){			
			Key<SubjectGroup> key = Key.create(SubjectGroup.class, id);
			sG = ofy().load().key(key).now();
		}
		return sG;
	}
	
	public SubjectGroup getById(Long id){
		SubjectGroup sG = null;
		if(id!= null){			
			Key<SubjectGroup> key = Key.create(SubjectGroup.class, id);
			sG = ofy().load().key(key).now();
		}
		return sG;
	}

	public SubjectGroup getUnkownSubjectGroup(String careerCode, boolean isFundamental) {
		SubjectGroup sG = null;
		sG = this.get(SubjectGroupCodes.UNKNOWN_NAME, isFundamental, careerCode);
		if(sG == null){
			CareerDao cDao = new CareerDao();
			Career career = cDao.getByCode(careerCode);
			sG = new SubjectGroup(SubjectGroupCodes.UNKNOWN_NAME, career, isFundamental, 0, 0, true);
			sG.setId(generateId());
			save(sG);
		}
		return sG;
	}

	/**
	 * This method will return a subjectGroup based on the typology, in case of @param typology fundamental, professional, nivelación or free election base on the values on TypologyCodes 
	 * it will return a subjectGroup unknown with isFudamental appropriate in any case
	 * <br/>
	 * If @param typology is TypologyCodes.LIBRE_ELECCION or NIVELACION and the subject group does not exist it will create one, if the @param typology one of the others values will return an SubjectGroupCodes.UNKNOWN_NAME 
	 * @param career
	 * @param typology
	 * @return
	 */
	public SubjectGroup getSubjectGroupFromTypology(Career career, String typology){
		
		SubjectGroup subjectGroup = null;
		String careerCode = career.getCode();
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		if(typology == null || typology.equals(TypologyCodes.LIBRE_ELECCION) == true){
			subjectGroup = subjectGroupDao.get(SubjectGroupCodes.LIBRE_NAME, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.LIBRE_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.save(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.NIVELACION) == true){
			subjectGroup = subjectGroupDao.get(SubjectGroupCodes.NIVELACION_NAME, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.NIVELACION_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.save(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.FUNDAMENTACION) == true){
			subjectGroup = subjectGroupDao.get(SubjectGroupCodes.UNKNOWN_NAME, true, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.UNKNOWN_NAME, career, true, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.save(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.PROFESIONAL) == true){
			subjectGroup = subjectGroupDao.get(SubjectGroupCodes.UNKNOWN_NAME, false, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.UNKNOWN_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.save(subjectGroup);
			}
		}
		
		return subjectGroup;
	}

	public void deleteAllSubjectGroups() {
		final List<SubjectGroup> list = getAllSubjectGroups();
		for(SubjectGroup sG : list){
			delete(sG.getId());
		}
		log.warning("All subjectGroup deleted");
	}

	private List<SubjectGroup> getAllSubjectGroups() {
		return ofy().load().type(SubjectGroup.class).list();
	}
}
