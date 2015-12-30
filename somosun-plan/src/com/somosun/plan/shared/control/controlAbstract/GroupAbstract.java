package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;

public abstract class GroupAbstract implements Serializable{

	@Id private Long id=null;
    @Index private Integer groupNumber;
    @Index private int freePlaces;
    private int totalPlaces;
    @Index private Double averageGrade = null; 

    abstract public Subject getSubject();
	abstract public void setSubject(Subject subject);
	abstract public Teacher getTeacher();
	abstract public void setTeacher(Teacher teacher);
	abstract public SemesterValue getSemesterValue();
	abstract public void setSemesterValue(SemesterValue semester);
	abstract public List<Block> getSchedule();
	abstract public List<Career> getCareers();
	abstract public void setSchedule(List<Block> schedule);
	abstract protected void setCareers(List<Career> careers);
	abstract public void addCareer(Career career);
    
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

	public void setId(Long id) {
		this.id = id;
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

	public Double getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(Double grade) {
		this.averageGrade = grade;
	}
	
}
