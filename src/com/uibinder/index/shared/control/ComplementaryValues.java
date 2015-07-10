package com.uibinder.index.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
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
    @Index private boolean mandatory = false;
    @Index private SubjectGroup subjectGroup = null;
    
    /**
     * Do NOT use this method
     */
    public ComplementaryValues(){
    	
    	this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
    }

	public ComplementaryValues(Career career, Subject subject,	List<Subject> listPrerequisites, List<Subject> listCorequisites, String typology,	boolean mandatory) {
		
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = listPrerequisites;
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		this.listCorequisites = listCorequisites;
		this.setTypology(typology);
		this.mandatory = mandatory;
	}
	
	public ComplementaryValues(Career career, Subject subject,	List<Subject> listPrerequisites, List<Subject> listCorequisites, String typology,	boolean mandatory, SubjectGroup subjectGroup) {
		
		this.career = career;
		this.subject = subject;
		this.listPrerequisites = listPrerequisites;
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		this.listCorequisites = listCorequisites;
		this.setTypology(typology);
		this.mandatory = mandatory;
		this.subjectGroup = subjectGroup;
	}

	public ComplementaryValues(Career career, Subject subject, String typology,	boolean mandatory) {
		
		this.career = career;
		this.subject = subject;
		
		this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		
		this.setTypology(typology);
		this.mandatory = mandatory;
	}
	
	public ComplementaryValues(Career career, Subject subject,	String typology, boolean mandatory, SubjectGroup subjectGroup) {
		
		this.career = career;
		this.subject = subject;
		
		this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		
		this.setTypology(typology);
		this.mandatory = mandatory;
		this.subjectGroup = subjectGroup;
	}
	
	public ComplementaryValues(Career career, Subject subject) {
		
		this.career = career;
		this.subject = subject;
		
		this.listPrerequisites = new ArrayList<Subject>();
		this.listCorequisites = new ArrayList<Subject>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
	}
	
	/**
	 * @param cV
	 * @return true if career, subjectGroup, subject, mandatory, and the four lists are the exact sames 
	 */
	public boolean equals(ComplementaryValues cV){
		
		boolean toReturn = false;
		
		if(this.getCareer().equals(cV.getCareer()) && this.getSubjectGroup().equals(cV.getCareer())
				&& this.getSubject().equals(cV.getSubject()) && this.isMandatory() == cV.isMandatory()
				&& this.getListCorequisites().equals(cV.getListCorequisites()) && this.getListCorequisitesOf().equals(cV.getListCorequisitesOf())
				&& this.getListPrerequisites().equals(cV.getListPrerequisites()) && this.getListPrerequisitesOf().equals(cV.getListPrerequisitesOf()))
			toReturn = true;
		
		return toReturn;
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
		switch(typology){
			case "n":
				typology = "b"; //nivelacion
				break;
		}
		this.typology = typology;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
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

	public SubjectGroup getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
