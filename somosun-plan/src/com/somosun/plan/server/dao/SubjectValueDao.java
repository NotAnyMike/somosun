package com.somosun.plan.server.dao;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.ComplementaryValueServer;
import com.somosun.plan.server.control.GroupServer;
import com.somosun.plan.server.control.SubjectGroupServer;
import com.somosun.plan.server.control.SubjectValueServer;
import com.somosun.plan.server.serviceImpl.LoginServiceImpl;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SubjectValueAbstract;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubjectValueDao implements Dao<SubjectValue> {
	
	static{
		ObjectifyService.register(SubjectValueServer.class);
		ObjectifyService.register(ComplementaryValueServer.class);
		ObjectifyService.register(SubjectGroupServer.class);
		ObjectifyService.register(GroupServer.class);
		ObjectifyService.register(Career.class);
		ObjectifyService.register(Block.class);
	}
	
	private SubjectValueServer getServerById(Long id){
		SubjectValueServer toReturn = null;
		if(id != null){
			Key<SubjectValueServer> key = Key.create(SubjectValueServer.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}
	
	@Override
	public SubjectValue getById(Long id) {
		SubjectValueServer sVS = getServerById(id);
		SubjectValue toReturn = null;
		if(sVS != null) toReturn = sVS.getClientInstance();
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
			SubjectValueServer original = getServerById(sV.getId());
			if(original == null || original.compare(sV) == false){
				
				if(original == null) original = new SubjectValueServer();
				
				ComplementaryValueDao cVDao = new ComplementaryValueDao();
				GroupDao gDao = new GroupDao();
				
				original.setId(sV.getId());
				original.setGrade(sV.getGrade());
				original.setTaken(sV.isTaken());
				original.setComplementaryValueRef(cVDao.getRef(sV.getComplementaryValue().getId()));
				original.setGroupRef((sV.getGroup() == null ? null : gDao.getRef(sV.getGroup().getId())));
				
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
	public SubjectValue getByGroup(Group g){
		return toSubjectValue((SubjectValueServer) ofy().load().type(SubjectValueServer.class).filter("group", g).first().now());
	}

	private SubjectValue toSubjectValue(SubjectValueServer sVS) {
		SubjectValue toReturn = null;
		if(sVS != null) toReturn = sVS.getClientInstance();
		return toReturn;
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
		List<SubjectValue> list = getAllSubjectValues();
		for(SubjectValue sV : list){
			delete(sV.getId());
		}
	}

	private List<SubjectValue> getAllSubjectValues() {
		List<SubjectValueServer> list = ofy().load().type(SubjectValueServer.class).list();
		List<SubjectValue> toReturn = null;
		if(list != null){
			for(SubjectValueServer sVS : list){
				if(toReturn == null) toReturn = new ArrayList<SubjectValue>();
				toReturn.add(sVS.getClientInstance());
			}
		}
		return toReturn;
	}

	protected Ref<SubjectValueServer> getRef(Long id) {
		SubjectValueServer sV = getServerById(id);
		Ref<SubjectValueServer> ref = (sV == null ? null : Ref.create(sV));
		return ref;
	}

}
