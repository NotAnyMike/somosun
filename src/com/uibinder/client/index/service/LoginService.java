package com.uibinder.client.index.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.shared.LoginInfo;
import com.uibinder.shared.SiaResultGroups;
import com.uibinder.shared.control.Subject;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService{
	public LoginInfo login(String requestUri);
}
