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
    
    public Career(){
    }

    public Career(String code, String name) {
        this.code = code;
        this.name = name;
    }

	public String getCode() {
		return code;
	}

}
