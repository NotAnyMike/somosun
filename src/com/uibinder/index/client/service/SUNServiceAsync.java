package com.uibinder.index.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

	void getRandomPhrase(AsyncCallback<String[]> callback);

}
