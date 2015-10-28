package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

/**
 * This class holds the complementary information about a subject based on a career
 * @author Mike
 *
 */
@Cache(expirationSeconds=9000)
@Entity
public class ComplementaryValue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private Career career = null;;
	@Index private Subject subject = null;
	
	@Serialize private List<List<Subject>> prerequisitesLists = null;
	@Serialize private List<List<Subject>> corequisitesLists = null;
	private List<Subject> listPrerequisitesOf = null;
	private List<Subject> listCorequisitesOf = null;
	
	@Index private String typology = null;
    @Index private boolean mandatory = false;
    @Index private SubjectGroup subjectGroup = null;
    
    /**
     * Do NOT use this method
     */
    public ComplementaryValue(){
    	
    	this.prerequisitesLists = new ArrayList<List<Subject>>();
		this.corequisitesLists = new ArrayList<List<Subject>>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
    }

	public ComplementaryValue(Career career, Subject subject,	List<List<Subject>> listPrerequisites, List<List<Subject>> listCorequisites, String typology,	boolean mandatory) {
		
		this.career = career;
		this.subject = subject;
		this.prerequisitesLists = listPrerequisites;
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		this.corequisitesLists = listCorequisites;
		this.setTypology(typology);
		this.mandatory = mandatory;
	}
	
	public ComplementaryValue(Career career, Subject subject,	List<List<Subject>> listPrerequisites, List<List<Subject>> listCorequisites, String typology,	boolean mandatory, SubjectGroup subjectGroup) {
		
		this.career = career;
		this.subject = subject;
		this.prerequisitesLists = listPrerequisites;
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		this.corequisitesLists = listCorequisites;
		this.setTypology(typology);
		this.mandatory = mandatory;
		this.subjectGroup = subjectGroup;
	}

	public ComplementaryValue(Career career, Subject subject, String typology,	boolean mandatory) {
		
		this.career = career;
		this.subject = subject;
		
		this.prerequisitesLists = new ArrayList<List<Subject>>();
		this.corequisitesLists = new ArrayList<List<Subject>>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		
		this.setTypology(typology);
		this.mandatory = mandatory;
	}
	
	public ComplementaryValue(Career career, Subject subject,	String typology, boolean mandatory, SubjectGroup subjectGroup) {
		
		this.career = career;
		this.subject = subject;
		
		this.prerequisitesLists = new ArrayList<List<Subject>>();
		this.corequisitesLists = new ArrayList<List<Subject>>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
		
		this.setTypology(typology);
		this.mandatory = mandatory;
		this.subjectGroup = subjectGroup;
	}
	
	public ComplementaryValue(Career career, Subject subject) {
		
		this.career = career;
		this.subject = subject;
		
		this.prerequisitesLists = new ArrayList<List<Subject>>();
		this.corequisitesLists = new ArrayList<List<Subject>>();
		this.listPrerequisitesOf = new ArrayList<Subject>();
		this.listCorequisitesOf = new ArrayList<Subject>();
	}
	
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
	
	/**
	 * This method will all the lists in @param lists to the prerequisitesLists
	 * @param lists
	 */
	public void addPrerequisites(List<List<Subject>> lists){
		if(lists != null){
			if(prerequisitesLists == null) prerequisitesLists = new ArrayList<List<Subject>>();
			prerequisitesLists.addAll(lists);
		}
	}
	
	/**
	 * This will take the @param subject as an AND prerequisite, therefore will create a new list and add it to the lists
	 * @param subject
	 */
	public void addPrerequisite(Subject subject){
		if(subject != null){			
			if(prerequisitesLists == null) prerequisitesLists = new ArrayList<List<Subject>>();
			boolean containsSubject = containsSubject(prerequisitesLists, subject);
			if(containsSubject == false){
				List<Subject> list = new ArrayList<Subject>();
				list.add(subject);
				prerequisitesLists.add(list);
			}
		}
	}
	
	public void removePrerequisite(Subject subject){
		if(prerequisitesLists.contains(subject) == true){
			prerequisitesLists.remove(subject);
		}
	}
	
	
	public void addPrerequisiteOf(Subject subject){
		if(listPrerequisitesOf == null) {
			listPrerequisitesOf = new ArrayList<Subject>();
		}
		boolean containsSubject = containSubject(listPrerequisitesOf, subject);
		if(containsSubject == false){
			listPrerequisitesOf.add(subject);
		}
	}

	public void removePrerequisiteOf(Subject subject){
		if(listPrerequisitesOf.contains(subject) == true){
			listPrerequisitesOf.remove(subject);
		}
	}
	
	/**
	 * This method will add the @param subject as an AND corequisite
	 * @param subject
	 */
	public void addCorequisite(Subject subject){
		if(corequisitesLists == null) corequisitesLists = new ArrayList<List<Subject>>();
		boolean containsSubject = containsSubject(corequisitesLists, subject);
		if(containsSubject == false){
			List<Subject> list = new ArrayList<Subject>();
			list.add(subject);
			corequisitesLists.add(list);
		}
	}
	
	/**
	 * This method will add all the lists in @param lists to the correquisiteLists
	 * @param lists
	 */
	public void addCorequisites(List<List<Subject>> lists){
		if(corequisitesLists == null) corequisitesLists = new ArrayList<List<Subject>>();
		corequisitesLists.addAll(lists);
	}
	
	public void removeCorequisite(Subject subject){
		if(corequisitesLists.contains(subject) == true){
			corequisitesLists.remove(subject);
		}
	}
	
	public void addCorequisiteOf(Subject subject){
		if(listCorequisitesOf == null) listCorequisitesOf = new ArrayList<Subject>();
		boolean containsSubject = containSubject(listCorequisitesOf, subject);
		if(containsSubject == false){
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

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * this method will return one list with every corequisite there, without differencing the AND and OR correquisites
	 * @return
	 */
	public List<Subject> getListCorequisites() {
		List<Subject> toReturn = new ArrayList<Subject>();
		for(List<Subject> list : corequisitesLists){
			toReturn.addAll(list);
		}
		return toReturn;
	}

	public void setListCorequisites(List<List<Subject>> corequisitesLists) {
		this.corequisitesLists = corequisitesLists;
	}
	
	/**
	 * Old lists are not lost
	 * @param list
	 */
	public void addListToCorequisites(List<Subject> list) {
		if(corequisitesLists == null){
			corequisitesLists = new ArrayList<List<Subject>>();
		}
		List<Subject> realList = new ArrayList<Subject>();
		for(Subject s : list){
			if(containsSubject(getCorequisitesLists(), s) == false){
				realList.add(s);
			}
		}
		if(realList.isEmpty() == false)
			corequisitesLists.add(realList);
	}
	
	public List<Subject> getListCorequisitesOf() {
		return listCorequisitesOf;
	}
	
	public void setListCorequisitesOf(List<Subject> listCorequisitesOf) {
		this.listCorequisitesOf = listCorequisitesOf;
	}

	public List<Subject> getListPrerequisites() {
		List<Subject> toReturn = new ArrayList<Subject>();
		for(List<Subject> list : prerequisitesLists){
			toReturn.addAll(list);
		}
		return toReturn;
	}

	public void setListPrerequisites(List<List<Subject>> prerequisitesLists) {
		this.prerequisitesLists = prerequisitesLists;
	}
	
	/**
	 * This method doest not delete the old lists in the prerequisites list, just add the @param list to that list
	 * @param list
	 */
	public void addListToPrerequisites(List<Subject> list) {
		if(prerequisitesLists == null){
			prerequisitesLists = new ArrayList<List<Subject>>();
		}
		List<Subject> realList = new ArrayList<Subject>();
		for(Subject s : list){
			if(containsSubject(getPrerequisitesLists(), s) == false){
				realList.add(s);
			}
		}
		if(realList.isEmpty() == false)
			prerequisitesLists.add(list);
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
	
	private boolean containsSubject(List<List<Subject>> lists, Subject subject) {
		boolean toReturn = false;
		
		for(List<Subject> list : lists){			
			toReturn = containSubject(list, subject);
			if(toReturn == true){
				break;
			}
		}
		
		return toReturn;
	}
	
	private boolean containSubject(List<Subject> list, Subject subject){
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

	public List<List<Subject>> getCorequisitesLists() {
		return corequisitesLists;
	}

	public List<List<Subject>> getPrerequisitesLists() {
		return prerequisitesLists;
	}
	
	public String getPrerequisitesString(){
		String toReturn = null;
		
		for(List<Subject> list : prerequisitesLists){
			if(toReturn != null) toReturn = toReturn.concat("; y ");
			
			for(Subject subject : list){
				if(toReturn == null){
					toReturn = "";
				}else{
					toReturn = toReturn.concat("o");					
				}
				toReturn = toReturn.concat(subject.getName() + " ");
			}
		}
		
		return toReturn;
	}
	
	public String getCorequisitesString(){
		String toReturn = null;
		
		for(List<Subject> list : corequisitesLists){
			if(toReturn != null) toReturn = toReturn.concat("; y ");
			
			for(Subject subject : list){
				if(toReturn == null){
					toReturn = "";
				}else{
					toReturn = toReturn.concat("o");					
				}
				toReturn = toReturn.concat(subject.getName() + " ");
			}
		}
		
		return toReturn;
	}

}
