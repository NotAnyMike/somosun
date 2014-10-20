package com.uibinder.index.shared.control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Semester {
    List<Subject> subjects;
    String date;
    
    public Semester(String date){
        subjects= new ArrayList<>();
        this.date=date;
    }
    
    public List<Subject> getSubjects(){
        return this.subjects;
    }
    
}
