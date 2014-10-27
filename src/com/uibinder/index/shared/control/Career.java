package com.uibinder.index.shared.control;

import java.io.Serializable;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Career implements Serializable {

    String code;
    String name;

    public Career(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
