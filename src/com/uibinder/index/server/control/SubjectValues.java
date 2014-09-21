package com.uibinder.index.server.control;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class SubjectValues {
    Group group;
    double grade;
    int timesSeen;
    String typology;

    public SubjectValues(Group group, double grade,int timesSeen,String type) {
        this.group = group;
        this.grade = grade;
        this.timesSeen=timesSeen;
        this.typology=type;
    }
    
}
