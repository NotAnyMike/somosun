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
public class Career implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id=null;
	@Index private String code;
    @Index private String name;
    @Index private String sede;
    
    public Career(){
    }

    public Career(String code, String name) {
        this.code = code;
        this.name = name;
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
		this.sede = sede;
	}

}
