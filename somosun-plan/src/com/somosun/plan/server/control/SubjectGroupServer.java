package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.controlAbstract.SubjectGroupAbstract;

/**
 * Agrupaciones
 * 
 * @author Mike
 *
 */
@Cache(expirationSeconds=9000)
@Entity
public class SubjectGroupServer extends SubjectGroupAbstract<SubjectGroupServer>{

	@Index private Ref<Career> career = null;
	
	public SubjectGroupServer(){
	}
	
	public SubjectGroupServer(String name, Career career, Boolean fundamental, int obligatoryCredits, int optativeCredits, boolean error) {
		setName(name);
		setCareer(career);
		setFundamental(fundamental);
		setObligatoryCredits(obligatoryCredits);
		setOptativeCredits(optativeCredits);
		setError(error);
	}
	
	public boolean equals(SubjectGroupServer g){
		if(this.getName() == g.getName() && this.getCareer().equals(g.getCareer()) && this.isFundamental() == g.isFundamental() && this.getObligatoryCredits() == g.getObligatoryCredits() && this.getOptativeCredits() == g.getOptativeCredits()){
			return true;
		} else {
			return false;
		}
	}

	public Ref<Career> getCareerRef() {
		return career;
	}

	public void setCareerRef(Ref<Career> career) {
		this.career = career;
	}
	
	public Career getCareer() {
		Career c = null;
		if(career != null) c = career.get();
		return c;
	}

	public void setCareer(Career career) {
		if(career != null && career.getId() != null){
			setCareerRef(Ref.create(career));
		}
	}
	
	public boolean compare(SubjectGroupAbstract sG){
		boolean toReturn = false;
		
		if(this.getId() != null && sG.getId() != null && this.getId().equals(sG.getId())== true && this.getName().equals(sG.getName()) == true && 
				this.getObligatoryCredits() == sG.getObligatoryCredits() && this.getOptativeCredits() == sG.getOptativeCredits() && this.isFundamental() == sG.isFundamental() &&
				((this.getError() == null && sG.getError() == null ) || (this.getError() != null && sG.getError() != null && this.getError().equals(sG.getError())))){
			if((this.getCareerRef() == null && sG.getCareer() == null) || (this.getCareerRef() != null && sG.getCareer() != null && this.getCareer().getId().equals(sG.getId()))){
				toReturn = true;
			}
		}
		
		return toReturn;
	}
	
	public SubjectGroup getClientInstance(){
		SubjectGroup sG = new SubjectGroup();
		
		sG.setId(this.getId());
		sG.setError(this.getError());
		sG.setFundamental(this.isFundamental());
		sG.setName(this.getName());
		sG.setObligatoryCredits(this.getObligatoryCredits());
		sG.setOptativeCredits(this.getOptativeCredits());
		sG.setCareer(this.getCareer());
		
		return sG;
	}
	
}
