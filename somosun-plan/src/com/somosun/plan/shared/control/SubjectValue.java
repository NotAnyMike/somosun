package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.controlAbstract.SubjectValueAbstract;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Mike
 */
public class SubjectValue extends SubjectValueAbstract implements Serializable {

	private Group group = null;
	private ComplementaryValue complementaryValue = null;
	
    public SubjectValue(){
    	setComplementaryValue(new ComplementaryValue());
    }
    
    public SubjectValue(Group group, double grade,boolean taken, ComplementaryValue complementaryValue) {
    	setGroup(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(complementaryValue);
    }
    
    public SubjectValue(double grade,boolean taken, ComplementaryValue complementaryValue) {
    	setGroup(null);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(complementaryValue);
    }
    
    public SubjectValue(Group group, double grade,boolean taken) {
    	setGroup(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(new ComplementaryValue());
    }

    public Group getGroup() {
    	return group;
    }
    
    public void setGroup(Group group) {
    	this.group = group;
    }
    
    public ComplementaryValue getComplementaryValue() {
    	return complementaryValue;
    }
    
    public void setComplementaryValue(ComplementaryValue complementaryValue) {
    	this.complementaryValue = complementaryValue;
    }
}
