package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class SubjectValues implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index Group group;
    double grade;
    int timesSeen;
    String typology;
    
    public SubjectValues(){
    }

    public SubjectValues(Group group, double grade,int timesSeen,String type) {
        this.group = group;
        this.grade = grade;
        this.timesSeen=timesSeen;
        this.typology=type;
    }
}
