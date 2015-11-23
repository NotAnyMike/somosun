package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;import com.googlecode.objectify.Ref;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;

public class ScoreDao implements Dao<Score>{
	
	static{
		ObjectifyService.register(Score.class);
	}

	@Override
	public Long save(Score score) {
		Long toReturn = null;
		
		if(score != null && score.getSubject() != null){
			if(score.getId() == null) score.setId(generateId());
			
			if(score.getScores() != null && score.getScores().isEmpty() == false){				
				SingleScoreDao sSDao = new SingleScoreDao();
				for(SingleScore sS : score.getScores()){
					if(sS.getId() == null) sS.setId(sSDao.save(sS));
				}
			}
			
			if(score.getTeacher() != null){				
				if(score.getTeacher().getIdSun() == null) {
					TeacherDao teacherDao = new TeacherDao();
					score.getTeacher().setIdSun(teacherDao.save(score.getTeacher()));
				}
//				score.setTeacherRef(Ref.create(score.getTeacher()));
			}
			
			if(score.getSubject().getId() == null) {
				SubjectDao subjectDao = new SubjectDao();
				score.getSubject().setId(subjectDao.save(score.getSubject()));
			}
//			score.setSubjectRef(Ref.create(score.getSubject()));
			
			ofy().save().entity(score).now();
			
			toReturn = score.getId();
		}
		
		return toReturn;
		
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Score> key = f.allocateId(Score.class);
		return key.getId();
	}

	@Override
	public boolean delete(Long id) {
		boolean toReturn = false;
		
		if(id != null){
			Key<Score> key = Key.create(Score.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		
		return toReturn;
	}

	public Score getById(Long id) {
		Score toReturn = null;
		
		if(id != null){
			Key<Score> key = Key.create(Score.class, id);
			toReturn = (Score) ofy().load().key(key).now();
		}
		
		return toReturn;
	}

	public Score getBySubjectId(Long subjectId) {
		
		Score toReturn = null;
		
		if(subjectId != null){
			toReturn = (Score) ofy().load().type(Score.class).filter("subject.idCopy", subjectId).first().now();
		}
		
		return toReturn;

	}

	public Score getBySubjectAndProfesor(Long subjectId, Long professorId) {
Score toReturn = null;
		
		if(professorId != null && subjectId != null){
			toReturn = (Score) ofy().load().type(Score.class).filter("subject.idCopy", subjectId).filter("teacher.idCopy", professorId).first().now();
		}
		
		return toReturn;
	}

}
