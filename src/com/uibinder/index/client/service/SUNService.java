package com.uibinder.index.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
	
	String[] getRandomPhrase();
	
}
