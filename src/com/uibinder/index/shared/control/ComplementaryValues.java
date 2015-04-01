package com.uibinder.index.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * This class holds the complementary information about a subject based on a career
 * @author Mike
 *
 */
@Entity
public class ComplementaryValues implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private Career career = null;;
	@Index private Subject subject = null;
	private List<Subject> listPrerequisites = null;
	private List<Subject> listPosrequisites = null;
	private List<Subject> listCorequisites = null;
	@Index private String typology = null;
    @Index private boolean obligatoriness = false;
    
    /**
     * Do NOT use this method
     */
    public ComplementaryValues(){
    	this.listPrerequisites = new ArrayList<Subject>();
		this.listPosrequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
    }

	public ComplementaryValues(Career career, Subject subject,	List<Subject> listPrerequisites, List<Subject> listPosrequisites, List<Subject> listCorequisites, String typology,	boolean obligatoriness) {
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = listPrerequisites;
		this.listPosrequisites = listPosrequisites;
		this.listCorequisites = listCorequisites;
		this.typology = typology;
		this.obligatoriness = obligatoriness;
	}

	public ComplementaryValues(Career career, Subject subject, String typology,	boolean obligatoriness) {
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = new ArrayList<Subject>();
		this.listPosrequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.typology = typology;
		this.obligatoriness = obligatoriness;
	}
	
	public ComplementaryValues(Career career, Subject subject) {
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = new ArrayList<Subject>();
		this.listPosrequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
	}

	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public void addPrerequisite(Subject subject){
		if(listPrerequisites == null) listPrerequisites = new ArrayList<Subject>();
		if(listPrerequisites.contains(subject) == false){
			listPrerequisites.add(subject);
		}
	}
	
	public void removePrerequisite(Subject subject){
		if(listPrerequisites.contains(subject) == true){
			listPrerequisites.remove(subject);
		}
	}
	
	public void addPosrequisite(Subject subject){
		if(listPosrequisites == null) listPosrequisites = new ArrayList<Subject>();
		if(listPosrequisites.contains(subject) == false){
			listPosrequisites.add(subject);
		}
	}
	
	public void removePosrequisite(Subject subject){
		if(listPosrequisites.contains(subject) == true){
			listPosrequisites.remove(subject);
		}
	}
	
	public void addCorequisite(Subject subject){
		if(listCorequisites == null) listCorequisites = new ArrayList<Subject>();
		if(listCorequisites.contains(subject) == false){
			listCorequisites.add(subject);
		}
	}
	
	public void removeCorequisite(Subject subject){
		if(listCorequisites.contains(subject) == true){
			listCorequisites.remove(subject);
		}
	}

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}

	public boolean isObligatoriness() {
		return obligatoriness;
	}

	public void setObligatoriness(boolean obligatoriness) {
		this.obligatoriness = obligatoriness;
	}

	public List<Subject> getListCorequisites() {
		return listCorequisites;
	}

	public void setListCorequisites(List<Subject> listCorequisites) {
		this.listCorequisites = listCorequisites;
	}

	public List<Subject> getListPrerequisites() {
		return listPrerequisites;
	}

	public void setListPrerequisites(List<Subject> listPrerequisites) {
		this.listPrerequisites = listPrerequisites;
	}

	public List<Subject> getListPosrequisites() {
		return listPosrequisites;
	}

	public void setListPosrequisites(List<Subject> listPosrequisites) {
		this.listPosrequisites = listPosrequisites;
	}

}
