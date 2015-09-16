package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class Semester implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	private List<SubjectValue> subjectValuesList;
	private SemesterValue semesterValue;
	
	public Semester(){
		subjectValuesList= new ArrayList<>();
	}
    
    public Semester(SemesterValue semesterValue){
        subjectValuesList= new ArrayList<>();
        this.semesterValue=semesterValue;
    }
    
    public List<SubjectValue> getSubjects(){
        return this.subjectValuesList;
    }

	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}
    
}
