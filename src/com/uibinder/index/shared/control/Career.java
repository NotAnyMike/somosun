package com.uibinder.index.shared.control;

import java.io.Serializable;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Career implements Serializable {

    private String code;
    private String name;

    public Career(String code, String name) {
        this.code = code;
        this.name = name;
    }

	public String getCode() {
		return code;
	}

}
