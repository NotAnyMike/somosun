package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Ref;
import com.somosun.plan.shared.control.SingleScore;

public class SingleScoreDao implements Dao<SingleScore> {

	public Long save(SingleScore singleScore) {
		Long toReturn = null;
		
		if(singleScore != null){
			if(singleScore.getId() == null) singleScore.setId(generateId());
			
			if(singleScore.getSemesterValue() != null && singleScore.getSemesterValue().getId() == null){
				SemesterValueDao sVDao = new SemesterValueDao();
				singleScore.getSemesterValue().setId(sVDao.generateId());
				sVDao.save(singleScore.getSemesterValue());
			}
			
			if(singleScore.getSemesterValue() != null){				
				singleScore.setSemesterValueRef(Ref.create(singleScore.getSemesterValue()));
			}else{
				singleScore.setSemesterValueRef(null);
			}
			
			ofy().save().entity(singleScore).now();
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
	public SingleScore getById(Long id) {
		SingleScore toReturn = null;
		
		if(id != null){
			Key<SingleScore> key = Key.create(SingleScore.class, id);
			toReturn = (SingleScore) ofy().load().key(key).now();
		}
		
		return toReturn;
	}

}
