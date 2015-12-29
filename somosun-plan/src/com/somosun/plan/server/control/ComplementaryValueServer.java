package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Serialize;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.controlAbstract.ComplementaryValueAbstract;

/**
 * This class holds the complementary information about a subject based on a career
 * @author Mike
 *
 */
@Cache(expirationSeconds=9000)
@Entity
public class ComplementaryValueServer extends ComplementaryValueAbstract {

	@Index @Load private Ref<Career> career = null;;
	@Index @Load private Ref<Subject> subject = null;
	@Index @Load private Ref<SubjectGroup> subjectGroup = null;
	@Serialize private List<List<Ref<Subject>>> prerequisitesLists = null;
	@Serialize private List<List<Ref<Subject>>> corequisitesLists = null;
	private List<Ref<Subject>> listPrerequisitesOf = null;
	private List<Ref<Subject>> listCorequisitesOf = null;
	
	/**
     * Do NOT use this method
     */
    public ComplementaryValueServer(){
    	
    	this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.corequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
    }

	public ComplementaryValueServer(Career career, Subject subject,	List<List<Subject>> listPrerequisites, List<List<Subject>> listCorequisites, String typology,	boolean mandatory) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		setListPrerequisites(listPrerequisites);
		setListCorequisites(listCorequisites);
		
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
	}
	
	public ComplementaryValueServer(Career career, Subject subject,	List<List<Subject>> listPrerequisites, List<List<Subject>> listCorequisites, String typology,	boolean mandatory, SubjectGroup subjectGroup) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		setSubjectGroup(subjectGroup);
		setListPrerequisites(listPrerequisites);
		setListCorequisites(listCorequisites);
		
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
	}

	public ComplementaryValueServer(Career career, Subject subject, String typology,	boolean mandatory) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		
		this.prerequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.corequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
		
	}
	
	public ComplementaryValueServer(Career career, Subject subject,	String typology, boolean mandatory, SubjectGroup subjectGroup) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		setSubjectGroup(subjectGroup);
		
		this.prerequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.corequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
		
	}
	
	public ComplementaryValueServer(Career career, Subject subject) {
		
		setCareer(career);
		setSubject(subject);
		
		this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.corequisitesLists = new ArrayList<List<Ref<Subject>>>();
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
	}
	
	public void setCareer(Career career){
		if(career != null && career.getId() != null){
			setCareerRef(Ref.create(career));
		}
	}

	public Ref<Career> getCareerRef() {
		return career;
	}
	
	public Career getCareer(){
		Career toReturn = null;
		if(career != null) toReturn = career.get();
		return toReturn;
	}

	public void setCareerRef(Ref<Career> career) {
		this.career = career;
	}

	public Ref<Subject> getSubjectRef() {
		return subject;
	}

	public void setSubjectRef(Ref<Subject> subject) {
		this.subject = subject;
	}
	
	public void setSubject(Subject subject){
		if(subject != null && subject.getId() != null) setSubjectRef(Ref.create(subject));
	}
	
	public Subject getSubject(){
		Subject toReturn = null;
		if(subject != null) toReturn = subject.get();
		return toReturn;
	}

	public Ref<SubjectGroup> getSubjectGroupRef() {
		return subjectGroup;
	}

	public void setSubjectGroupRef(Ref<SubjectGroup> subjectGroup) {
		this.subjectGroup = subjectGroup;
	}
	
	public void setSubjectGroup(SubjectGroup subjectGroup){
		if(subjectGroup != null && subjectGroup.getId() != null) setSubjectGroupRef(Ref.create(subjectGroup));
	}
	
	public SubjectGroup getSubjectGroup(){
		SubjectGroup toReturn = null;
		if(subjectGroup != null) toReturn = subjectGroup.get();
		return toReturn;
	}

	@Override
	public void addPrerequisites(List<List<Subject>> lists) {
		if(lists != null){
			if(prerequisitesLists  == null) prerequisitesLists = new ArrayList<List<Ref<Subject>>>();
			for(List<Subject> list : lists){
				List<Ref<Subject>> listToAdd = new ArrayList<Ref<Subject>>();
				for(Subject s : list){
					if(s.getId() != null && containsSubject(prerequisitesLists, s.getId()) == false){
						listToAdd.add(Ref.create(s));						
					}
				}
				prerequisitesLists.add(listToAdd);
			}
		}
	}

	@Override
	public void addPrerequisite(Subject subject) {
		if(subject != null && subject.getId() != null){			
			if(prerequisitesLists == null) prerequisitesLists = new ArrayList<List<Ref<Subject>>>();
			boolean containsSubject = containsSubject(prerequisitesLists, subject.getId());
			if(containsSubject == false){
				List<Ref<Subject>> list = new ArrayList<Ref<Subject>>();
				list.add(Ref.create(subject));
				prerequisitesLists.add(list);
			}
		}
	}

	@Override
	public void removePrerequisite(Subject subject) {
		if(subject != null && subject.getId() != null){
			for(List<Ref<Subject>> list : prerequisitesLists){
				removeFromList(list, subject.getId());
			}
		}
	}
	
	private void removeFromList(List<Ref<Subject>> list, Long subjectId){
		if(list != null && subjectId != null){			
			List<Ref<Subject>> listToRemove = new ArrayList<Ref<Subject>>();
			for(Ref<Subject> ref : list){
				if(ref.get().getId().equals(subjectId)){
					listToRemove.add(ref);
				}
			}
			if(listToRemove != null) list.removeAll(listToRemove);
		}
	}

	@Override
	public void addPrerequisiteOf(Subject subject) {
		if(subject != null && subject.getId() != null && containsSubjectInList(listPrerequisitesOf, subject.getId()) == false){
			if(listPrerequisitesOf == null) listPrerequisitesOf = new ArrayList<Ref<Subject>>();
			listPrerequisitesOf.add(Ref.create(subject));
		}
	}

	@Override
	public void removePrerequisiteOf(Subject subject) {
		if(subject != null && subject.getId() != null){			
			removeFromList(listPrerequisitesOf, subject.getId());
		}
	}

	@Override
	public void addCorequisite(Subject subject) {
		if(subject != null && subject.getId() != null){			
			if(corequisitesLists == null) corequisitesLists = new ArrayList<List<Ref<Subject>>>();
			boolean containsSubject = containsSubject(corequisitesLists, subject.getId());
			if(containsSubject == false){
				List<Ref<Subject>> list = new ArrayList<Ref<Subject>>();
				list.add(Ref.create(subject));
				corequisitesLists.add(list);
			}
		}
	}

	@Override
	public void addCorequisites(List<List<Subject>> lists) {
		if(lists != null){
			if(corequisitesLists  == null) corequisitesLists = new ArrayList<List<Ref<Subject>>>();
			for(List<Subject> list : lists){
				List<Ref<Subject>> listToAdd = new ArrayList<Ref<Subject>>();
				for(Subject s : list){
					if(s.getId() != null && containsSubject(corequisitesLists, s.getId()) == false){
						listToAdd.add(Ref.create(s));						
					}
				}
				corequisitesLists.add(listToAdd);
			}
		}
	}

	@Override
	public void removeCorequisite(Subject subject) {
		if(subject != null && subject.getId() != null){
			for(List<Ref<Subject>> list : corequisitesLists){
				removeFromList(list, subject.getId());
			}
		}
	}

	@Override
	public void addCorequisiteOf(Subject subject) {
		if(subject != null && subject.getId() != null && containsSubjectInList(listCorequisitesOf, subject.getId()) == false){
			if(listCorequisitesOf == null) listCorequisitesOf = new ArrayList<Ref<Subject>>();
			listCorequisitesOf.add(Ref.create(subject));
		}
	}

	@Override
	public void removeCorequisiteOf(Subject subject) {
		if(subject != null && subject.getId() != null){			
			removeFromList(listCorequisitesOf, subject.getId());
		}
	}

	@Override
	public List<Subject> getListCorequisites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListCorequisites(List<List<Subject>> corequisitesLists) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListToCorequisites(List<Subject> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Subject> getListCorequisitesOf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListCorequisitesOf(List<Subject> listCorequisitesOf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Subject> getListPrerequisites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListPrerequisites(List<List<Subject>> prerequisitesLists) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListToPrerequisites(List<Subject> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Subject> getListPrerequisitesOf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListPrerequisitesOf(List<Subject> listPosrequisitesOf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<List<Subject>> getCorequisitesLists() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Subject>> getPrerequisitesLists() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrerequisitesString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCorequisitesString() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<List<Ref<Subject>>> getPrerequisitesListsRef() {
		return prerequisitesLists;
	}

	public void setPrerequisitesListsRef(List<List<Ref<Subject>>> prerequisitesLists) {
		this.prerequisitesLists = prerequisitesLists;
	}
	
	public boolean containsSubjectInList(List<Ref<Subject>> list, Long subjectId){
		boolean toReturn = false;
		
		if(subjectId != null && list != null){
			for(Ref<Subject> subject : list){
				if(subject.get().getId().equals(subjectId)){
					toReturn = true;
					break;
				}
			}
		}
		
		return toReturn;
	}
	
	private boolean containsSubject(List<List<Ref<Subject>>> lists, Long subjectId){
		boolean toReturn = false;
		
		if(subjectId != null && lists != null){
			for(List<Ref<Subject>> list : lists){
				toReturn = containsSubjectInList(list, subjectId);
				if(toReturn == true) break;
			}
		}
		
		return toReturn;
	}
	
}
