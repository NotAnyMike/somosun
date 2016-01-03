package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.ScoreServer;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.control.controlAbstract.ScoreAbstract;

public class ScoreDao implements Dao<Score>{
	
	static{
		ObjectifyService.register(ScoreServer.class);
	}

	public Long save(ScoreAbstract score) {
		Long toReturn = null;
		
		if(score != null && score.getSubject() != null){
			if(score.getId() == null) score.setId(generateId());
			
			if(score.getScores() != null && score.getScores().isEmpty() == false){				
				SingleScoreDao sSDao = new SingleScoreDao();
				for(SingleScore sS : score.getScores()){
					sS.setId(sSDao.save(sS));
				}
			}
			
			if(score.getTeacher() != null){				
				TeacherDao teacherDao = new TeacherDao();
				score.getTeacher().setIdSun(teacherDao.save(score.getTeacher()));
			}
			
			SubjectDao subjectDao = new SubjectDao();
			score.getSubject().setId(subjectDao.save(score.getSubject()));
			
			Score scoreOriginal = getById(score.getId());
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

	public Score getById(Long id) {
		ScoreServer score = null;
		Score toReturn = null;
		
		if(id != null){
			Key<ScoreServer> key = Key.create(ScoreServer.class, id);
			score = (ScoreServer) ofy().load().key(key).now();
		}
		
		if(score != null) toReturn = score.getClientInstance();
		
		return toReturn;
	}

	/**
	 * This will search the general score (i.e. for no teacher (null))
	 * @param subjectId
	 * @return
	 */
	public Score getBySubjectId(Long subjectId) {
		
		ScoreServer score = null;
		
		if(subjectId != null){
			SubjectDao subjectDao = new SubjectDao();
			Subject subject = subjectDao.getById(subjectId);
			Ref<Subject> subjectRef = null;
			if(subject != null) subjectRef = Ref.create(subject);
			
			score = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject", subjectRef).filter("teacher", null).first().now();
		}
		
		Score toReturn = null;
		if(score != null) toReturn = score.getClientInstance();
		
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
	public Score getBySubjectAndProfesor(Long subjectId, Long professorId) {
		ScoreServer score = null;
		
		if(professorId != null && subjectId != null){
			SubjectDao subjectDao = new SubjectDao();
			Subject subject = subjectDao.getById(subjectId);
			Ref<Subject> subjectRef = null;
			if(subject != null) Ref.create(subject);
			
			TeacherDao teacherDao = new TeacherDao();
			Teacher teacher = teacherDao.getById(professorId);
			Ref<Teacher> teacherRef = null;
			if(teacher != null) Ref.create(teacher);
			
			score = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject", subject).filter("teacher", teacher).first().now();
		}
		
		Score toReturn = null;
		if(score != null) toReturn = score.getClientInstance();
		
		return toReturn;
	}

	/**
	 * This will search the general score (i.e. for no teacher (null))
	 * @param code
	 * @return
	 */
	public Score getByCode(String code) {
		
		ScoreServer score = null;
		
		if(code != null){
			
			SubjectDao subjectDao = new SubjectDao();
			Subject subject = subjectDao.getByCode(code);
			Ref<Subject> subjectRef = null;
			if(subject != null) subjectRef = Ref.create(subject);
			
			score = (ScoreServer) ofy().load().type(ScoreServer.class).filter("subject", subjectRef).filter("teacher", null).first().now();
		}
		
		Score toReturn = null;
		if(score != null) toReturn = score.getClientInstance();
		
		return toReturn;
	}

}
