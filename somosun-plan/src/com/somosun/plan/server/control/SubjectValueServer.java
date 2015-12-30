package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.server.dao.ComplementaryValueDao;
import com.somosun.plan.server.dao.GroupDao;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.SubjectValueAbstract;

@Cache(expirationSeconds=9000)
@Entity
public class SubjectValueServer extends SubjectValueAbstract {

	@Index private Ref<GroupServer> group = null;
	private Ref<ComplementaryValueServer> complementaryValue = null;
	
    public SubjectValueServer(){
    	this.setComplementaryValueServer(new ComplementaryValueServer());
    }
    
    public SubjectValueServer(GroupServer group, double grade,boolean taken, ComplementaryValueServer complementaryValue) {
        setGroupServer(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValueServer(complementaryValue);
    }

	public SubjectValueServer(double grade,boolean taken, ComplementaryValueServer complementaryValue) {
    	setGroupServer(null);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValueServer(complementaryValue);
    }
    
    public SubjectValueServer(GroupServer group, double grade,boolean taken) {
    	setGroupServer(group);
        setGrade(grade);
        setTaken(taken);
        setComplementaryValueServer(new ComplementaryValueServer());
    }

    public void setGroupServer(GroupServer group) {
		if(group != null && group.getId() != null){
			setGroupRef(Ref.create(group));
		}
	}
    
	public Ref<GroupServer> getGroupRef() {
		return group;
	}

	public void setGroupRef(Ref<GroupServer> group) {
		this.group = group;
	}

	public Ref<ComplementaryValueServer> getComplementaryValueRef() {
		return complementaryValue;
	}

	public void setComplementaryValueRef(Ref<ComplementaryValueServer> complementaryValue) {
		this.complementaryValue = complementaryValue;
	}
	
	public void setComplementaryValueServer(ComplementaryValueServer complementaryValue) {
		if(complementaryValue != null && complementaryValue.getId() != null) this.complementaryValue = Ref.create(complementaryValue);
	}

	@Override
	public Group getGroup() {
		Group toReturn = null;
		if(group != null) toReturn = group.get().getClientInstance();
		return toReturn;
	}

	@Override
	public void setGroup(Group group) {
		if(group != null && group.getId()!= null){
			GroupDao gDao = new GroupDao();
			GroupServer gS = gDao.getById(group.getId());
			setGroupRef((gS == null ? null : Ref.create(gS)));
		}
	}

	@Override
	public ComplementaryValue getComplementaryValue() {
		ComplementaryValue toReturn = null;
		if(complementaryValue != null) toReturn = complementaryValue.get().getClientInstance();
		return toReturn;
	}

	@Override
	public void setComplementaryValue(ComplementaryValue complementaryValue) {
		if(complementaryValue != null && complementaryValue.getId() != null){
			ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
			ComplementaryValueServer cV = complementaryValueDao.getById(complementaryValue.getId());
			if(cV != null){
				setComplementaryValueRef(Ref.create(cV));
			}				
		}
		
	}
	
	public SubjectValue getClientInstance(){
		SubjectValue subjectValue = new SubjectValue();
		
		subjectValue.setId(getId());
		subjectValue.setGrade(getGrade());
		subjectValue.setSubjectValuesPublicId(getSubjectValuePublicId());
		subjectValue.setTaken(isTaken());
		subjectValue.setGroup(getGroup());
		subjectValue.setComplementaryValue(getComplementaryValueRef().get().getClientInstance());
		
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
