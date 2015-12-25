package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.ScoreServer;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;

public class ScoreDao implements Dao<ScoreServer>{
	
	static{
		ObjectifyService.register(ScoreServer.class);
	}

	@Override
	public Long save(ScoreServer score) {
		Long toReturn = null;
		
		if(score != null && score.getSubject() != null){
			if(score.getId() == null) score.setId(generateId());
			
			if(score.getScores() != null && score.getScores().isEmpty() == false){				
				SingleScoreDao sSDao = new SingleScoreDao();
				for(Ref<SingleScoreServer> sS : score.getScores()){
					sS.get().setId(sSDao.save(sS.get()));
				}
			}
			
			if(score.getTeacher() != null){				
				TeacherDao teacherDao = new TeacherDao();
				score.getTeacher().get().setIdSun(teacherDao.save(score.getTeacher().get()));
			}
			
			SubjectDao subjectDao = new SubjectDao();
			score.getSubject().get().setId(subjectDao.save(score.getSubject().get()));
			
			ScoreServer scoreOriginal = getById(score.getId());
			if(score.compare(scoreOriginal) == false){				
				ofy().save().entity(score).now();
			}
			toReturn = score.getId();
			
		}
		
		return toReturn;
		
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<ScoreServer> key = f.allocateId(ScoreServer.class);
		return key.getId();
	}

	@Override
	public boolean delete(Long id) {
		boolean toReturn = false;
		
		if(id != null){
			Key<ScoreServer> key = Key.create(ScoreServer.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		
		return toReturn;
	}

	public ScoreServer getById(Long id) {
		ScoreServer toReturn = null;
		
		if(id != null){
			Key<ScoreServer> key = Key.create(ScoreServer.class, id);
			toReturn = (ScoreServer) ofy().load().key(key).now();
		}
		
		return toReturn;
	}

	/**
	 * This will search the general score (i.e. for no teacher (null))
	 * @param subjectId
	 * @return
	 */
	public ScoreServer getBySubjectId(Long subjectId) {
		
		ScoreServer toReturn = null;
		
		if(subjectId != null){
			toReturn = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject.idCopy", subjectId).filter("teacher", null).first().now();
		}
		
		return toReturn;

	}

	/**
	 * Send null in order to mean no teacher, be careful if professorId == null that means there is no information in the db nor that the professor is pending-info.
	 * <br></br>
	 * If there is a score with null professorId means that it is the general score and it MUST NOT be used in the group averageGrade field, it should be used only in the subject averageGrade field
	 * 
	 * @param subjectId
	 * @param professorId
	 * @return
	 */
	public ScoreServer getBySubjectAndProfesor(Long subjectId, Long professorId) {
		ScoreServer toReturn = null;
		
		if(professorId != null && subjectId != null){
			toReturn = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject.idCopy", subjectId).filter("teacher.idCopy", professorId).first().now();
		}
		
		return toReturn;
	}

	/**
	 * This will search the general score (i.e. for no teacher (null))
	 * @param code
	 * @return
	 */
	public ScoreServer getByCode(String code) {
		
		ScoreServer toReturn = null;
		
		if(code != null){
			toReturn = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject.code", code).filter("teacher", null).first().now();
		}
		
		return toReturn;
		
	}

}
