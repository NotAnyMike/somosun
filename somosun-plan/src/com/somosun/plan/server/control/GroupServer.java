package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.control.controlAbstract.GroupAbstract;

@Entity
public class GroupServer extends GroupAbstract {

	@Index private Ref<Subject> subject = null;
    @Index private Ref<Teacher> teacher = null;
    @Index private Ref<SemesterValue> semesterValue = null;
    private List<Ref<Block>> schedule=null;
    private List<Ref<Career>> careers = null;
	
	public GroupServer(){}
	    
    public GroupServer(Subject subject, Teacher teacher, SemesterValue semester, int freePlaces, int groupNumber, int totalPlaces, List<Block> schedule, List<Career> careers) {
        setSubject(subject);
        setTeacher(teacher);
        setSemesterValue(semester);
        setFreePlaces(freePlaces);
        setGroupNumber(groupNumber);
        setTotalPlaces(totalPlaces);
        setSchedule(schedule);
        setCareers(careers);
    }
    
    public GroupServer(Subject subject, SemesterValue semesterValue, Integer groupNumber) {
    	setSubject(subject);
        setSemesterValue(semesterValue);
        setGroupNumber(groupNumber);
        setCareers(new ArrayList<Career>());
	}
    
    public Subject getSubject() {
		return (getSubjectRef() == null ? null : getSubjectRef().get());
	}

	public void setSubject(Subject subject) {
		if(subject != null && subject.getId() != null){			
			this.setSubjectRef(Ref.create(subject));
		}
	}

	public Teacher getTeacher() {
		return (getTeacherRef() == null ? null : getTeacherRef().get());
	}

	public void setTeacher(Teacher teacher) {
		if(teacher != null && teacher.getIdSun() != null){
			this.setTeacherRef(Ref.create(teacher));
		}
	}

	public SemesterValue getSemesterValue() {
		return (getSemesterValueRef() == null ? null : getSemesterValueRef().get());
	}

	public void setSemesterValue(SemesterValue semester) {
		if(semester != null && semester.getId() != null){
			this.setSemesterValueRef(Ref.create(semester));
		}
	}
	
	public List<Block> getSchedule() {
		List<Block> toReturn = null;
		if(schedule != null){
			toReturn = new ArrayList<Block>();
			for(Ref<Block> ref : schedule){
				toReturn.add(ref.get());
			}
		}
		return toReturn;
	}

	public void setSchedule(List<Block> schedule) {
		if(schedule != null){
			List<Ref<Block>> list = new ArrayList<Ref<Block>>();
			for(Block b : schedule){
				if(b.getId() != null) list.add(Ref.create(b));
			}
			setScheduleRef(list);
		}
	}
	
	public void setScheduleRef(List<Ref<Block>> list){
		this.schedule = list;
	}

	public List<Career> getCareers() {
		List<Career> listToReturn = null;
		if(careers != null){
			listToReturn = new ArrayList<Career>();
			for(Ref<Career> ref : careers){
				listToReturn.add(ref.get());
			}
		}
		return listToReturn;
	}
	
	/**
	 * Be carefull with this, because if the list careers is not empty then it can be deleted 
	 * @param careers
	 */
	public void setCareers(List<Career> careers) {
		if(careers != null){
			List<Ref<Career>> list = new ArrayList<Ref<Career>>();
			for(Career c : careers){
				if(c.getId() != null) list.add(Ref.create(c));
			}
			setCareersRef(list);
		}
	}
	
	protected void setCareersRef(List<Ref<Career>> list){
		this.careers = list;
	}
	
	public List<Ref<Career>> getCareersRef(){
		return careers;
	}

	public Ref<SemesterValue> getSemesterValueRef() {
		return semesterValue;
	}

	public void setSemesterValueRef(Ref<SemesterValue> semesterValue) {
		this.semesterValue = semesterValue;
	}
	
	private boolean compareSchedules(List<Block> list){
		boolean toReturn = false;
		
		if(this.getSchedule() == null && list == null){
			toReturn = true;
		}else if(this.getSchedule() != null && list != null && list.size() == this.getSchedule().size()){
			boolean areEqual = true;
			for(int x = 0; x < this.getSchedule().size(); x++){
				if(this.getSchedule().get(x).getId().equals(list.get(x).getId()) == false){
					areEqual = false;
					break;
				}
			}
			if(areEqual == true) toReturn = true;
		}
		
		return toReturn;
	}
	
	private boolean compareCareers(List<Career> list){
		boolean toReturn = false;
		
		if(this.getCareers() == null && list == null){
			toReturn = true;
		}else if(this.getCareers() != null && list != null && list.size() == this.getSchedule().size()){
			boolean areEqual = true;
			for(int x = 0; x < this.getSchedule().size(); x++){
				if(this.getCareers().get(x).getId().equals(list.get(x).getId()) == false){
					areEqual = false;
					break;
				}
			}
			if(areEqual == true) toReturn = true;
		}
		
		return toReturn;
	}

	public boolean compare(GroupAbstract g){
		boolean toReturn = false;
		
		if(this.getId() != null && g.getId() != null && this.getId().equals(g.getId()) && this.getGroupNumber() == g.getGroupNumber() && this.getFreePlaces() == g.getFreePlaces() &&
				this.getTotalPlaces() == g.getTotalPlaces() && 
				((this.getAverageGrade() == null && g.getAverageGrade() == null) || (this.getAverageGrade() != null && g.getAverageGrade() != null && this.getAverageGrade().equals(g.getAverageGrade())))){
			//Subject, Teacher, semesterValue, schedule, careers	
			if((this.getSubjectRef() == null && g.getSubject() == null) || 
					(this.getSubjectRef() != null && g.getSubject() != null && this.getSubject().getId().equals(g.getSubject().getId()) == true)){
				if((this.getTeacherRef() == null && g.getTeacher() == null) || 
						(this.getTeacherRef() != null && g.getTeacher() != null && this.getTeacher().getIdSun().equals(g.getTeacher().getIdSun()) == true)){
					if((this.getSemesterValueRef() == null && g.getSemesterValue() == null) || 
							(this.getSemesterValueRef() != null && g.getSemesterValue() != null && this.getSemesterValue().getId().equals(g.getSemesterValue().getId()) == true)){
						//check schedule and careers
						if(compareSchedules(g.getSchedule()) == true){
							if(compareCareers(g.getCareers()) == true){
								toReturn = true;
							}
						}
						
					}
				}
			}
		}
		
		return toReturn;
	}

	public Ref<Subject> getSubjectRef() {
		return subject;
	}

	public void setSubjectRef(Ref<Subject> subject) {
		this.subject = subject;
	}

	public Ref<Teacher> getTeacherRef() {
		return teacher;
	}

	public void setTeacherRef(Ref<Teacher> teacher) {
		this.teacher = teacher;
	}
	
	public Group getClientInstance(){
		Group g = new Group();
		
		g.setId(this.getId());
		g.setGroupNumber(this.getGroupNumber());
		g.setFreePlaces(this.getFreePlaces());
		g.setTotalPlaces(this.getTotalPlaces());
		g.setAverageGrade(this.getAverageGrade());
		g.setSubject(this.getSubject());
		g.setTeacher(this.getTeacher());
		g.setSemesterValue(this.getSemesterValue());
		g.setSchedule(this.getSchedule());
		g.setCareers(this.getCareers());
		
		return g;
	}
	
	public void addCareer(Career career) {
		if(career != null){
			if(careers == null) careers = new ArrayList<Ref<Career>>();
			getCareersRef().add(Ref.create(career));
		}
	}
}
