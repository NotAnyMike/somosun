package com.uibinder.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 * This class will be the most used, so none of its parameters will be index to avoid work indexing it
 *  
 * @author Mike
 */
@Cache(expirationSeconds=9000)
@Entity
public class Block implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	private int startHour;
	private int endHour;
	private int day;
	private String classRoom;
	
	public Block(){
	}
	
	/**
	 * 
	 * @param startHour
	 * @param endHour
	 * @param day: L=0, M=1 ...
	 * @param classRoom
	 */
	public Block(int startHour, int endHour, int day, String classRoom){
		this.startHour = startHour;
		this.endHour = endHour;
		this.day = day;
		this.classRoom = classRoom;
	}
	
	public boolean equals(Block b){
		if(this.getClassRoom() == b.getClassRoom() && this.getDay() == b.getDay() && this.getEndHour() == b.getEndHour() && this.getStartHour() == this.getStartHour()){
			return true;
		} else {
			return false;
		}
	}

	public Long getId() {
		return id;
	}
	
	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int starHour) {
		this.startHour = starHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getDay() {
		return day;
	}

	/**
	 * 
	 * @param day: L=0, M=1 ...
	 */
	public void setDay(int day) {
		this.day = day;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}
