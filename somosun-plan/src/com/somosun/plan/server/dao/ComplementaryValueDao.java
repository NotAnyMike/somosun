package com.somosun.plan.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;
import com.somosun.plan.server.control.ComplementaryValueServer;
import com.somosun.plan.server.control.GroupServer;
import com.somosun.plan.server.control.SubjectGroupServer;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.controlAbstract.ComplementaryValueAbstract;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ComplementaryValueDao implements Dao<ComplementaryValue> {

	private static final Logger log = Logger.getLogger("ComplementaryValueDao");
	
	static{
		ObjectifyService.register(ComplementaryValueServer.class);
		ObjectifyService.register(SubjectGroupServer.class);
		ObjectifyService.register(GroupServer.class);
		ObjectifyService.register(Career.class);
		ObjectifyService.register(Subject.class);
		ObjectifyService.register(Block.class);
	}
	
	public Long save(ComplementaryValueAbstract cV){
		Long id = null;
		
		if(cV != null){
			
			if(cV.getId() == null) cV.setId(generateId());
			
			
			/****** check references ******/
			//career
			if(cV.getCareer() != null) {
				CareerDao careerDao = new CareerDao();
				cV.getCareer().setId(careerDao.save(cV.getCareer()));
			}
			
			//subjectGroup
			if(cV.getSubjectGroup() != null){
				SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
				cV.getSubjectGroup().setId(subjectGroupDao.save(cV.getSubjectGroup()));
			}
			
			//subjectGroup
			SubjectDao subjectDao = new SubjectDao();
			if(cV.getSubject() != null){				
				cV.getSubject().setId(subjectDao.save(cV.getSubject()));
			}
			
			//requisites
			//SUBJECTS MUST NOT BE UPDATED WHEN A COMPLAMENTARYVALUE IS SAVED, THEY CAN BE UPDATED ONLY FROM THE SIA
//			List<Subject> allRequisites = new ArrayList<Subject>();
//			allRequisites.addAll(cV.getListCorequisites());
//			allRequisites.addAll(cV.getListPrerequisites());
//			allRequisites.addAll(cV.getListCorequisitesOf());
//			allRequisites.addAll(cV.getListPrerequisitesOf());
//			for(Subject s : allRequisites){
//				if(s != null){
//					s.setId(subjectDao.save(s));
//				}
//			}
			/******************************/
			
			/******* save the entity's plan *******/
			ComplementaryValueServer original = getServerById(cV.getId());
			if(original == null || original.compare(cV) == false){
				
				if(original == null) original = new ComplementaryValueServer();
				
				SubjectGroupDao sGDao = new SubjectGroupDao();
				
				original.setId(cV.getId());
				original.setTypology(cV.getTypology());
				original.setMandatory(cV.isMandatory());
				original.setCareer(cV.getCareer());
				original.setSubject(cV.getSubject());
				original.setSubjectGroupRef((cV.getSubjectGroup() == null ? null : sGDao.getRef(cV.getSubjectGroup().getId())));
				original.setListPrerequisites(cV.getPrerequisitesLists());
				original.setListCorequisites(cV.getCorequisitesLists());
				original.setListPrerequisitesOf(cV.getListPrerequisitesOf());
				original.setListCorequisitesOf(cV.getListCorequisitesOf());
				
				//save original
				ofy().defer().save().entity(original);
				
			}
			id = original.getId();
			/**************************************/
			
		}
		
		return id;
	}
	
//	public Long save(ComplementaryValue cV){
//		Long toReturn = null;
//		if(cV != null){
//			if(cV.getSubjectGroup() != null && cV.getSubject() != null && cV.getCareer() != null){
//				SubjectDao sDao = new SubjectDao();
//
//				if(cV.getSubjectGroup().getId() == null){
//					SubjectGroupDao sGDao = new SubjectGroupDao();
//					Long id = sGDao.generateId();
//					cV.getSubjectGroup().setId(id);
//				}
//				if(cV.getSubject().getId() == null){
//					Long id = sDao.generateId();
//					cV.getSubject().setId(id);
//				}
//				if(cV.getSubject().getId() != null && cV.getSubjectGroup().getId() != null)
//					cV.setTypology(SomosUNUtils.standardizeString(cV.getTypology(), false, false));//standardizeString(cV.getTypology()));
//				
//				for(List<Subject> lists : cV.getCorequisitesLists()){
//					for(Subject s : lists)
//						if(s.getId() == null) s.setId(sDao.generateId());
//				}
//				for(List<Subject> lists : cV.getPrerequisitesLists()){
//					for(Subject s : lists)
//						if(s.getId() == null) s.setId(sDao.generateId());
//				}
//				for(Subject s : cV.getListCorequisitesOf())
//					if(s.getId() == null) s.setId(sDao.generateId());
//				for(Subject s : cV.getListPrerequisitesOf())
//					if(s.getId() == null) s.setId(sDao.generateId());
//				
//				ofy().save().entity(cV).now();
//				toReturn = cV.getId();
//			}			
//		}
//		return toReturn;
//	}
	
	public ComplementaryValue get(Career career, Subject subject){
		return get(career.getCode(), subject.getCode());
	}
	
	public void deleteComplementaryValues(ComplementaryValue cV){
		if(cV != null)
		{
			//FIXME
			ofy().delete().entity(cV).now();			
		}
	}
	
	/**
	 * Will create a unique key if the entity X has an embedded entity Y which has an empty id, this will not allow to save the X entity,
	 * then use this method in order to set the Y's id.
	 * 
	 * @return
	 */
	public Long generateId() {
		
		ObjectifyFactory f = new ObjectifyFactory();
		Key<ComplementaryValueServer> key = f.allocateId(ComplementaryValueServer.class);
		
		return key.getId();
		
	}

	public List<ComplementaryValue> getComplementaryValues(String code) {
		List<ComplementaryValueServer> list = ofy().load().type(ComplementaryValueServer.class).filter("career.code", code).list();
		List<ComplementaryValue> listToReturn = null;
		if(list != null){
			for(ComplementaryValueServer cVS : list){
				if(listToReturn == null) listToReturn = new ArrayList<ComplementaryValue>();
				listToReturn.add(cVS.getClientInstance());
			}
		}
		return listToReturn;
	}

	public ComplementaryValue get(String careerCode, String subjectCode) {
		
		ComplementaryValue complementaryValue = null;
		if(subjectCode != null && careerCode != null){
			
			CareerDao careerDao = new CareerDao();
			Ref<Career> careerRef = null;
			Career career = careerDao.getByCode(careerCode);
			if(career != null) careerRef = Ref.create(career);
			
			SubjectDao subjectDao = new SubjectDao();
			Ref<Subject> subjectRef = null;
			Subject subject = subjectDao.getByCode(subjectCode);
			if(subject != null) subjectRef = Ref.create(subject);
			
			ComplementaryValueServer complementaryValueServer = (ComplementaryValueServer) ofy().load().type(ComplementaryValueServer.class).filter("career", careerRef).filter("subject", subjectRef).first().now();
			
			if(complementaryValueServer != null){
				complementaryValue = complementaryValueServer.getClientInstance();
				if(complementaryValue.getSubject() != null) {
					
					Subject subjectUpdated = subjectDao.getById(complementaryValue.getSubject().getId());
					if(subjectUpdated != null){
						complementaryValue.setSubject(subjectUpdated);						
					}else{						
						complementaryValue.setSubject(subjectDao.getByCode(complementaryValue.getSubject().getCode()));
					}
					
				}
				if(complementaryValue.getSubjectGroup() != null){
					SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
					
					SubjectGroup subjectGroupUpdated = subjectGroupDao.getById(complementaryValue.getSubjectGroup().getId());
					if(subjectGroupUpdated != null){
						complementaryValue.setSubjectGroup(subjectGroupUpdated);						
					}else{						
						SubjectGroup sGST = subjectGroupDao.get(complementaryValue.getSubjectGroup().getName(), complementaryValue.getSubjectGroup().isFundamental(), complementaryValue.getCareer().getCode()); 
						complementaryValue.setSubjectGroup((sGST == null ? null : sGST));
					}
					
					
				}
				if(complementaryValue.getSubject() != null && complementaryValue.getSubject().getId() != null){
					//Update the new values
					Subject s = subjectDao.getById(complementaryValue.getSubject().getId());
					complementaryValue.setSubject(s);
				}
				complementaryValue.setId(save(complementaryValue));
			}
		}
		
		return complementaryValue;
		
	}

	public void createComplementaryValuesForLibre(String careerCode) {
		if(get(careerCode, SomosUNUtils.LIBRE_CODE) == null && careerCode.isEmpty() == false){
			CareerDao careerDao = new CareerDao();
			SubjectDao subjectDao = new SubjectDao();
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			Career c = careerDao.getByCode(careerCode);
			Subject s = subjectDao.getDummySubjectByCode(SomosUNUtils.LIBRE_CODE);
			SubjectGroup sG = subjectGroupDao.get(SomosUNUtils.LIBRE_CODE, careerCode);
			
			if(s != null && c != null && sG != null){				
				ComplementaryValue cVT = new ComplementaryValue(c, s, "l", false, sG);
				ComplementaryValueDao cVDao = new ComplementaryValueDao();
				cVDao.save(cVT);
			}
			
		}
		
	}
	
	public void createComplementaryValuesForOptativa(String careerCode, String subjectGroupId) {
		if(get(careerCode, SomosUNUtils.OPTATIVA_CODE) == null && careerCode.isEmpty() == false && subjectGroupId.isEmpty() == false){
			CareerDao careerDao = new CareerDao();
			SubjectDao subjectDao = new SubjectDao();
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			Career c = careerDao.getByCode(careerCode);
			Subject s = subjectDao.getDummySubjectByCode(SomosUNUtils.OPTATIVA_CODE);
			SubjectGroup sG = subjectGroupDao.getById(subjectGroupId);
			
			if(s != null && c != null && sG != null){				
				ComplementaryValue cVT = new ComplementaryValue(c, s, (sG.isFundamental() == true ? "b" : "p"), false, sG);
				ComplementaryValueDao cVDao = new ComplementaryValueDao();
				cVDao.save(cVT);
			}
			
		}
		
	}

	public List<ComplementaryValue> getMandatoryComplementaryValues(String careerCode) {
		
		List<ComplementaryValueServer> list = null;
		List<ComplementaryValue> toReturn = null;
		CareerDao careerDao = new CareerDao();
		Career career = careerDao.getByCode(careerCode);
		Ref<Career> ref = null;
		if(career != null) ref = Ref.create(career);
		list = ofy().load().type(ComplementaryValueServer.class).filter("career", ref).filter("mandatory", true).list();
		if(list != null){
			for(ComplementaryValueServer cVS : list){
				if(toReturn == null) toReturn = new ArrayList<ComplementaryValue>();
				toReturn.add(cVS.getClientInstance());
			}
		}
		
		return toReturn;
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<ComplementaryValueServer> key = Key.create(ComplementaryValueServer.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllComplementeryValues() {
		List<ComplementaryValueServer> list = getAllComplementaryValues();
		for(ComplementaryValueServer cV : list){
			delete(cV.getId());
		}
		log.warning("All complementaryValues deleted");
	}

	private List<ComplementaryValueServer> getAllComplementaryValues() {
		return ofy().load().type(ComplementaryValueServer.class).list();
	}

	public void deleteCertainComplementeryValues(final String careerCode) {
		final List<ComplementaryValueServer> list = getComplementaryValuesForCareer(careerCode);
		ofy().transact(new VoidWork(){
			public void vrun() {
				for(ComplementaryValueServer cV : list){
					delete(cV.getId());
				}
				log.warning("All complementaryValues for " + careerCode + " were deleted");
			}
		});
	}

	private List<ComplementaryValueServer> getComplementaryValuesForCareer(String careerCode) {
		CareerDao careerDao = new CareerDao();
		Career career = careerDao.getByCode(careerCode);
		Ref<Career> ref = null;
		if(career != null) ref = Ref.create(career);
		return ofy().load().type(ComplementaryValueServer.class).filter("career", ref).list();
	}

	private ComplementaryValueServer getServerById(Long id) {
		ComplementaryValueServer toReturn = null;
		if(id!=null){
			Key<ComplementaryValueServer> key = Key.create(ComplementaryValueServer.class, id);
			toReturn = (ComplementaryValueServer) ofy().load().key(key).now();
		}
		return toReturn;
	}
	
	public ComplementaryValue getById(Long id){
		ComplementaryValueServer cVS = getServerById(id);
		ComplementaryValue toReturn = null;
		if(cVS != null) toReturn = cVS.getClientInstance();
		return toReturn;
	}

	protected Ref<ComplementaryValueServer> getRef(Long id) {
		Ref<ComplementaryValueServer> ref = null;
		ComplementaryValueServer cVS = getServerById(id);
		if(cVS != null) ref = Ref.create(cVS);
		return ref;
	}

	
}
