package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.somosun.plan.server.control.SubjectValueServer;
import com.somosun.plan.shared.control.controlAbstract.SemesterAbstract;

/**
 * 
 * @author MW
 *
 */
public class Semester extends SemesterAbstract implements Serializable {
	
	private List<SubjectValue> subjectValuesList;
	private SemesterValue semesterValue;
	
	public Semester(){
		subjectValuesList= new ArrayList<SubjectValue>();
	}
	
	public Semester(SemesterValue semesterValue){
        subjectValuesList= new ArrayList<SubjectValue>();
        this.semesterValue=semesterValue;
    }
	
	public List<SubjectValue> getSubjects(){
        return this.subjectValuesList;
    }
	
	public SemesterValue getSemesterValue() {
		return semesterValue;
	}
	
	public void setSemesterValue(SemesterValue semesterValue) {
		this.semesterValue = semesterValue;
	}

	public void setSubjects(List<SubjectValue> subjects) {
		this.subjectValuesList = subjects;
	}

	public void deleteSubject(SubjectValue subjectValues) {
		if(subjectValuesList.contains(subjectValues)==true){
			subjectValuesList.remove(subjectValues);
		}
	}

	public void addSubject(SubjectValue subjectValues) {
		if(subjectValues != null){
			subjectValuesList.add(subjectValues);
		}
	}
    
}
