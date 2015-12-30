package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Serialize;
import com.somosun.plan.server.dao.SubjectGroupDao;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
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
	@Index @Load private Ref<SubjectGroupServer> subjectGroup = null;
	@Serialize private List<List<Ref<Subject>>> prerequisitesLists = null;
	@Serialize private List<List<Ref<Subject>>> corequisitesLists = null;
	private List<Ref<Subject>> listPrerequisitesOf = null;
	private List<Ref<Subject>> listCorequisitesOf = null;
	
	/**
     * Do NOT use this method
     */
    public ComplementaryValueServer(){
    	
    	this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
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
	
	public ComplementaryValueServer(Career career, Subject subject,	List<List<Subject>> listPrerequisites, List<List<Subject>> listCorequisites, String typology,	boolean mandatory, SubjectGroupServer subjectGroup) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		setSubjectGroupServer(subjectGroup);
		setListPrerequisites(listPrerequisites);
		setListCorequisites(listCorequisites);
		
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
	}

	public void setSubjectGroupServer(SubjectGroupServer subjectGroup) {
		if(subjectGroup != null && subjectGroup.getId() != null) setSubjectGroupRef(Ref.create(subjectGroup));
	}

	public ComplementaryValueServer(Career career, Subject subject, String typology, boolean mandatory) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		
		this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
		
	}
	
	public ComplementaryValueServer(Career career, Subject subject,	String typology, boolean mandatory, SubjectGroupServer subjectGroup) {
		
		setCareer(career);
		setSubject(subject);
		setTypology(typology);
		setMandatory(mandatory);
		setSubjectGroupServer(subjectGroup);
		
		this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.listPrerequisitesOf = new ArrayList<Ref<Subject>>();
		this.listCorequisitesOf = new ArrayList<Ref<Subject>>();
		
	}
	
	public ComplementaryValueServer(Career career, Subject subject) {
		
		setCareer(career);
		setSubject(subject);
		
		this.setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
		this.setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
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

	public Ref<SubjectGroupServer> getSubjectGroupRef() {
		return subjectGroup;
	}

	public void setSubjectGroupRef(Ref<SubjectGroupServer> subjectGroup) {
		this.subjectGroup = subjectGroup;
	}
	
	public void setSubjectGroup(SubjectGroup subjectGroup){
		if(subjectGroup != null && subjectGroup.getId() != null){
			SubjectGroupDao sGDao = new SubjectGroupDao();
			SubjectGroupServer sGS = sGDao.getById(subjectGroup.getId());
			Ref<SubjectGroupServer> ref = null;
			if(sGS != null) ref = Ref.create(sGS);
			setSubjectGroupRef(ref);
		}
	}
	
	public SubjectGroup getSubjectGroup(){
		SubjectGroup toReturn = null;
		if(subjectGroup != null) toReturn = subjectGroup.get().getClientInstance();
		return toReturn;
	}

	@Override
	public void addPrerequisites(List<List<Subject>> lists) {
		if(lists != null){
			if(getPrerequisitesListsRef()  == null) setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
			for(List<Subject> list : lists){
				List<Ref<Subject>> listToAdd = new ArrayList<Ref<Subject>>();
				for(Subject s : list){
					if(s.getId() != null && containsSubject(getPrerequisitesListsRef(), s.getId()) == false){
						listToAdd.add(Ref.create(s));						
					}
				}
				getPrerequisitesListsRef().add(listToAdd);
			}
		}
	}

	@Override
	public void addPrerequisite(Subject subject) {
		if(subject != null && subject.getId() != null){			
			if(getPrerequisitesListsRef() == null) setPrerequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
			boolean containsSubject = containsSubject(getPrerequisitesListsRef(), subject.getId());
			if(containsSubject == false){
				List<Ref<Subject>> list = new ArrayList<Ref<Subject>>();
				list.add(Ref.create(subject));
				getPrerequisitesListsRef().add(list);
			}
		}
	}

	@Override
	public void removePrerequisite(Subject subject) {
		if(subject != null && subject.getId() != null){
			for(List<Ref<Subject>> list : getPrerequisitesListsRef()){
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
			if(getCorequisitesListsRef() == null) setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
			boolean containsSubject = containsSubject(getCorequisitesListsRef(), subject.getId());
			if(containsSubject == false){
				List<Ref<Subject>> list = new ArrayList<Ref<Subject>>();
				list.add(Ref.create(subject));
				getCorequisitesListsRef().add(list);
			}
		}
	}

	@Override
	public void addCorequisites(List<List<Subject>> lists) {
		if(lists != null){
			if(getCorequisitesListsRef()  == null) setCorequisitesListsRef(new ArrayList<List<Ref<Subject>>>());
			for(List<Subject> list : lists){
				List<Ref<Subject>> listToAdd = new ArrayList<Ref<Subject>>();
				for(Subject s : list){
					if(s.getId() != null && containsSubject(getCorequisitesListsRef(), s.getId()) == false){
						listToAdd.add(Ref.create(s));						
					}
				}
				getCorequisitesListsRef().add(listToAdd);
			}
		}
	}

	@Override
	public void removeCorequisite(Subject subject) {
		if(subject != null && subject.getId() != null){
			for(List<Ref<Subject>> list : getCorequisitesListsRef()){
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
		List<Subject> toReturn = null;
		
		if(getCorequisitesListsRef() != null){
			toReturn = new ArrayList<Subject>();
			for(List<Ref<Subject>> list : getCorequisitesListsRef()){
				for(Ref<Subject> ref : list){
					toReturn.add(ref.get());
				}
			}
			
		}
		
		return toReturn;
	}

	@Override
	public void setListCorequisites(List<List<Subject>> corequisitesLists) {
		if(corequisitesLists != null){
			List<List<Ref<Subject>>> listsToAdd = transformToRefLists(corequisitesLists);
			setCorequisitesListsRef(listsToAdd);
		}
	}

	@Override
	public void addListToCorequisites(List<Subject> list) {
		if(list != null && !list.isEmpty()){
			List<Ref<Subject>> listToAdd = new ArrayList<Ref<Subject>>();
			for(Subject s : list){
				if(containsSubject(corequisitesLists, s.getId()) == false) listToAdd.add(Ref.create(s));
			}
			if(corequisitesLists == null) corequisitesLists = new ArrayList<List<Ref<Subject>>>();
			corequisitesLists.add(listToAdd);
		}
		
	}

	@Override
	public List<Subject> getListCorequisitesOf() {
		List<Subject> listToReturn = null;
		if(listCorequisitesOf != null){
			if(listToReturn == null) listToReturn = new ArrayList<Subject>();
			for(Ref<Subject> ref : listCorequisitesOf){
				listToReturn.add(ref.get());
			}
		}
		return listToReturn;
	}

	@Override
	public void setListCorequisitesOf(List<Subject> listCorequisitesOf) {
		if(listCorequisitesOf != null){	
			List<Ref<Subject>> listToAdd = transformToRefList(listCorequisitesOf);
			if(listToAdd != null) setListCorequisitesOfRef(listToAdd);
		}
	}

	@Override
	public List<Subject> getListPrerequisites() {
		List<Subject> toReturn = null;
		
		if(getCorequisitesListsRef() != null){
			toReturn = new ArrayList<Subject>();
			for(List<Ref<Subject>> list : getPrerequisitesListsRef()){
				for(Ref<Subject> ref : list){
					toReturn.add(ref.get());
				}
			}
			
		}
		
		return toReturn;
	}
	
	private List<List<Ref<Subject>>> transformToRefLists(List<List<Subject>> lists){
		List<List<Ref<Subject>>> listsToReturn = null;
		if(lists != null){
			if(listsToReturn == null) listsToReturn = new ArrayList<List<Ref<Subject>>>();
			for(List<Subject> list : lists){
				
				List<Ref<Subject>> listToAdd = transformToRefList(list);
				listsToReturn.add(listToAdd);
			}
		}
		return listsToReturn;
	}
	
	private List<Ref<Subject>> transformToRefList(List<Subject> list){
		List<Ref<Subject>> listToReturn = null;
		for(Subject s : list){
			if(listToReturn == null) listToReturn = new ArrayList<Ref<Subject>>();
			if(s.getId() != null){
				listToReturn.add(Ref.create(s));
			}
		}
		return listToReturn;
	}

	@Override
	public void setListPrerequisites(List<List<Subject>> prerequisitesLists) {
		if(prerequisitesLists != null){
			List<List<Ref<Subject>>> lists = transformToRefLists(prerequisitesLists);
			setPrerequisitesListsRef(lists);
		}
		
	}

	@Override
	public void addListToPrerequisites(List<Subject> list) {
		List<Ref<Subject>> listToAdd = transformToRefList(list);
		if(listToAdd != null){
			if(prerequisitesLists == null) prerequisitesLists = new ArrayList<List<Ref<Subject>>>();
			prerequisitesLists.add(listToAdd);
		}
		
	}

	@Override
	public List<Subject> getListPrerequisitesOf() {
		List<Subject> listToReturn = null;
		if(listPrerequisitesOf != null){
			listToReturn = transformToList(listPrerequisitesOf);
		}
		return listToReturn;
	}

	@Override
	public void setListPrerequisitesOf(List<Subject> listPrerequisitesOf) {
		if(listPrerequisitesOf != null){
			List<Ref<Subject>> listToAdd = transformToRefList(listPrerequisitesOf);
			if(listToAdd != null) setListPrerequisitesOfRef(listToAdd);
		}
		
	}

	@Override
	public List<List<Subject>> getCorequisitesLists() {
		List<List<Subject>> toReturn = null;
		if(corequisitesLists != null){
			toReturn = transformToLists(corequisitesLists);
		}
		return toReturn;
	}

	@Override
	public List<List<Subject>> getPrerequisitesLists() {
		List<List<Subject>> toReturn = null;
		if(corequisitesLists != null){
			if(toReturn == null) toReturn = new ArrayList<List<Subject>>();
			for(List<Ref<Subject>> list : corequisitesLists){
				List<Subject> listToAdd = new ArrayList<Subject>();
				for(Ref<Subject> ref : list){
					listToAdd.add(ref.get());
				}
				toReturn.add(listToAdd);
			}
		}
		return toReturn;
	}
	
	private List<List<Subject>> transformToLists(List<List<Ref<Subject>>> lists){
		List<List<Subject>> toReturn = null;
		if(lists != null){
			if(toReturn == null) toReturn = new ArrayList<List<Subject>>();
			for(List<Ref<Subject>> list : lists){
				List<Subject> toAdd = transformToList(list);
				if(toAdd != null) toReturn.add(toAdd);
			}
		}
		return toReturn;
	}
	
	private List<Subject> transformToList(List<Ref<Subject>> list){
		List<Subject> toReturn = null;
		if(list != null){
			if(toReturn == null) toReturn = new ArrayList<Subject>();
			for(Ref<Subject> ref : list){
				toReturn.add(ref.get());
			}
		}
		return toReturn;
	}

	@Override
	public String getPrerequisitesString() {
		String toReturn = null;
		
		for(List<Ref<Subject>> list : prerequisitesLists){
			if(toReturn != null) toReturn = toReturn.concat("; y ");
			
			for(Ref<Subject> ref : list){
				if(toReturn == null){
					toReturn = "";
				}else{
					toReturn = toReturn.concat("o");					
				}
				toReturn = toReturn.concat(ref.get().getName() + " ");
			}
		}
		
		return toReturn;
	}

	@Override
	public String getCorequisitesString() {
		String toReturn = null;
		
		for(List<Ref<Subject>> list : corequisitesLists){
			if(toReturn != null) toReturn = toReturn.concat("; y ");
			
			for(Ref<Subject> ref : list){
				if(toReturn == null){
					toReturn = "";
				}else{
					toReturn = toReturn.concat("o");					
				}
				toReturn = toReturn.concat(ref.get().getName() + " ");
			}
		}
		
		return toReturn;
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

	public List<List<Ref<Subject>>> getCorequisitesListsRef() {
		return corequisitesLists;
	}

	public void setCorequisitesListsRef(List<List<Ref<Subject>>> corequisitesLists) {
		this.corequisitesLists = corequisitesLists;
	}
	
	public void setListPrerequisitesOfRef(List<Ref<Subject>> list){
		this.listPrerequisitesOf = list;
	}
	
	public List<Ref<Subject>> getListPrerequisitesOfRef(){
		return listPrerequisitesOf;
	}
	
	public void setListCorequisitesOfRef(List<Ref<Subject>> list){
		this.listCorequisitesOf = list;
	}
	
	public List<Ref<Subject>> getListCorequisitesOfRef(){
		return listCorequisitesOf;
	}
	
	/**
	 * THis will only compare ids and the order of the subjects
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private boolean compare2Lists(List<List<Subject>> list1, List<List<Subject>> list2){
		boolean toReturn = false;
		
		if(list1 == null && list2 == null) toReturn = true;
		else if(list1 != null && list2 != null && list1.size() == list2.size()){
			boolean equals = true;
			for(int x = 0; x < list1.size(); x++){
				if(compare2List(list1.get(x),list2.get(x)) == false){
					equals = false;
					break;
				}
			}
			if(equals == true) toReturn = true;
		}
		
		return toReturn;
	}
	
	private boolean compare2List(List<Subject> list1, List<Subject> list2){
		boolean toReturn = false;

		if(list1 == null && list2 == null) toReturn = true;
		else if(list1 != null && list2 != null && list1.size() == list2.size()){
			boolean equals = true;
			for(int x = 0; x < list1.size(); x++){
				if(list1.get(x).getId().equals(list2.get(x).getId()) == false){
					equals = false;
					break;
				}
			}
			if(equals == true) toReturn = true;
		}
		
		return toReturn;
	}

	public boolean compare(ComplementaryValueAbstract cV) {
		boolean toReturn = false;
		if(cV != null && this.getId() != null && cV.getId() != null){
			if((this.getTypology()== null && cV.getTypology() == null) || (this.getTypology() != null && cV.getTypology() != null && this.getTypology().equals(cV.getTypology()) == true) && this.isMandatory() == cV.isMandatory()){
				//compare career subject and subjectGroup
				if((this.getCareerRef() == null && cV.getCareer() == null) ||
						(this.getCareerRef() != null && cV.getCareer() != null && this.getCareer().getId().equals(cV.getCareer().getId()))){
					if((this.getSubjectRef() == null && cV.getSubject() == null) ||
							(this.getSubjectRef() != null && cV.getSubject() != null && this.getSubject().getId().equals(cV.getSubject().getId()))){
						if((this.getSubjectGroupRef() == null && cV.getSubjectGroup() == null) ||
								(this.getSubjectGroupRef() != null && cV.getSubjectGroup() != null && this.getSubjectGroup().getId().equals(cV.getSubjectGroup().getId()))){
							//compare requisites
							if(compare2Lists(this.getPrerequisitesLists(), cV.getPrerequisitesLists()) == true)
								if(compare2Lists(this.getCorequisitesLists(), cV.getCorequisitesLists()) == true)
									if(compare2List(this.getListCorequisitesOf(), cV.getListCorequisitesOf()) == true)
										if(compare2List(this.getListPrerequisitesOf(), cV.getListPrerequisitesOf()) == true)
											toReturn = true;							
						}
					}
				}
			}
		}
		return toReturn;
	}

	public ComplementaryValue getClientInstance() {
		
		ComplementaryValue complementaryValue = new ComplementaryValue();
		
		complementaryValue.setId(getId());
		complementaryValue.setMandatory(isMandatory());
		complementaryValue.setTypology(getTypology());
		
		complementaryValue.setCareer(getCareer());
		complementaryValue.setSubject(getSubject());
		complementaryValue.setSubjectGroup(getSubjectGroup());
		
		complementaryValue.setListPrerequisites(getPrerequisitesLists());
		complementaryValue.setListCorequisites(getCorequisitesLists());
		complementaryValue.setListCorequisitesOf(getListCorequisitesOf());
		complementaryValue.setListPrerequisitesOf(getListPrerequisitesOf());
		
		return complementaryValue;
	}
	
}
