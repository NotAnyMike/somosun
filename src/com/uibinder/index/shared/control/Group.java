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
    
    
    
}
