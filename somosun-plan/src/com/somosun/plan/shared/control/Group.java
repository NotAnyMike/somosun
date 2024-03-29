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

/**
 *
 * @author Mike
 */
@Entity
public class Group implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id=null;
    @Index private Subject subject = null;
    @Index private Teacher teacher = null;
    @Index private SemesterValue semesterValue=null;
    @Index private Integer groupNumber;
    @Index private int freePlaces;
    private int totalPlaces;
    private List<Block> schedule=null;
    private List<Career> careers = null;
    @Index private Double averageGrade = null; 

    public Group(){
    }
    
    public Group(Subject subject, Teacher teacher, SemesterValue semester, int freePlaces, int groupNumber, int totalPlaces, List<Block> schedule, List<Career> careers) {
        this.subject = subject;
        this.teacher = teacher;
        this.semesterValue = semester;
        this.freePlaces = freePlaces;
        this.groupNumber = groupNumber;
        this.totalPlaces = totalPlaces;
        this.schedule = schedule;
        this.setCareers(careers);
    }
    
    public Group(Subject subject, SemesterValue semesterValue, Integer groupInt) {
    	this.subject = subject;
        this.semesterValue = semesterValue;
        this.groupNumber = groupInt;
        setCareers(new ArrayList<Career>());
	}

	/**
     * Two groups will be the same group if the subject,
     * teacher, semester, freePLaces, groupNumber, totalPlaces and schedule are the same
     * 
     * @param group
     * @return
     */
    public boolean equals(Group group){
    	boolean toReturn = false;
    	boolean blockEquals = false;
    	if
    	((this.getSubject() == null && group.getSubject() == null) || (this.getSubject() != null && group.getSubject() != null && this.getSubject().equals(group.getSubject())) && 
    			((this.getTeacher() == null && group.getTeacher() == null) || (this.getTeacher() != null && group.getTeacher()!= null && this.getTeacher().equals(group.getTeacher()))) &&
    			((this.getSemesterValue() == null && group.getSemesterValue() == null) || (this.getSemesterValue() != null && group.getSemesterValue() != null && this.getSemesterValue().equals(group.getSemesterValue()))) && 
    			this.getFreePlaces() == group.getFreePlaces() &&
    			this.getGroupNumber() == group.getGroupNumber() && 
    			this.getTotalPlaces() == group.getTotalPlaces()
    			){
    		
    		if(this.getSchedule().size() == group.getSchedule().size()){
				for(Block b : this.getSchedule()){
					blockEquals = false;
					for(Block b2 : group.getSchedule()){
						if(b.equals(b2)) {
							blockEquals = true;
							break;
						}      				
					}
					if(blockEquals == false){
						break;
					}
				}
				if(blockEquals == true){
					toReturn = true;    			
				} else {
					toReturn = false;
				}
    		}
    	}
    	return toReturn;
    }

	public Long getId() {
		return id;
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

	public int getFreePlaces() {
		return freePlaces;
	}

	public void setFreePlaces(int freePlaces) {
		this.freePlaces = freePlaces;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public int getTotalPlaces() {
		return totalPlaces;
	}

	public void setTotalPlaces(int totalPlaces) {
		this.totalPlaces = totalPlaces;
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

	public void setId(Long id) {
		this.id = id;
	}

	
	public void addCareer(Career career) {
		if(career != null){
			getCareers().add(career);
		}
	}

	public boolean containsCareer(String code) {
		boolean toReturn = false;
		
		if(getCareers() != null){			
			for(Career c : getCareers()){
				if(c != null && code != null && c.getCode().equals(code)){
					toReturn = true;
					break;
				}
			}
		}
		return toReturn;
	}

	/**
	 * Be carefull with this, because if the list careers is not empty then it can be deleted 
	 * @param careers
	 */
	private void setCareers(List<Career> careers) {
		this.careers = careers;
	}

	public Double getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(Double grade) {
		this.averageGrade = grade;
	}

}
