package com.uibinder.index.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.control.Subject;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService{
	public LoginInfo login(String requestUri);
}
