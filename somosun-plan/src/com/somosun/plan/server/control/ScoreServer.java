package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.control.controlAbstract.ScoreAbstract;

@Entity
public class ScoreServer extends ScoreAbstract{

	@Index @Load private Ref<Teacher> teacher = null;
	@Index @Load private Ref<Subject> subject = null;
	@Index @Load private List<Ref<SingleScoreServer>> scores = null;
	
	public SingleScoreServer getSemester(Double semesterNumber) {
		SingleScoreServer toReturn = null;
		
		if(getScores() != null && !getScores().isEmpty()){			
			for(Ref<SingleScoreServer> singleScore : getScoresRef()){
				if(singleScore.get().getSemesterValue().toStringDouble().equals(semesterNumber.toString())){
					toReturn = singleScore.get();
					break;
				}
			}
		}
		
		return toReturn;
	}

	public Ref<Teacher> getTeacherRef() {
		return teacher;
	}
	
	public Teacher getTeacher(){
		Teacher toReturn = null;
		if(teacher != null) toReturn = teacher.get();
		return toReturn;
	}

	public void setTeacher(Ref<Teacher> teacher) {
		this.teacher = teacher;
	}
	
	public void setTeacher(Teacher t){
		if(t != null && t.getIdSun() != null){
			setTeacher(Ref.create(t));
		}
	}

	public Ref<Subject> getSubjectRef() {
		return subject;
	}
	
	public Subject getSubject(){
		Subject toReturn = null;
		
		if(subject != null) toReturn = subject.get();
		
		return toReturn;
	}

	public void setSubject(Ref<Subject> subject) {
		this.subject = subject;
	}

	public void setSubject(Subject s){
		if(s != null && s.getId() != null){
			setSubject(Ref.create(s));
		}
	}
	
	public List<Ref<SingleScoreServer>> getScoresRef() {
		return scores;
	}
	
	public List<SingleScore> getScores(){
		List<SingleScore> list = null;
		if(scores != null){
			for(Ref<SingleScoreServer> ref : scores){
				SingleScoreServer sSS = ref.get();
				if(sSS != null){
					if(list == null) list = new ArrayList<SingleScore>();
					list.add(sSS.getClientInstance());
				}
			}
		}
		
		return list;
	}

	public void setScores(List<Ref<SingleScoreServer>> scores) {
		this.scores = scores;
	}
	
	public Score getClientInstance(){
		Score toReturn = new Score();
		
		toReturn.setId(getId());
		toReturn.setScores(getScores());
		toReturn.setSubject(getSubject());
		toReturn.setTeacher(getTeacher());
		toReturn.setTotalAmount(getTotalAmount());
		toReturn.setTotalAverage(getTotalAmount());
		
		return toReturn;
	}
	
}
