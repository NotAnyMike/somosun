package com.uibinder.index.server.control;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Subject {

    private int credits;
    private String code;    
    private String name;
    
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
   