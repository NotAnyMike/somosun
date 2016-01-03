package com.somosun.plan.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.SubjectGroupServer;
import com.somosun.plan.server.serviceImpl.LoginServiceImpl;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.controlAbstract.SubjectGroupAbstract;
import com.somosun.plan.shared.values.SubjectGroupCodes;
import com.somosun.plan.shared.values.TypologyCodes;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectGroupDao implements Dao<SubjectGroup> {

	private static final Logger log = Logger.getLogger("SubjectGroupDao");
	
	static{
		ObjectifyService.register(SubjectGroupServer.class);
		ObjectifyService.register(Career.class);
	}
	
	public Long save(SubjectGroupAbstract sG){
		Long id = null;
		
		/*
		 * 1. Check if the plan has id
		 * 2. Check if it is the same as the one in the db (be carefull with the case where it is a new plan, therefore originPlan = null [null pointer exception])
		 * 2.1 Compare the values and references if they are the same) if false save it
		 * 2.2 Take the plan's references (not planOrigina !IMportant) from the db and re do this thing (i.e. compare values and reference, then check the references values and its references and so on)
		 */
		if(sG != null){
			
			if(sG.getId() == null) sG.setId(generateId());

			/******** check for the references' values (i.e. only for Ref<Semester> ********/
			//CareerMust not be changed or updated from this dao, only info comming from the sia
			/*******************************************************************************/
			
			/******* save the entity's plan *******/
			SubjectGroupServer original = getServerById(sG.getId());
			if(original == null || original.compare(sG) == false){
				
				if(original == null) original = new SubjectGroupServer();
				
				original.setId(sG.getId());
				original.setError(sG.getError());
				original.setFundamental(sG.isFundamental());
				original.setName(sG.getName());
				original.setObligatoryCredits(sG.getObligatoryCredits());
				original.setOptativeCredits(sG.getOptativeCredits());
				original.setCareer(sG.getCareer());
				
				//save original
				ofy().defer().save().entity(original);
				
			}
			id = original.getId();
			/**************************************/
			
		}
		
		return id;
	}
	
//	public Long save(SubjectGroup sG){
//		Long toReturn = null;
//		if(sG != null)
//		{
//			// OLD sG.setName(SomosUNUtils.standardizeString(sG.getName(), false));
//			sG.setName(SomosUNUtils.removeAccents(sG.getName()));
//			ofy().save().entity(sG).now();
//			toReturn = sG.getId();
//		}
//		return toReturn;
//	}
	
	public SubjectGroup get(String name, String careerCode){
		CareerDao cDao = new CareerDao();
		Career c = cDao.getByCode(careerCode);
		Ref<Career> ref = null;
		if(c != null) ref = Ref.create(c);
		SubjectGroupServer sGS =(SubjectGroupServer) ofy().load().type(SubjectGroupServer.class).filter("name", name).filter("career", ref).first().now();
		SubjectGroup toReturn = null;
		if(sGS != null) toReturn = sGS.getClientInstance();
		return toReturn;
	}
	
	public SubjectGroup get(String name, boolean isFundamental, String careerCode){
		CareerDao cDao = new CareerDao();
		Career c = cDao.getByCode(careerCode);
		Ref<Career> ref = null;
		if(c != null) ref = Ref.create(c);
		SubjectGroupServer sGS = (SubjectGroupServer) ofy().load().type(SubjectGroupServer.class).filter("name", name).filter("career", ref).filter("fundamental", isFundamental).first().now(); 
		SubjectGroup toReturn = null;
		if(sGS != null) toReturn = sGS.getClientInstance();
		return toReturn;
	}
	
	public List<SubjectGroup> getList(String careerCode){
		CareerDao cDao = new CareerDao();
		Career c = cDao.getByCode(careerCode);
		Ref<Career> ref = null;
		if(c != null) ref = Ref.create(c);
		List<SubjectGroupServer> list = ofy().load().type(SubjectGroupServer.class).filter("career", ref).list();
		List<SubjectGroup> toReturn = toClientList(list);
		
		return toReturn;
	}
	
	private List<SubjectGroup> toClientList(List<SubjectGroupServer> list){
		List<SubjectGroup> toReturn = null;
		if(list != null){
			for(SubjectGroupServer sGS : list){
				if(toReturn == null) toReturn = new ArrayList<SubjectGroup>();
				toReturn.add(sGS.getClientInstance());
			}
		}
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
			Key<SubjectGroupServer> key = Key.create(SubjectGroupServer.class, id);
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
		Key<SubjectGroupServer> key = f.allocateId(SubjectGroupServer.class);
		
		return key.getId();
		
	}

	private SubjectGroupServer getServerById(Long id) {
		SubjectGroupServer sG = null;
		if(id!= null){			
			Key<SubjectGroupServer> key = Key.create(SubjectGroupServer.class, id);
			sG = ofy().load().key(key).now();
		}
		return sG;
	}
	
	public SubjectGroup getById(String id){
		SubjectGroupServer sGS = getServerById(Long.getLong(id));
		SubjectGroup toReturn = null;
		if(sGS != null) toReturn = sGS.getClientInstance();
		return toReturn;
	}
	
	public SubjectGroup getById(Long id){
		SubjectGroupServer sG = null;
		SubjectGroupServer sGS = getServerById(id);
		SubjectGroup toReturn = null;
		if(sGS != null) toReturn = sGS.getClientInstance();
		return toReturn;
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
		return toClientList(ofy().load().type(SubjectGroupServer.class).list());
	}

	public Ref<SubjectGroupServer> getRef(Long id) {
		SubjectGroupServer sGS = getServerById(id);
		Ref<SubjectGroupServer> toReturn = (sGS == null ? null : Ref.create(sGS));
		return toReturn;
	}
}
