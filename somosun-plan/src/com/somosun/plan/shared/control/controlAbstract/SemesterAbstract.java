package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SubjectValue;

public abstract class SemesterAbstract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
    
    abstract public List<SubjectValue> getSubjects();
    abstract public SemesterValue getSemesterValue();
    abstract public void setSemesterValue(SemesterValue semesterValue);
//    abstract public void setSubjects(List<SubjectValue> subjects);
//    abstract public void deleteSubject(SubjectValue subjectValues);
//    abstract public void addSubject(SubjectValue subjectValues);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
