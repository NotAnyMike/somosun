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
	private List<Subject> subjects;
	private String date;
    
    public Semester(String date){
        subjects= new ArrayList<>();
        this.date=date;
    }
    
    public List<Subject> getSubjects(){
        return this.subjects;
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

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
    
}
