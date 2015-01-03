package com.uibinder.index.shared.control;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class Group implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id=null;
    @Index private Subject subject = null;
    @Index private Teacher teacher=null;
    @Index private Semester semester=null;
    @Index private int freePlaces;
    private int groupNumber;
    private int totalPlaces;
    private List<Block> schedule=null;

    public Group(){
    }
    
    public Group(Subject subject, int groupNumber, Teacher teacher) {
        this.subject = subject;
        this.groupNumber = groupNumber;
        this.teacher = teacher;
    }
    
    /**
     * Two groups will be the same group if the subject,
     * teacher, semester, freePLaces, groupNumber, totalPlaces and schedule are the same
     * 
     * @param group
     * @return
     */
    public boolean equal(Group group){
    	boolean toReturn = false;
    	if(this.subject == group.getSubject() && this.teacher == group.getTeacher() && 
    			this.semester == group.getSemester() && this.freePlaces == group.getFreePlaces() &&
    			this.groupNumber == group.getGroupNumber() && this.totalPlaces == group.getTotalPlaces() &&
    			this.schedule == group.getSchedule()){
    		toReturn = true;
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

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
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
		return schedule;
	}

	public void setSchedule(List<Block> schedule) {
		this.schedule = schedule;
	}
    
    
    
}
