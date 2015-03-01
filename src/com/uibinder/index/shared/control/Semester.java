package com.uibinder.index.shared.control;

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
	@Id private Long id=null;
	private List<SubjectValues> subjectValuesList;
	private String date;
	
	public Semester(){
	}
    
    public Semester(String date){
        subjectValuesList= new ArrayList<>();
        this.date=date;
    }
    
    public List<SubjectValues> getSubjects(){
        return this.subjectValuesList;
    }

	public Long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSubjects(List<SubjectValues> subjects) {
		this.subjectValuesList = subjects;
	}

	public void deleteSubject(SubjectValues subjectValues) {
		if(subjectValuesList.contains(subjectValues)==true){
			subjectValuesList.remove(subjectValues);
		}
	}
    
}
