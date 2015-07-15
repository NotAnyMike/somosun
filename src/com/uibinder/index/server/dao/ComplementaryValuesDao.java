package com.uibinder.index.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.SomosUNUtils;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectGroup;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ComplementaryValuesDao extends Dao {

	static{
		ObjectifyService.register(ComplementaryValues.class);
	}
	
	public void saveComplementaryValues(ComplementaryValues cV){
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
	
	public ComplementaryValues getComplementaryValues(Career career, Subject subject){
		return getComplementaryValues(career.getCode(), subject.getCode());
	}
	
	public void deleteComplementaryValues(ComplementaryValues cV){
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
		Key<ComplementaryValues> key = f.allocateId(ComplementaryValues.class);
		
		return key.getId();
		
	}

	public List<ComplementaryValues> getComplementaryValues(String code) {
		return ofy().load().type(ComplementaryValues.class).filter("career.code", code).list();
	}

	public ComplementaryValues getComplementaryValues(String careerCode, String subjectCode) {
		
		ComplementaryValues toReturn = null;
		if(subjectCode != null && careerCode != null)
		{
			toReturn = (ComplementaryValues) ofy().load().type(ComplementaryValues.class).filter("career.code", careerCode).filter("subject.code", subjectCode).first().now();
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
				ComplementaryValues cVT = new ComplementaryValues(c, s, "l", false, sG);
				ComplementaryValuesDao cVDao = new ComplementaryValuesDao();
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
				ComplementaryValues cVT = new ComplementaryValues(c, s, (sG.isFundamental() == true ? "b" : "p"), false, sG);
				ComplementaryValuesDao cVDao = new ComplementaryValuesDao();
				cVDao.saveComplementaryValues(cVT);
			}
			
		}
		
	}

	public List<ComplementaryValues> getMandatoryComplementaryValues(String careerCode) {
		
		List<ComplementaryValues> list = null;
		
		list = ofy().load().type(ComplementaryValues.class).filter("career.code", careerCode).filter("mandatory", true).list();
				
		return list;
	}

	
}
