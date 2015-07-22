package com.uibinder.shared.control;

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
	private String sede;

	public Teacher(){
	}
	
	public Teacher(String name, String username, String email, String id, String sede) {
		super(name, username, email, id);
        this.sede = confirmSede(sede);
    }
	
	public Teacher(String name, String username, String email, String sede){
		super(name, username, email);
		this.sede = confirmSede(sede);
	}
	
	public boolean equals(Teacher t){
		if(super.equals(t) == true && this.getSede().equals(t.getSede())) return true;
		else return false;
	}
	
	private String confirmSede(String sede){
		if(sede != "ama" && sede!= "bog" && sede!= "car" && sede!= "man" && sede!= "med" && sede!= "ori" && sede!= "pal" && sede!= "tum"){
			sede = "bog";
		}
		return sede;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}
    
}
