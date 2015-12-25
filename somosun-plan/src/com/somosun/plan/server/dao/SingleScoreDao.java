package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.SingleScore;

public class SingleScoreDao implements Dao<SingleScoreServer> {

	public Long save(SingleScoreServer singleScore) {
		Long toReturn = null;
		
		if(singleScore != null){
			if(singleScore.getId() == null) singleScore.setId(generateId());			
			
			if(singleScore.getSemesterValue() != null){
				SemesterValueDao sVDao = new SemesterValueDao();
				if(singleScore.getSemesterValue().get().getId() == null) {
					singleScore.getSemesterValue().get().setId(sVDao.generateId());
				}
				sVDao.save(singleScore.getSemesterValue().get());
			}
			
			SingleScoreServer singleScoreOriginal = getById(singleScore.getId());
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

	@Override
	public SingleScoreServer getById(Long id) {
		SingleScoreServer toReturn = null;
		
		if(id != null){
			Key<SingleScoreServer> key = Key.create(SingleScoreServer.class, id);
			toReturn = (SingleScoreServer) ofy().load().key(key).now();
		}
		
		return toReturn;
	}

}
