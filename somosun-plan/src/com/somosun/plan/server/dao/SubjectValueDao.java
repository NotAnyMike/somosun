package com.somosun.plan.server.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.server.control.PlanServer;
import com.somosun.plan.server.control.SubjectValueServer;
import com.somosun.plan.server.serviceImpl.LoginServiceImpl;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.controlAbstract.SubjectValueAbstract;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValueDao implements Dao<SubjectValueServer> {
	
	static{
		ObjectifyService.register(SubjectValueServer.class);
		ObjectifyService.register(ComplementaryValue.class);
		ObjectifyService.register(Group.class);
	}
	
	public SubjectValueServer getById(Long id){
		SubjectValueServer toReturn = null;
		if(id != null){
			Key<SubjectValueServer> key = Key.create(SubjectValueServer.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}

//	public Long save(SubjectValueServer sV){
//		Long toReturn = null;
//		if(sV != null){
//			
//			GroupDao groupDao = new GroupDao();
//			if(sV.getGroup() == null){
//				sV.setGroup(groupDao.getOrCreateGroup(sV.getComplementaryValue().getSubject(), null, null));
//			}
//			
//			if(sV.getGroup().getId() == null){
//				sV.getGroup().setId(groupDao.save(sV.getGroup()));
//			}
//
//			if(sV.getGrade() > 5 || sV.getGrade() < 0){
//				sV.setTaken(false);
//			}
//			
//			if(sV.getComplementaryValue() != null && sV.getComplementaryValue().getId() == null){
//				ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
//				sV.getComplementaryValue().setId(complementaryValueDao.save(sV.getComplementaryValue()));
//			}
//			
//			if(sV.getId() == null) sV.setId(generateId());
//			ofy().save().entity(sV).now();
//			toReturn = sV.getId();
//			
//		}
//		return toReturn;
//	}
	
	public Long save(SubjectValueAbstract sV){
		Long id = null;
		
		if(sV != null){
			
			if(sV.getId() == null) sV.setId(generateId());
			
			
			/****** check references ******/
			//complementaryValue
			if(sV.getComplementaryValue() != null) {
				ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
				sV.getComplementaryValue().setId(complementaryValueDao.save(sV.getComplementaryValue()));
			}
			
			//group
			if(sV.getGroup() != null){
				GroupDao groupDao = new GroupDao();
				sV.getGroup().setId(groupDao.save(sV.getGroup()));
			}
			/******************************/
			
			/******* save the entity's plan *******/
			SubjectValueServer original = getById(sV.getId());
			if(original == null || original.compare(sV) == false){
				
				if(original == null) original = new SubjectValueServer();
				
				original.setId(sV.getId());
				original.setGrade(sV.getGrade());
				original.setTaken(sV.isTaken());
				original.setComplementaryValue(sV.getComplementaryValue());
				original.setGroup(sV.getGroup());
				
				//save original
				ofy().defer().save().entity(original);
				
			}
			id = original.getId();
			/**************************************/
			
		}
		
		
		return id;
	}
	
	/**
	 * TODO: correct the filter, use an embedded filter
	 * 
	 * @param g
	 * @return
	 */
	public SubjectValueServer getByGroup(Group g){
		return (SubjectValueServer) ofy().load().type(SubjectValueServer.class).filter("group", g).first().now();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SubjectValueServer> key = f.allocateId(SubjectValueServer.class);
		return key.getId();
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){
			Key<SubjectValueServer> key = Key.create(SubjectValueServer.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public void deleteAllSubjectValues() {
		List<SubjectValueServer> list = getAllSubjectValues();
		for(SubjectValueServer sV : list){
			delete(sV.getId());
		}
	}

	private List<SubjectValueServer> getAllSubjectValues() {
		return ofy().load().type(SubjectValueServer.class).list();
	}
}
