package com.somosun.plan.server.control;

import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.control.incomplete.ScoreIncomplete;

@Entity
public class ScoreServer extends ScoreIncomplete{

	@Index @Load private Ref<Teacher> teacher = null;
	@Index @Load private Ref<Subject> subject = null;
	@Index @Load private List<Ref<SingleScoreServer>> scores = null;
	
	public SingleScoreServer getSemester(Double semesterNumber) {
		SingleScoreServer toReturn = null;
		
		if(getScores() != null && !getScores().isEmpty()){			
			for(Ref<SingleScoreServer> singleScore : getScores()){
				if(singleScore.get().getSemesterValue().get().toStringDouble().equals(semesterNumber.toString())){
					toReturn = singleScore.get();
					break;
				}
			}
		}
		
		return toReturn;
	}

	public Ref<Teacher> getTeacher() {
		return teacher;
	}

	public void setTeacher(Ref<Teacher> teacher) {
		this.teacher = teacher;
	}
	
	public void setTeacher(Teacher t){
		if(t != null && t.getIdSun() != null){
			setTeacher(Ref.create(t));
		}
	}

	public Ref<Subject> getSubject() {
		return subject;
	}

	public void setSubject(Ref<Subject> subject) {
		this.subject = subject;
	}

	public void setSubject(Subject s){
		if(s != null && s.getId() != null){
			setSubject(Ref.create(s));
		}
	}
	
	public List<Ref<SingleScoreServer>> getScores() {
		return scores;
	}

	public void setScores(List<Ref<SingleScoreServer>> scores) {
		this.scores = scores;
	}
	
	public Score getShared(){
		Score toReturn = null;
		
		return toReturn;
	}
	
	public boolean compare(ScoreServer s){
		boolean toReturn = false;
		
		if(s != null){
			if(this.getId() == s.getId() && this.getTotalAmount() == s.getTotalAmount() && this.getTotalAverage() == s.getTotalAverage() && 
					this.getTeacher() == s.getTeacher() && this.getSubject() == s.getSubject()){
				List<Ref<SingleScoreServer>> list = this.getScores();
				boolean isEqual = true;
				if(list.size() == s.getScores().size()){
					for(int x = 0; x < list.size(); x++){
						if(list.get(x) != s.getScores().get(x)){
							isEqual = false;
							break;
						}
					}
					if(isEqual == true) toReturn = true;
				}
			}
		}
		
		return toReturn;
	}
	
}
