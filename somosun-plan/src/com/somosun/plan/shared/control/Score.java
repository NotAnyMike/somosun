package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;

@Entity
public class Score implements Serializable {

	@Id private Long id = null;
//	@Index @Load private Ref<Teacher> teacherRef = null;
//	@Index @Load private Ref<Subject> subjectRef = null;
//	@Load private List<Ref<SingleScore>> scoresRef = null;
	@Index private Teacher teacher = null;
	@Index private Subject subject = null;
	@Index private double totalAverage = 0.0;
	private int totalAmount = 0;
	@Index private List<SingleScore> scores = null;
	
	public Score(){}
	
//	@OnLoad
//	private void onLoad(){
//		
//		if(scoresRef != null && scoresRef.isEmpty() == false){
//			setScores(new ArrayList<SingleScore>());
//			for(Ref<SingleScore> ref : scoresRef){
//				SingleScore sS = ref.get();
//				if(sS != null) getScores().add(sS);
//			}
//		}
//		
//		if(teacherRef != null) teacher = teacherRef.get();
//		
//		subject = subjectRef.get();
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Double getTotalAverage() {
		return totalAverage;
	}

	public void setTotalAverage(double totalAverage) {
		this.totalAverage = totalAverage;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<SingleScore> getScores() {
		return scores;
	}

	public void setScores(List<SingleScore> scores) {
		this.scores = scores;
	}
	
	public void addSingleScore(SingleScore singleScore){
		if(scores == null) scores = new ArrayList<SingleScore>();
		scores.add(singleScore);
	}

	public SingleScore getSemester(Double semesterNumber) {
		SingleScore toReturn = null;
		
		if(scores != null && !scores.isEmpty()){			
			for(SingleScore singleScore : scores){
				if(singleScore.getSemesterValue().toStringDouble().equals(semesterNumber.toString())){
					toReturn = singleScore;
					break;
				}
			}
		}
		
		return toReturn;
	}

}
