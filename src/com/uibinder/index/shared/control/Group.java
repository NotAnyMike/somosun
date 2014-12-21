package com.uibinder.index.shared.control;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Group implements Serializable {
    private Subject subject;
    private int groupNumber;
    private Teacher teacher;
    private int freePlaces;
    private int totalPlaces;
    private List<Block> schedule;
    private Semester semester;

    public Group(Subject subject, int groupNumber, Teacher teacher) {
        this.subject = subject;
        this.groupNumber = groupNumber;
        this.teacher = teacher;
    }
    
    
    
}
