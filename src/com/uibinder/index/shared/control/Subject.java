package com.uibinder.index.shared.control;

import java.io.Serializable;

/**
 *
 * @author Cesar A. Villamizar C.
 * 
 * Mike added the Serializable part to allow server-client communication, hope you don't mind 
 * and moved it to the shared package cuz the client-side will use it
 *  
 */
public class Subject implements Serializable {

    private int credits;
    private String code;    
    private String name;
    
    /**
     * This one is to handle serialization to allow client-server communication (RPC services)
     * The other way to solve it is to create a customFieldSerialization class but it is not as simple
     * or it requires the use of other frameworks 
     * 
     * But the use of this constructor is discourage  
     */
    public Subject(){
    	credits=0;
    	code="";
    	name="empty";
    }
    
    public String getName() {
        return name;
    }
    
    public String getCode(){
        return code;
    }
   
    public int getCredits() {
        return credits;
    }

    public Subject(int credits, String code, String name) {
        this.credits = credits;
        this.code = code;
        this.name = name;
    }
        
}
   