package com.uibinder.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.shared.SomosUNUtils;
import com.uibinder.shared.control.Career;
import com.uibinder.shared.control.ComplementaryValue;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectGroup;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ComplementaryValueDao extends Dao {

	static{
		ObjectifyService.register(ComplementaryValue.class);
	}
	
	public void saveComplementaryValues(ComplementaryValue cV){
		if(cV != null)
		{
			if(cV.getSubjectGroup() != null && cV.getSubject() != null && cV.getCareer() != null)
			{
				if(cV.getSubjectGroup().getId() == null){
					SubjectGroupDao sGDao = new SubjectGroupDao();
					Long id = sGDao.generateId();
					cV.getSubjectGroup().setId(id);
				}
				if(cV.getSubject().getId() == null){
					SubjectDao sDao = new SubjectDao();
					Long id = sDao.generateId();
					cV.getSubject().setId(id);
				}
				if(cV.getSubject().getId() != null && cV.getSubjectGroup().getId() != null)
					cV.setTypology(standardizeString(cV.getTypology()));
					ofy().save().entity(cV).now();
			}			
		}
	}
	
	public ComplementaryValue getComplementaryValues(Career career, Subject subject){
		return getComplementaryValues(career.getCode(), subject.getCode());
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

	public ComplementaryValue getComplementaryValues(String careerCode, String subjectCode) {
		
		ComplementaryValue toReturn = null;
		if(subjectCode != null && careerCode != null)
		{
			toReturn = (ComplementaryValue) ofy().load().type(ComplementaryValue.class).filter("career.code", careerCode).filter("subject.code", subjectCode).first().now();
		}
		
		return toReturn;
		
	}

	public void createComplementaryValuesForLibre(String careerCode) {
		if(getComplementaryValues(careerCode, SomosUNUtils.LIBRE_CODE) == null && careerCode.isEmpty() == false){
			CareerDao careerDao = new CareerDao();
			SubjectDao subjectDao = new SubjectDao();
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			Career c = careerDao.getCareerByCode(careerCode);
			Subject s = subjectDao.getDummySubjectByCode(SomosUNUtils.LIBRE_CODE);
			SubjectGroup sG = subjectGroupDao.getSubjectGroup(SomosUNUtils.LIBRE_CODE, careerCode);
			
			if(s != null && c != null && sG != null){				
				ComplementaryValue cVT = new ComplementaryValue(c, s, "l", false, sG);
				ComplementaryValueDao cVDao = new ComplementaryValueDao();
				cVDao.saveComplementaryValues(cVT);
			}
			
		}
		
	}
	
	public void createComplementaryValuesForOptativa(String careerCode, String subjectGroupId) {
		if(getComplementaryValues(careerCode, SomosUNUtils.OPTATIVA_CODE) == null && careerCode.isEmpty() == false && subjectGroupId.isEmpty() == false){
			CareerDao careerDao = new CareerDao();
			SubjectDao subjectDao = new SubjectDao();
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			Career c = careerDao.getCareerByCode(careerCode);
			Subject s = subjectDao.getDummySubjectByCode(SomosUNUtils.OPTATIVA_CODE);
			SubjectGroup sG = subjectGroupDao.getSubjectGroupById(subjectGroupId);
			
			if(s != null && c != null && sG != null){				
				ComplementaryValue cVT = new ComplementaryValue(c, s, (sG.isFundamental() == true ? "b" : "p"), false, sG);
				ComplementaryValueDao cVDao = new ComplementaryValueDao();
				cVDao.saveComplementaryValues(cVT);
			}
			
		}
		
	}

	public List<ComplementaryValue> getMandatoryComplementaryValues(String careerCode) {
		
		List<ComplementaryValue> list = null;
		
		list = ofy().load().type(ComplementaryValue.class).filter("career.code", careerCode).filter("mandatory", true).list();
				
		return list;
	}

	public void deleteComplementaryValues(Long id) {
		Key<ComplementaryValue> key = Key.create(ComplementaryValue.class, id);
		ofy().delete().key(key);//.now();
	}

	
}
