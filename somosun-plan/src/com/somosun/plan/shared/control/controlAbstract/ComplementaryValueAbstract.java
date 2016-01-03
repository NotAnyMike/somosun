package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;

public abstract class ComplementaryValueAbstract implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private String typology = null;
    @Index private boolean mandatory = false;
    
    abstract public Career getCareer();
    abstract public void setCareer(Career career);
    abstract public Subject getSubject();
    abstract public void setSubject(Subject subject);
    abstract public void addPrerequisites(List<List<Subject>> lists);
    abstract public void addPrerequisite(Subject subject);
    abstract public void removePrerequisite(Subject subject);
    abstract public void addPrerequisiteOf(Subject subject);
    abstract public void removePrerequisiteOf(Subject subject);
    abstract public void addCorequisite(Subject subject);
    abstract public void addCorequisites(List<List<Subject>> lists);
    abstract public void removeCorequisite(Subject subject);
    abstract public void addCorequisiteOf(Subject subject);
    abstract public void removeCorequisiteOf(Subject subject);
    abstract public List<Subject> getListCorequisites();
    abstract public void setListCorequisites(List<List<Subject>> corequisitesLists);
    abstract public void addListToCorequisites(List<Subject> list);
    abstract public List<Subject> getListCorequisitesOf();
    abstract public void setListCorequisitesOf(List<Subject> listCorequisitesOf);
    abstract public List<Subject> getListPrerequisites();
    abstract public void setListPrerequisites(List<List<Subject>> prerequisitesLists);
    abstract public void addListToPrerequisites(List<Subject> list);
	abstract public List<Subject> getListPrerequisitesOf();
	abstract public void setListPrerequisitesOf(List<Subject> listPosrequisitesOf);
	abstract public SubjectGroup getSubjectGroup();
//	abstract public void setSubjectGroup(SubjectGroup subjectGroup);
	abstract public List<List<Subject>> getCorequisitesLists();
	abstract public List<List<Subject>> getPrerequisitesLists();
	abstract public String getPrerequisitesString();
	abstract public String getCorequisitesString();
//	abstract public boolean containsSubject(List<List<Subject>> lists, Long subjectId);

	
	/**
	 * @param cV
	 * @return true if career, subjectGroup, subject, mandatory, and the four lists are the exact sames 
	 */
	public boolean equals(ComplementaryValue cV){
		
		boolean toReturn = false;
		
		if(this.getCareer().equals(cV.getCareer()) && this.getSubjectGroup().equals(cV.getCareer())
				&& this.getSubject().equals(cV.getSubject()) && this.isMandatory() == cV.isMandatory()
				&& this.getListCorequisites().equals(cV.getListCorequisites()) && this.getListCorequisitesOf().equals(cV.getListCorequisitesOf())
				&& this.getListPrerequisites().equals(cV.getListPrerequisites()) && this.getListPrerequisitesOf().equals(cV.getListPrerequisitesOf()))
			toReturn = true;
		
		return toReturn;
	}

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	protected boolean containsSubjectInList(List<Subject> list, Subject subject){
		boolean toReturn = false;
		
		for(Subject s : list){
			if(s.getCode().isEmpty() == false){
				if(s.getCode().equals(subject.getCode()) == true){
					toReturn = true;
					break;
				}
			}else{
				if(s.getName().equals(subject.getName()) == true){
					toReturn = true;
					break;
				}
			}
		}
		
		return toReturn;
	}	
}
