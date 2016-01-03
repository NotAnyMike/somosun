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
	
	private boolean compareSchedules(List<Block> list){
		boolean toReturn = false;
		
		if(this.getSchedule() == null && list == null){
			toReturn = true;
		}else if(this.getSchedule() != null && list != null && list.size() == this.getSchedule().size()){
			boolean areEqual = true;
			for(int x = 0; x < this.getSchedule().size(); x++){
				if(this.getSchedule().get(x).getId().equals(list.get(x).getId()) == false){
					areEqual = false;
					break;
				}
			}
			if(areEqual == true) toReturn = true;
		}
		
		return toReturn;
	}
	
	private boolean compareCareers(List<Career> list){
		boolean toReturn = false;
		
		if(this.getCareers() == null && list == null){
			toReturn = true;
		}else if(this.getCareers() != null && list != null && list.size() == this.getSchedule().size()){
			boolean areEqual = true;
			for(int x = 0; x < this.getSchedule().size(); x++){
				if(this.getCareers().get(x).getId().equals(list.get(x).getId()) == false){
					areEqual = false;
					break;
				}
			}
			if(areEqual == true) toReturn = true;
		}
		
		return toReturn;
	}

	public boolean compare(GroupAbstract g){
		boolean toReturn = false;
		
		if(this.getId() != null && g.getId() != null && this.getId().equals(g.getId()) && this.getGroupNumber() == g.getGroupNumber() && this.getFreePlaces() == g.getFreePlaces() &&
				this.getTotalPlaces() == g.getTotalPlaces() && 
				((this.getAverageGrade() == null && g.getAverageGrade() == null) || (this.getAverageGrade() != null && g.getAverageGrade() != null && this.getAverageGrade().equals(g.getAverageGrade())))){
			//Subject, Teacher, semesterValue, schedule, careers	
			if((this.getSubject() == null && g.getSubject() == null) || 
					(this.getSubject() != null && g.getSubject() != null && this.getSubject().getId().equals(g.getSubject().getId()) == true)){
				if((this.getTeacher() == null && g.getTeacher() == null) || 
						(this.getTeacher() != null && g.getTeacher() != null && this.getTeacher().getIdSun().equals(g.getTeacher().getIdSun()) == true)){
					if((this.getSemesterValue() == null && g.getSemesterValue() == null) || 
							(this.getSemesterValue() != null && g.getSemesterValue() != null && this.getSemesterValue().getId().equals(g.getSemesterValue().getId()) == true)){
						//check schedule and careers
						if(compareSchedules(g.getSchedule()) == true){
							if(compareCareers(g.getCareers()) == true){
								toReturn = true;
							}
						}
						
					}
				}
			}
		}
		
		return toReturn;
	}

}
