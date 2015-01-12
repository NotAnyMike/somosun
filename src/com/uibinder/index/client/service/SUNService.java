package com.uibinder.index.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
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
	
	/** 
	 * For more information goto SiaProxy.class
	 * 
	 * @param subjectSiaCode
	 * @param sede
	 * @return
	 */
	public SiaResultGroups getGroupsFromSia(String subjectSiaCode, String sede);
	
	/**
	 * For more information goto SiaProxy.class
	 * @param subject
	 * @param sede
	 * @return
	 */
	public SiaResultGroups getGroupsFromSia(Subject subject, String sede);
	
	/**
	 * For more information goto SiaProxy.class
	 * 
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @return
	 */
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede);
	
}
