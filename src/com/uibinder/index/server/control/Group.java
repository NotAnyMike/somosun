package com.uibinder.index.server.control;

import java.util.List;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Group {
    Subject subject;
    int groupNumber;
    Teacher teacher;
    int freePlaces;
    int totalPlaces;
    List<Block> schedule;
    Semester semester;

    public Group(Subject subject, int groupNumber, Teacher teacher) {
        this.subject = subject;
        this.groupNumber = groupNumber;
        this.teacher = teacher;
    }
    
    
    
}
