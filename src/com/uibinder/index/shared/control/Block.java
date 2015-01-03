package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 * Will this class be repeated? Will it be more than 1 equal block? I think not but it is supposed to
 *  
 * @author Cesar A. Villamizar C.
 */
@Entity
public class Block implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private int starHour;
	@Index private int endHour;
	@Index private int day;
	private String classRoom;
	
	public Block(){
	}

	public Long getId() {
		return id;
	}
	
	public int getStarHour() {
		return starHour;
	}

	public void setStarHour(int starHour) {
		this.starHour = starHour;
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

	public void setDay(int day) {
		this.day = day;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
    
}
