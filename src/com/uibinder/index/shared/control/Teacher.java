package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class Teacher extends UserSun implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Teacher(){
	}
	
	public Teacher(String name, String username, String email, String id) {
        super(name, username, email, id);
    }
    
}
