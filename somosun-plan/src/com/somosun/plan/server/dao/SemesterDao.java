package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.ComplementaryValueServer;
import com.somosun.plan.server.control.GroupServer;
import com.somosun.plan.server.control.SemesterServer;
import com.somosun.plan.server.control.SubjectGroupServer;
import com.somosun.plan.server.control.SubjectValueServer;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SemesterAbstract;

public class SemesterDao implements Dao<Semester> {
	
	static{
		ObjectifyService.register(SemesterServer.class);
		ObjectifyService.register(ComplementaryValueServer.class);
		ObjectifyService.register(SubjectValueServer.class);
		ObjectifyService.register(SubjectGroupServer.class);
		ObjectifyService.register(GroupServer.class);
		ObjectifyService.register(Career.class);
		ObjectifyService.register(Subject.class);
	}
	
	public Long save(SemesterAbstract s){
		Long id = null;
		
		/*
		 * 1. Check if the plan has id
		 * 2. Check if it is the same as the one in the db (be carefull with the case where it is a new plan, therefore originPlan = null [null pointer exception])
		 * 2.1 Compare the values and references if they are the same) if false save it
		 * 2.2 Take the plan's references (not planOrigina !IMportant) from the db and re do this thing (i.e. compare values and reference, then check the references values and its references and so on)
		 */
		if(s != null){
			
			if(s.getId() == null) s.setId(generateId());

			/******** check for the references' values (i.e. only for Ref<SemesterValue> and Ref<SubjectValue>) ********/
			if(s.getSemesterValue() != null){
				SemesterValueDao semesterValueDao = new SemesterValueDao();
				s.getSemesterValue().setId(semesterValueDao.save(s.getSemesterValue()));
			}
			if(s.getSubjects() != null && s.getSubjects().isEmpty() == false){
				SubjectValueDao subjectValueDao = new SubjectValueDao();
				for(SubjectValue subjectValue : s.getSubjects()){
					subjectValue.setId(subjectValueDao.save(subjectValue));
				}
			}
			/*******************************************************************************/
			
			/******* save the entity's plan *******/
			SemesterServer original = getServerById(s.getId());
			if(original == null || original.compare(s) == false){
				
				if(original == null) original = new SemesterServer();
				
				SubjectValueDao sVDao = new SubjectValueDao();
				
				original.setId(s.getId());
				original.setSemesterValue(s.getSemesterValue());
				List<Ref<SubjectValueServer>> subjects = null;
				if(s.getSubjects() != null){
					for(SubjectValue sV : s.getSubjects()){
						if(subjects == null) subjects = new ArrayList<Ref<SubjectValueServer>>();
						if(sV.getId() != null){
							Ref<SubjectValueServer> ref = sVDao.getRef(sV.getId());
							if(ref != null) subjects.add(ref);
						}
					}
				}
				original.setSubjectValuesListRef(subjects);
				
				//save original
				ofy().defer().save().entity(original);
				
			}
			id = original.getId();
			/**************************************/
			
		}
		
		return id;
	}
	
//	public Long save(SemesterAbstract s){
//		Long toReturn = null;
//		if(s != null) {
//			List<SubjectValue> subjectValuesList = s.getSubjects();
//			SubjectValueDao sVDao = new SubjectValueDao();
//			SemesterValueDao semesterValueDao = new SemesterValueDao();
//			GroupDao gDao = new GroupDao();
//			ComplementaryValueDao cVDao = new ComplementaryValueDao();
//			for(SubjectValue sV : subjectValuesList){
//				if(sV.getId() == null || sV.getGroup() == null){
//					if(sV.getGroup() != null){
//						if(sV.getGroup().getId() == null){
//							gDao.save(sV.getGroup());
//						}
//					}else{
//						Group g = null;
//						g = gDao.getOrCreateGroup(sV.getComplementaryValue().getSubject(), s.getSemesterValue(), null);
//						sV.setGroup(g);
//					}
//					if(sV.getComplementaryValue() != null){
//						if(sV.getComplementaryValue().getId() == null){
//							cVDao.save(sV.getComplementaryValue());
//						}
//					}
//					sVDao.save(sV);
//				}
//			}
//			if(s.getSemesterValue() != null && s.getSemesterValue().getId() == null){
//				SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(s.getSemesterValue().getYear(), s.getSemesterValue().getNumberSemester());
//				s.setSemesterValue(semesterValue);
//			}
//			ofy().save().entity(s).now();
//			toReturn = s.getId();
//		}
//		return toReturn;
//	}
	
	public Long generateId(){
		
		 ObjectifyFactory f = new ObjectifyFactory();
		 Key<SemesterServer> key = f.allocateId(SemesterServer.class);
		 return key.getId();
		
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<SemesterServer> key = Key.create(SemesterServer.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllSemesters() {
		List<Semester> list = getAllSemesters();
		for(Semester s : list){
			delete(s.getId());
		}
	}

	private List<Semester> getAllSemesters() {
		List<SemesterServer> list = ofy().load().type(SemesterServer.class).list();
		List<Semester> toReturn = null;
		if(list != null){
			for(SemesterServer sS : list){
				if(toReturn == null) toReturn = new ArrayList<Semester>();
				toReturn.add(sS.getClientInstance());
			}
		}
		return toReturn; 
	}

	private SemesterServer getServerById(Long id){
		SemesterServer semester = null;
		Semester toReturn = null;
		if(id != null){
			Key<SemesterServer> key = Key.create(SemesterServer.class, id);
			semester = ofy().load().key(key).now();
		}
		return semester;
	}
	
	public Semester getById(Long id){
		Semester toReturn = null;
		SemesterServer sS = getServerById(id);
		if(sS != null) toReturn = sS.getClientInstance();
		return toReturn;
	}

	protected Ref<SemesterServer> getRef(Long id) {
		SemesterServer s = getServerById(id);
		Ref<SemesterServer> ref = (s == null ? null : Ref.create(s));
		return ref;
	}
	
}
