package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SemesterAbstract;

@Entity
public class SemesterServer extends SemesterAbstract{

	@Load private List<Ref<SubjectValue>> subjectValuesList;
	@Load private Ref<SemesterValue> semesterValue;
	
	public List<Ref<SubjectValue>> getSubjectValuesListRef() {
		return subjectValuesList;
	}
	
	public void setSubjectValuesListRef(List<Ref<SubjectValue>> subjectValuesList) {
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
		List<SubjectValue> list = null;
		for(Ref<SubjectValue> subjectValueRef : subjectValuesList){
			if(list == null) list = new ArrayList<SubjectValue>();
			list.add(subjectValueRef.get());
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
		List<Ref<SubjectValue>> list = null;
		for(SubjectValue subjectValue : subjects){
			if(list == null) list = new ArrayList<Ref<SubjectValue>>();
			if(subjectValue.getId() != null){
				list.add(Ref.create(subjectValue));
			}
		}
		setSubjectValuesListRef(list);
	}

	@Override
	public void deleteSubject(SubjectValue subjectValue) {
		List<Ref<SubjectValue>> listToRemove = new ArrayList<Ref<SubjectValue>>();
		if(subjectValue != null && subjectValue.getId() != null){
			for(Ref<SubjectValue> subjectValueRef : subjectValuesList){
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
	
}
