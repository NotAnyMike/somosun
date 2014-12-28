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
    
}
