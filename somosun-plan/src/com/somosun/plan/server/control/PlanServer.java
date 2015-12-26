package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.server.dao.SemesterDao;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.incomplete.PlanIncomplete;

@Entity
public class PlanServer extends PlanIncomplete {

	@Index @Load private Ref<Career> career = null;
	@Index @Load private Ref<Student> user = null;
    @Load private List<Ref<Semester>> semesters = null;
    
    public PlanServer(){}
    
    public PlanServer(Plan p){
		this.setId(p.getId());
		this.setCareerNoRef(p.getCareer());
		this.setDefault(p.isDefault());
		this.setGpa(p.getGpa());
		this.setName(p.getName());
		this.setUserNoRef(p.getUser());
		
		List<Ref<Semester>> list = null;
		for(Semester s : p.getSemesters()){
			if(list == null) list = new ArrayList<Ref<Semester>>();
			if(s.getId() == null){
				SemesterDao semesterDao = new SemesterDao();
				s.setId(semesterDao.save(s));
			}
			Ref<Semester> ref = Ref.create(s);
			list.add(ref);
			
			//TODO update all the values here I THIKNK ALL VALUES ARE UPDATED
		}
		this.setSemesters(list);
		
		
		int x = this.getSemesters().get(0).get().getSubjects().size();
		x = this.getSemesters().get(1).get().getSubjects().size();
		x = 0;
    }
    
    public void calculateGpa() {
		int credits = 0;
		double sum = 0;
		for(Ref<Semester> semester : getSemesters()){
			for(SubjectValue subjectValue : semester.get().getSubjects()){
				if(subjectValue.isTaken()){
					if(subjectValue.getComplementaryValue().getSubject().isApprovenType() == false){						
						sum += (subjectValue.getGrade()*(subjectValue.getComplementaryValue().getSubject().getCredits()));
						credits += subjectValue.getComplementaryValue().getSubject().getCredits();
					}
				}
			}
		}
		this.setGpa((double) sum/credits);
	}

	public Ref<Career> getCareer() {
		return career;
	}

	public void setCareer(Ref<Career> career) {
		this.career = career;
	}
	
	public void setCareerNoRef(Career career) {
		if(career != null && career.getId() != null){			
			this.career = Ref.create(career);
		}
	}

	public Ref<Student> getUser() {
		return user;
	}

	public void setUser(Ref<Student> user) {
		this.user = user;
	}

	public List<Ref<Semester>> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Ref<Semester>> semesters) {
		this.semesters = semesters;
	}
	
	public void setSemestersNoRef(List<Semester> semesters){
		List<Ref<Semester>> list = null;
		for(Semester s : semesters){
			if(list == null) list = new ArrayList<Ref<Semester>>();
			list.add(Ref.create(s));
		}
		setSemesters(list);
	}

	public void setUserNoRef(Student student) {
		if(student != null && student.getIdSun() != null){
			setUser(Ref.create(student));
		}
	}
	
	public Plan getClientInstance(){
		Plan p = new Plan();
		
		p.setId(getId());
		p.setCareer(getCareer().get());
		p.setDefault(isDefault());
		p.setGpa(getGpa());
		p.setName(getName());
		p.setUser(getUser().get());
		
		List<Semester> list = null;
		for(Ref<Semester> s : semesters){
			if(list == null) list = new ArrayList<Semester>();
			list.add(s.get());
		}
		p.setSemesters(list);
		
		return p;
	}
	
	public boolean compare(Plan p){
		boolean toReturn = false;
		
		if(p != null && this.getName().equals(p.getName()) && this.getUser().equals(p.getUser())  && this.getGpa() == p.getGpa() &&
				this.getId().equals(p.getId()) && this.getCareer().equals(p.getCareer())){
			//Check the semesters
			//if null or empty both
			if((this.getSemesters() == null && p.getSemesters() == null) || (this.getSemesters() != null && this.getSemesters().isEmpty() && p.getSemesters() != null & p.getSemesters().isEmpty())){
				toReturn = true;
			}else{
				if(this.getSemesters() != null && p.getSemesters() != null && this.getSemesters().size() == p.getSemesters().size()){
					boolean isEqual = true;
					for(int x = 0; x < this.getSemesters().size(); x++){
						if(this.getSemesters().get(x).equals(p.getSemesters().get(x)) == false){
							isEqual = false;
							break;
						}
					}
					if(isEqual == true) toReturn = true;
				}
			}
		}
		
		return toReturn;
	}
    
}
