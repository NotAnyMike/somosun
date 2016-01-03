package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.controlAbstract.SingleScoreAbstract;

public class SingleScoreDao implements Dao<SingleScore> {

	static{
		ObjectifyService.register(SingleScoreServer.class);
	}
	
	public Long save(SingleScoreAbstract singleScore) {
		Long toReturn = null;
		
		if(singleScore != null){
			if(singleScore.getId() == null) singleScore.setId(generateId());			
			
			if(singleScore.getSemesterValue() != null){
				SemesterValueDao sVDao = new SemesterValueDao();
				if(singleScore.getSemesterValue().getId() == null) {
					singleScore.getSemesterValue().setId(sVDao.generateId());
				}
				sVDao.save(singleScore.getSemesterValue());
			}
			
			SingleScoreServer singleScoreOriginal = getServerById(singleScore.getId());
			if(singleScoreOriginal.compare(singleScore) == false){
				ofy().save().entity(singleScore).now();
			}
			toReturn = singleScore.getId();				
		}
		
		return toReturn;
	}

	@Override
	public Long generateId() {
		
		ObjectifyFactory f = new ObjectifyFactory();
		Key<SingleScore> key = f.allocateId(SingleScore.class);
		return key.getId();
		
		
	}

	@Override
	public boolean delete(Long id) {
		boolean toReturn = false;
		
		if(id != null){
			Key<SingleScore> key = Key.create(SingleScore.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		
		return toReturn;
	}

	public SingleScoreServer getServerById(Long id) {
		SingleScoreServer sS = null;
		
		if(id != null){
			Key<SingleScoreServer> key = Key.create(SingleScoreServer.class, id);
			sS = (SingleScoreServer) ofy().load().key(key).now();
		}
		
		return sS;
	}
	
	@Override
	public SingleScore getById(Long id){
		SingleScoreServer sSS = getServerById(id);
		SingleScore toReturn = null;
		if(sSS != null) toReturn = sSS.getClientInstance();
		return toReturn;
	}

}
