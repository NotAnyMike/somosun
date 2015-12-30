package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.somosun.plan.shared.control.controlAbstract.GroupAbstract;

/**
 *
 * @author Mike
 */
public class Group extends GroupAbstract implements Serializable {
	
	private Subject subject = null;
    private Teacher teacher = null;
    private SemesterValue semesterValue = null;
    private List<Block> schedule = null;
    private List<Career> careers = null;
	
	public Group(){}
	    
	public Group(Subject subject, Teacher teacher, SemesterValue semester, int freePlaces, int groupNumber, int totalPlaces, List<Block> schedule, List<Career> careers) {
        setSubject(subject);
        setTeacher(teacher);
        setSemesterValue(semester);
        setFreePlaces(freePlaces);
        setGroupNumber(groupNumber);
        setTotalPlaces(totalPlaces);
        setSchedule(schedule);
        setCareers(careers);
    }
	    
    public Group(Subject subject, SemesterValue semesterValue, Integer groupInt) {
    	setSubject(subject);
        setSemesterValue(semesterValue);
        setGroupNumber(groupInt);
        setCareers(new ArrayList<Career>());
	}
    
    public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public SemesterValue getSemesterValue() {
		return semesterValue;
	}

	public void setSemesterValue(SemesterValue semester) {
		this.semesterValue = semester;
	}
	
	public List<Block> getSchedule() {
		if(schedule == null) schedule = new ArrayList<Block>();
		return schedule;
	}

	public void setSchedule(List<Block> schedule) {
		this.schedule = schedule;
	}

	public List<Career> getCareers() {
		if(careers == null) careers = new ArrayList<Career>();
		return careers;
	}
	
	/**
	 * Be carefull with this, because if the list careers is not empty then it can be deleted 
	 * @param careers
	 */
	public void setCareers(List<Career> careers) {
		this.careers = careers;
	}
	
	public void addCareer(Career career) {
		if(career != null){
			if(careers == null) careers = new ArrayList<Career>();
			getCareers().add(career);
		}
	}

}
