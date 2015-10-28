package com.somosun.plan.server.dao;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ComplementaryValueDao implements Dao<ComplementaryValue> {

	private static final Logger log = Logger.getLogger("ComplementaryValueDao");
	
	static{
		ObjectifyService.register(ComplementaryValue.class);
	}
	
	public Long save(ComplementaryValue cV){
		Long toReturn = null;
		if(cV != null){
			if(cV.getSubjectGroup() != null && cV.getSubject() != null && cV.getCareer() != null){
				SubjectDao sDao = new SubjectDao();

				if(cV.getSubjectGroup().getId() == null){
					SubjectGroupDao sGDao = new SubjectGroupDao();
					Long id = sGDao.generateId();
					cV.getSubjectGroup().setId(id);
				}
				if(cV.getSubject().getId() == null){
					Long id = sDao.generateId();
					cV.getSubject().setId(id);
				}
				if(cV.getSubject().getId() != null && cV.getSubjectGroup().getId() != null)
					cV.setTypology(SomosUNUtils.standardizeString(cV.getTypology(), false, false));//standardizeString(cV.getTypology()));
				
				for(List<Subject> lists : cV.getCorequisitesLists()){
					for(Subject s : lists)
						if(s.getId() == null) s.setId(sDao.generateId());
				}
				for(List<Subject> lists : cV.getPrerequisitesLists()){
					for(Subject s : lists)
						if(s.getId() == null) s.setId(sDao.generateId());
				}
				for(Subject s : cV.getListCorequisitesOf())
					if(s.getId() == null) s.setId(sDao.generateId());
				for(Subject s : cV.getListPrerequisitesOf())
					if(s.getId() == null) s.setId(sDao.generateId());
				
				ofy().save().entity(cV).now();
				toReturn = cV.getId();
			}			
		}
		return toReturn;
	}
	
	public ComplementaryValue get(Career career, Subject subject){
		return get(career.getCode(), subject.getCode());
	}
	
	public void deleteComplementaryValues(ComplementaryValue cV){
		if(cV != null)
		{
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
		Key<ComplementaryValue> key = f.allocateId(ComplementaryValue.class);
		
		return key.getId();
		
	}

	public List<ComplementaryValue> getComplementaryValues(String code) {
		return ofy().load().type(ComplementaryValue.class).filter("career.code", code).list();
	}

	public ComplementaryValue get(String careerCode, String subjectCode) {
		
		ComplementaryValue toReturn = null;
		if(subjectCode != null && careerCode != null)
		{
			toReturn = (ComplementaryValue) ofy().load().type(ComplementaryValue.class).filter("career.code", careerCode).filter("subject.code", subjectCode).first().now();
		}
		
		return toReturn;
		
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
		
		List<ComplementaryValue> list = null;
		
		list = ofy().load().type(ComplementaryValue.class).filter("career.code", careerCode).filter("mandatory", true).list();
				
		return list;
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<ComplementaryValue> key = Key.create(ComplementaryValue.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllComplementeryValues() {
		List<ComplementaryValue> list = getAllComplementaryValues();
		for(ComplementaryValue cV : list){
			delete(cV.getId());
		}
		log.warning("All complementaryValues deleted");
	}

	private List<ComplementaryValue> getAllComplementaryValues() {
		return ofy().load().type(ComplementaryValue.class).list();
	}

	public void deleteCertainComplementeryValues(final String careerCode) {
		final List<ComplementaryValue> list = getComplementaryValuesForCareer(careerCode);
		ofy().transact(new VoidWork(){
			public void vrun() {
				for(ComplementaryValue cV : list){
					delete(cV.getId());
				}
				log.warning("All complementaryValues for " + careerCode + " were deleted");
			}
		});
	}

	private List<ComplementaryValue> getComplementaryValuesForCareer(String careerCode) {
		return ofy().load().type(ComplementaryValue.class).filter("career.code", careerCode).list();
	}

	@Override
	public ComplementaryValue getById(Long id) {
		ComplementaryValue toReturn = null;
		if(id!=null){
			Key<ComplementaryValue> key = Key.create(ComplementaryValue.class, id);
			toReturn = (ComplementaryValue) ofy().load().key(key).now();
		}
		return toReturn;
	}

	
}
