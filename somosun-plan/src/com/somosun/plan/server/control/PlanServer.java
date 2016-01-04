package com.somosun.plan.server.control;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.server.dao.SemesterDao;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.controlAbstract.PlanAbstract;

@Entity
public class PlanServer extends PlanAbstract {

	@Index private Ref<Career> career = null;
	@Index private Ref<Student> user = null;
    private List<Ref<SemesterServer>> semesters = null;
    
    public PlanServer(){}
    
//    public PlanServer(Plan p){
//    	
//		this.setId(p.getId());
//		this.setCareerNoRef(p.getCareer());
//		this.setDefault(p.isDefault());
//		this.setGpa(p.getGpa());
//		this.setName(p.getName());
//		this.setUserNoRef(p.getUser());
//		
//		List<Ref<Semester>> list = null;
//		for(Semester s : p.getSemesters()){
//			if(list == null) list = new ArrayList<Ref<Semester>>();
//			if(s.getId() == null){
//				SemesterDao semesterDao = new SemesterDao();
//				s.setId(semesterDao.save(s));
//			}
//			Ref<Semester> ref = Ref.create(s);
//			list.add(ref);
//			
//			//TODO update all the values here I THIKNK ALL VALUES ARE UPDATED
//		}
//		this.setSemesters(list);
//		
//		//this.getUser().get().setName("test");
//		
//		boolean isLoaded = this.getUser().isLoaded();
//		isLoaded =  this.getSemesters().get(0).isLoaded();
//		isLoaded =  this.getSemesters().get(1).isLoaded();
//		
//		int x = this.getSemesters().get(0).get().getSubjects().size();
//		x = this.getSemesters().get(1).get().getSubjects().size();
//		x = 0;
//    }
    
    public void calculateGpa() {
		int credits = 0;
		double sum = 0;
		for(Ref<SemesterServer> semester : getSemestersRef()){
			for(SubjectValue subjectValue : semester.get().getSubjects()){
				if(subjectValue.isTaken()){
					if(subjectValue != null && subjectValue.getComplementaryValue() != null && subjectValue.getComplementaryValue().getSubject() != null){						
						if(subjectValue.getComplementaryValue().getSubject().isApprovenType() == false){						
							sum += (subjectValue.getGrade()*(subjectValue.getComplementaryValue().getSubject().getCredits()));
							credits += subjectValue.getComplementaryValue().getSubject().getCredits();
						}
					}
				}
			}
		}
		this.setGpa((double) sum/credits);
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
	
	public void setCareer(Career career) {
		if(career != null && career.getId() != null){			
			this.career = Ref.create(career);
		}
	}

	public Ref<Student> getUserRef() {
		return user;
	}
	
	public Student getUser(){
		Student toReturn = null;
		if(user != null) toReturn = user.get();
		return toReturn;
	}

	public void setUserRef(Ref<Student> user) {
		this.user = user;
	}
	
	public void setUser(Student user){
		if(user != null && user.getIdSun() != null){
			this.setUserRef(Ref.create(user));
		}else{
			this.setUserRef(null);
		}
	}

	public List<Ref<SemesterServer>> getSemestersRef() {
		if(semesters == null) semesters = new ArrayList<Ref<SemesterServer>>();
		return semesters;
	}

	public void setSemestersRef(List<Ref<SemesterServer>> semesters) {
		this.semesters = semesters;
	}
	
	public void setSemesters(List<SemesterServer> semesters){
		List<Ref<SemesterServer>> list = null;
		SemesterDao semesterDao = new SemesterDao();
		if(semesters != null){			
			for(SemesterServer s : semesters){
				if(s.getId() != null){				
					if(list == null) list = new ArrayList<Ref<SemesterServer>>();
					list.add(Ref.create(s));
				}
			}
		}
		setSemestersRef(list);
	}
	
	public void setSemesterServers(List<SemesterServer> semesters) {
		List<Ref<SemesterServer>> list = new ArrayList<Ref<SemesterServer>>();
		for(SemesterServer s : semesters){
			if(s.getId() != null){				
				list.add(Ref.create(s));
			}
		}
		setSemestersRef(list);
	}
	
	public List<Semester> getSemesters(){
		List<Semester> list = new ArrayList<Semester>();
		for(Ref<SemesterServer> semesterRef : semesters){
			if(semesterRef != null){				
				SemesterServer semesterServer = semesterRef.get();
				Semester semester = null;
				if(semesterServer != null) semester = semesterServer.getClientInstance();
				if(semester != null){					
					if(list == null) list = new ArrayList<Semester>();
					list.add(semester);
				}
			}
		}
		return list;
	}
	
	public Plan getClientInstance(){
		Plan p = new Plan();
		
		p.setId(getId());
		p.setCareer(getCareer());
		p.setDefault(isDefault());
		p.setGpa(getGpa());
		p.setName(getName());
		p.setUser(getUserRef().get());
		
		List<Semester> list = null;
		if(semesters != null){			
			for(Ref<SemesterServer> s : semesters){
				if(s != null && s.get() != null){					
					if(list == null) list = new ArrayList<Semester>();
					list.add(s.get().getClientInstance());
				}
			}
		}
		p.setSemesters(list);
		
		return p;
	}
	
	public boolean compare(PlanAbstract p){
		boolean toReturn = false;
		
		if(p != null && this.getName().equals(p.getName()) && (this.getUser().getIdSun().equals(p.getUser().getIdSun()))  && this.getGpa() == p.getGpa() &&
				this.getId().equals(p.getId()) && this.getCareer().equals(p.getCareer())){
			//Check the semesters
			//if null or empty both
			if((this.getSemestersRef() == null && p.getSemesters() == null) || (this.getSemestersRef() != null && this.getSemestersRef().isEmpty() && p.getSemesters() != null & p.getSemesters().isEmpty())){
				toReturn = true;
			}else{
				if(this.getSemestersRef() != null && p.getSemesters() != null && this.getSemestersRef().size() == p.getSemesters().size()){
					boolean isEqual = true;
					for(int x = 0; x < this.getSemestersRef().size(); x++){
						if(this.getSemestersRef().get(x).equals(p.getSemesters().get(x)) == false){
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
