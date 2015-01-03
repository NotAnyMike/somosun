package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class Student extends UserSun implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Plan plan = null;
	
	public Student(){
	}
    
    public Student(String name, String username, String email, String id) {
        super(name, username, email, id);
    }

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
    
}
