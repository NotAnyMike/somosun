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
	@Index @Load private Ref<Teacher> teacherRef = null;
	@Ignore private Teacher teacher = null;
	@Index @Load private Ref<Subject> subjectRef = null;
	@Ignore private Subject subject = null;
	@Index private Double totalAverage = null;
	private int totalAmount = 0;
	@Load private List<Ref<SingleScore>> scoresRef = null;
	@Ignore private List<SingleScore> scores = null;
	
	public Score(){}
	
	@OnLoad
	private void onLoad(){
		
		if(scoresRef != null && scoresRef.isEmpty() == false){
			setScores(new ArrayList<SingleScore>());
			for(Ref<SingleScore> ref : scoresRef){
				SingleScore sS = ref.get();
				if(sS != null) getScores().add(sS);
			}
		}
		
		if(teacherRef != null) teacher = teacherRef.get();
		
		subject = subjectRef.get();
	}

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

	public void setTotalAverage(Double totalAverage) {
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
		scoresRef = new ArrayList<Ref<SingleScore>>();
		for(SingleScore sS : scores){
			scoresRef.add(Ref.create(sS));
		}
	}
	
	public void addSingleScore(SingleScore singleScore){
		if(scoresRef == null) scoresRef = new ArrayList<Ref<SingleScore>>();
		if(scores == null) scores = new ArrayList<SingleScore>();
		scores.add(singleScore);
		scoresRef.add(Ref.create(singleScore));
	}

	public Ref<Teacher> getTeacherRef() {
		return teacherRef;
	}

	public void setTeacherRef(Ref<Teacher> teacherRef) {
		this.teacherRef = teacherRef;
	}

	public Ref<Subject> getSubjectRef() {
		return subjectRef;
	}

	public void setSubjectRef(Ref<Subject> subjectRef) {
		this.subjectRef = subjectRef;
	}
}
