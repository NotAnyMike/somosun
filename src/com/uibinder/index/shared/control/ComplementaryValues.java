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
	private List<Subject> listCorequisites = null;
	private List<Subject> listPrerequisitesOf = null;
	private List<Subject> listCorequisitesOf = null;
	
	@Index private String typology = null;
    @Index private boolean obligatoriness = false;
    
    /**
     * Do NOT use this method
     */
    public ComplementaryValues(){
    	this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
    }

	public ComplementaryValues(Career career, Subject subject,	List<Subject> listPrerequisites, List<Subject> listCorequisites, String typology,	boolean obligatoriness) {
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = listPrerequisites;
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		this.listCorequisites = listCorequisites;
		this.typology = typology;
		this.obligatoriness = obligatoriness;
	}

	public ComplementaryValues(Career career, Subject subject, String typology,	boolean obligatoriness) {
		this.career = career;
		this.subject = subject;
		
		this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		
		this.typology = typology;
		this.obligatoriness = obligatoriness;
	}
	
	public ComplementaryValues(Career career, Subject subject) {
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
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
	
	public void addPrerequisiteOf(Subject subject){
		if(listPrerequisitesOf == null) listPrerequisitesOf = new ArrayList<Subject>();
		if(listPrerequisitesOf.contains(subject) == false){
			listPrerequisitesOf.add(subject);
		}
	}
	
	public void removePrerequisiteOf(Subject subject){
		if(listPrerequisitesOf.contains(subject) == true){
			listPrerequisitesOf.remove(subject);
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
	
	public void addCorequisiteOf(Subject subject){
		if(listCorequisitesOf == null) listCorequisitesOf = new ArrayList<Subject>();
		if(listCorequisitesOf.contains(subject) == false){
			listCorequisitesOf.add(subject);
		}
	}
	
	public void removeCorequisiteOf(Subject subject){
		if(listCorequisitesOf.contains(subject) == true){
			listCorequisitesOf.remove(subject);
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
	
	public List<Subject> getListCorequisitesOf() {
		return listCorequisitesOf;
	}
	
	public void setListCorequisitesOf(List<Subject> listCorequisitesOf) {
		this.listCorequisitesOf = listCorequisitesOf;
	}

	public List<Subject> getListPrerequisites() {
		return listPrerequisites;
	}

	public void setListPrerequisites(List<Subject> listPrerequisites) {
		this.listPrerequisites = listPrerequisites;
	}

	public List<Subject> getListPrerequisitesOf() {
		return listPrerequisitesOf;
	}

	public void setListPrerequisitesOf(List<Subject> listPosrequisitesOf) {
		this.listPrerequisitesOf = listPosrequisitesOf;
	}

}
