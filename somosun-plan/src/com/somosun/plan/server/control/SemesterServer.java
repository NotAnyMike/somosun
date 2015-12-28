package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.server.dao.SubjectValueDao;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SemesterAbstract;

@Entity
public class SemesterServer extends SemesterAbstract{

	@Load private List<Ref<SubjectValueServer>> subjectValuesList;
	@Load private Ref<SemesterValue> semesterValue;
	
	public SemesterServer(){
		subjectValuesList= new ArrayList<Ref<SubjectValueServer>>();
	}
	
	public SemesterServer(SemesterValue semesterValue){
        subjectValuesList= new ArrayList<Ref<SubjectValueServer>>();
        setSemesterValue(semesterValue);
    }
	
	public List<Ref<SubjectValueServer>> getSubjectValuesListRef() {
		return subjectValuesList;
	}
	
	public List<SubjectValueServer> getSubjectValuesListLoaded(){
		List<SubjectValueServer> list = new ArrayList<SubjectValueServer>();
		for(Ref<SubjectValueServer> s : subjectValuesList){
			list.add(s.get());
		}
		return list;
	}
	
	public void setSubjectValuesListRef(List<Ref<SubjectValueServer>> subjectValuesList) {
		this.subjectValuesList = subjectValuesList;
	}
	
	public Ref<SemesterValue> getSemesterValueRef() {
		return semesterValue;
	}
	
	public void setSemesterValueRef(Ref<SemesterValue> semesterValue) {
		this.semesterValue = semesterValue;
	}

	@Override
	public List<SubjectValue> getSubjects() {
		List<SubjectValue> list = new ArrayList<SubjectValue>();
		if(subjectValuesList != null){			
			for(Ref<SubjectValueServer> subjectValueRef : subjectValuesList){
				list.add(subjectValueRef.get().getClientInstance());
			}
		}
		return list;
	}

	@Override
	public SemesterValue getSemesterValue() {
		SemesterValue toReturn = null;
		if(semesterValue != null) toReturn = semesterValue.get();
		return toReturn;
	}

	@Override
	public void setSemesterValue(SemesterValue semesterValue) {
		if(semesterValue != null && semesterValue.getId() != null){
			this.setSemesterValueRef(Ref.create(semesterValue));
		}else{
			this.setSemesterValueRef(null);
		}
	}

	@Override
	public void setSubjects(List<SubjectValue> subjects) {
		List<Ref<SubjectValueServer>> list = new ArrayList<Ref<SubjectValueServer>>();
		for(SubjectValue subjectValue : subjects){
			if(subjectValue.getId() != null){
				SubjectValueDao subjectValueDao = new SubjectValueDao();
				SubjectValueServer subjectValueServer = subjectValueDao.getById(subjectValue.getId());
				if(subjectValueServer != null){					
					list.add(Ref.create(subjectValueServer));
				}
			}
		}
		setSubjectValuesListRef(list);
	}
	
	public void setSubjectServers(List<SubjectValueServer> subjects) {
		List<Ref<SubjectValueServer>> list = new ArrayList<Ref<SubjectValueServer>>();
		for(SubjectValueServer subjectValue : subjects){
			if(subjectValue.getId() != null){
				list.add(Ref.create(subjectValue));									
			}
		}
		setSubjectValuesListRef(list);
	}

	@Override
	public void deleteSubject(SubjectValue subjectValue) {
		List<Ref<SubjectValueServer>> listToRemove = new ArrayList<Ref<SubjectValueServer>>();
		if(subjectValue != null && subjectValue.getId() != null){
			for(Ref<SubjectValueServer> subjectValueRef : subjectValuesList){
				if(subjectValueRef.get().getId().equals(subjectValue.getId()) == true){
					listToRemove.add(subjectValueRef);
				}
			}
		}
		subjectValuesList.removeAll(listToRemove);
	}

	@Override
	public void addSubject(SubjectValue subjectValue) {
		if(subjectValue != null && subjectValue.getId() != null){
			SubjectValueDao subjectValueDao = new SubjectValueDao();
			SubjectValueServer subjectValueServer = subjectValueDao.getById(subjectValue.getId());
			if(subjectValueServer != null){				
				subjectValuesList.add(Ref.create(subjectValueServer));
			}
		}
	}
	
	public void addSubjectServer(SubjectValueServer subjectValue) {
		if(subjectValue != null && subjectValue.getId() != null){
			subjectValuesList.add(Ref.create(subjectValue));							
		}
	}
	
	public boolean compare(SemesterAbstract semester){
		boolean toReturn = false;
		
		if(semester != null){
			if(this.getId() != null && semester.getId() != null && this.getId().equals(semester.getId()) == true && 
					((this.getSemesterValue() == null && semester.getSemesterValue() == null) || (this.getSemesterValue() != null &&  this.getSemesterValue().getId().equals(semester.getSemesterValue().getId()) == true))){
				//Check the subjectValues
				if((this.getSubjects() == null && semester.getSubjects() == null) || (this.getSubjects().isEmpty() == true && semester.getSubjects().isEmpty() == true)){
					toReturn = true;
				}else{
					if(this.getSubjects() != null && semester.getSubjects() != null && this.getSubjects().size() == semester.getSubjects().size()){
						boolean areEqual = true;
						for(int x = 0; x < this.getSubjects().size(); x++){
							if(this.getSubjects().get(x).getId().equals(semester.getSubjects().get(x).getId()) == false){
								areEqual = false;
								break;
							}
						}
						if(areEqual == true) toReturn = true;
					}
				}
			}
		}
		
		return toReturn;
	}
	
	public Semester getClientInstance(){
		Semester semester = new Semester();
		
		semester.setId(this.getId());
		semester.setSemesterValue(getSemesterValue());
		
		List<SubjectValue> list = new ArrayList<SubjectValue>();
		if(subjectValuesList != null){			
			for(Ref<SubjectValueServer> ref : subjectValuesList){
				if(list == null) list = new ArrayList<SubjectValue>();
				boolean isLoaded = ref.isLoaded();
				list.add(ref.get().getClientInstance());
			}
		}
		semester.setSubjects(list);
		
		return semester; 
	}
	
}
