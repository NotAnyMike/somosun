package com.uibinder.index.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;

/**
 * 
 * @author Mike
 * 
 * The RemoteServiceRelativePath note is to reference it dynamically on the web.xml to allow the use of the servlet
 * This is the class that has the methods that can be used to communicate with the server.
 * 
 * 
 */
@RemoteServiceRelativePath("sunService")
public interface SUNService extends RemoteService{

	Subject getSubjectByCode(int code);
	Subject getSubjectByCode(int code, String career);
	
	Subject getSubjectByName(String name);
	Subject getSubjectByName(String name, String career);
	
	List<RandomPhrase> getRandomPhrase();
	
	void saveRandomPhrase(String phrase, String author);
	
	public SiaResultGroups getGroupsFromSia(String subjectSiaCode, String sede);
	public SiaResultGroups getGroupsFromSia(Subject subject, String sede);
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student);
	public SiaResultSubjects getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student);
	
	public List<Career> getCareers(String sede);
	
	public Plan getPlanDefaultFromString(String careerCode);
	
	public void toTest();
	
	public ComplementaryValues getComplementaryValues(String career, String code);
	
	public List<ComplementaryValues> getComplementaryValues(String careerCode);
	
	public void savePlanAsDefault(Student student, Plan plan);
	
	public Plan getPlanDefault(String careerCode);
}
