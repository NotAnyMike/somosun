package com.uibinder.index.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Subject;

/**
 * 
 * @author Mike
 * 
 * This is the Asynchronous interface, the one that is inherit from the bitches form the server side,
 * this one has the same methods than SUNService because it is the connection between the server an 
 * the client
 * 
 * 
 */
public interface SUNServiceAsync {

	void getSubjectByCode(int code, AsyncCallback<Subject> callback);

	void getSubjectByCode(int code, String career, AsyncCallback<Subject> callback);

	void getSubjectByName(String name, AsyncCallback<Subject> callback);

	void getSubjectByName(String name, String career, AsyncCallback<Subject> callback);

	void getRandomPhrase(AsyncCallback<List<RandomPhrase>> callback);

	void saveRandomPhrase(String phrase, String author,
			AsyncCallback<Void> callback);

	/**
	 * For more information goto SiaProxy.class
	 * @param subject
	 * @param sede
	 * @param callback
	 */
	void getGroupsFromSia(Subject subject, String sede,
			AsyncCallback<SiaResultGroups> callback);
	
	/**
	 * For more information goto SiaProxy.class
	 * @param subjectSiaCode
	 * @param sede
	 * @param callback
	 */
	void getGroupsFromSia(String subjectSiaCode, String sede,
			AsyncCallback<SiaResultGroups> callback);

	/**
	 * For more information goto SiaProxy.class
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param callback
	 */
	void getSubjectFromSia(String nameOrCode, String typology, String career,
			String scheduleCP, int page, int ammount, String sede,
			AsyncCallback<SiaResultSubjects> callback);

}
