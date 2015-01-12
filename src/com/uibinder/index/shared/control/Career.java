package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Career implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id=null;
	@Index private String name;
	@Index private String code;
    @Index private String sede;
    
    public Career(){
    }

    /**
     * 
     * @param code
     * @param name
     * @param sede: "ama", "bog", "car", "man", "med", "ori", "pal", "tum" or "" in that case will be "bog"
     */
    public Career(String name, String code, String sede) {
    	
    	if(sede != "ama" && sede != "bog" && sede != "car" && sede != "man" && sede != "med" && sede != "ori" && sede != "pal" && sede != "tum"){
    		sede = "bog";
    	}
    	
        this.code = code;
        this.name = name;
        this.sede = sede;
    }
    
    public boolean equal(Career career){
    	if(this.getCode() == career.getCode() && this.getSede() == career.getSede() && this.getName() == career.getName()){
    		return true;
    	}else{
    		return false;
    	}
    }

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		if(sede != "ama" && sede != "bog" && sede != "car" && sede != "man" && sede != "med" && sede != "ori" && sede != "pal" && sede != "tum"){
    		sede = "bog";
    	}
		this.sede = sede;
	}

}
