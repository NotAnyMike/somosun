package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SubjectValueAbstract;

@Cache(expirationSeconds=9000)
@Entity
public class SubjectValueServer extends SubjectValueAbstract {

	@Index private Ref<Group> group = null;
	private Ref<ComplementaryValue> complementaryValue = null;
	
    public SubjectValueServer(){
    	this.setComplementaryValue(new ComplementaryValue());
    }
    
    public SubjectValueServer(Group group, double grade,boolean taken, ComplementaryValue complementaryValue) {
        setGroup(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(complementaryValue);
    }
    
    public SubjectValueServer(double grade,boolean taken, ComplementaryValue complementaryValue) {
    	setGroup(null);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(complementaryValue);
    }
    
    public SubjectValueServer(Group group, double grade,boolean taken) {
    	setGroup(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValue(new ComplementaryValue());
    }

	public Ref<Group> getGroupRef() {
		return group;
	}

	public void setGroupRef(Ref<Group> group) {
		this.group = group;
	}

	public Ref<ComplementaryValue> getComplementaryValueRef() {
		return complementaryValue;
	}

	public void setComplementaryValueRef(Ref<ComplementaryValue> complementaryValue) {
		this.complementaryValue = complementaryValue;
	}

	@Override
	public Group getGroup() {
		Group toReturn = null;
		if(group != null) toReturn = group.get();
		return toReturn;
	}

	@Override
	public void setGroup(Group group) {
		if(group != null && group.getId()!= null){
			setGroupRef(Ref.create(group));
		}
	}

	@Override
	public ComplementaryValue getComplementaryValue() {
		ComplementaryValue toReturn = null;
		if(complementaryValue != null) toReturn = complementaryValue.get();
		return toReturn;
	}

	@Override
	public void setComplementaryValue(ComplementaryValue complementaryValue) {
		if(complementaryValue != null && complementaryValue.getId() != null){
			setComplementaryValueRef(Ref.create(complementaryValue));
		}
		
	}
	
	public SubjectValue getClientInstance(){
		SubjectValue subjectValue = new SubjectValue();
		
		subjectValue.setId(getId());
		subjectValue.setGrade(getGrade());
		subjectValue.setSubjectValuesPublicId(getSubjectValuePublicId());
		subjectValue.setTaken(isTaken());
		subjectValue.setGroup(getGroup());
		subjectValue.setComplementaryValue(getComplementaryValue());
		
		return subjectValue;
	}
	
	public boolean compare(SubjectValueAbstract sV){
		boolean toReturn = false;
		
		if(sV != null && sV.getId() != null && this.getId() != null && this.getGrade() == sV.getGrade()){
			//check complementaryValue
			if((this.getComplementaryValue() == null & sV.getComplementaryValue() == null) || 
					(this.getComplementaryValue() != null && sV.getComplementaryValue() != null && this.getComplementaryValue().equals(sV.getComplementaryValue()) == true)){
				//check group
				if((this.getGroup() == null && sV.getGroup() == null ) || 
						(this.getGroup() != null && sV.getGroup() != null && this.getGroup().getId().equals(sV.getGroup().getId()) == true)){
					toReturn = true;
				}
			}
		}
		
		return toReturn;
	}
	
}
