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

public class SubjectGroupDao extends Dao {

	private static final Logger log = Logger.getLogger("SubjectGroupDao");
	
	static{
		ObjectifyService.register(SubjectGroup.class);
	}
	
	public void saveSubjectGroup(SubjectGroup sG){
		if(sG != null)
		{
			// OLD sG.setName(SomosUNUtils.standardizeString(sG.getName(), false));
			sG.setName(SomosUNUtils.removeAccents(sG.getName()));
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
			deleteSubjectGroup(toDelete.getId());
		}
	}
	
	private void deleteSubjectGroup(Long id) {
		Key<SubjectGroup> key = Key.create(SubjectGroup.class, id);
		ofy().delete().key(key).now();
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

	public SubjectGroup getSubjectGroupById(String id) {
		SubjectGroup sG = null;
		Key<SubjectGroup> key = Key.create(SubjectGroup.class, id);
		sG = ofy().load().key(key).now();
		return sG;
		
	}

	public SubjectGroup getUnkownSubjectGroup(String careerCode, boolean isFundamental) {
		SubjectGroup sG = null;
		sG = this.getSubjectGroup(SubjectGroupCodes.UKNOWN_NAME, isFundamental, careerCode);
		if(sG == null){
			CareerDao cDao = new CareerDao();
			Career career = cDao.getCareerByCode(careerCode);
			sG = new SubjectGroup(SubjectGroupCodes.UKNOWN_NAME, career, isFundamental, 0, 0, true);
			sG.setId(generateId());
			saveSubjectGroup(sG);
		}
		return sG;
	}

	/**
	 * This method will return a subjectGroup based on the typology, in case of @param typology fundamental or profesional 
	 * it will return a subjectGroup unknown with isFudamental appropriete in any case
	 * @param career
	 * @param typology
	 * @return
	 */
	public SubjectGroup getSubjectGroupFromTypology(Career career, String typology){
		
		SubjectGroup subjectGroup = null;
		String careerCode = career.getCode();
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		if(typology == null || typology.equals(TypologyCodes.LIBRE_ELECCION) == true){
			subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.LIBRE_NAME, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.LIBRE_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.saveSubjectGroup(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.NIVELACION) == true){
			subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.NIVELACION_NAME, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.NIVELACION_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.saveSubjectGroup(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.FUNDAMENTACION) == true){
			subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.UKNOWN_NAME, true, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.UKNOWN_NAME, career, true, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.saveSubjectGroup(subjectGroup);
			}
		}else if(typology.equals(TypologyCodes.PROFESIONAL) == true){
			subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.UKNOWN_NAME, false, careerCode);
			if(subjectGroup == null){
				subjectGroup = new SubjectGroup(SubjectGroupCodes.UKNOWN_NAME, career, false, 0, 0, true);
				subjectGroup.setId(subjectGroupDao.generateId());
				subjectGroupDao.saveSubjectGroup(subjectGroup);
			}
		}
		
		return subjectGroup;
	}

	public void deleteAllSubjectGroups() {
		final List<SubjectGroup> list = getAllSubjectGroups();
		for(SubjectGroup sG : list){
			deleteSubjectGroup(sG.getId());
		}
		log.warning("All subjectGroup deleted");
	}

	private List<SubjectGroup> getAllSubjectGroups() {
		return ofy().load().type(SubjectGroup.class).list();
	}
}