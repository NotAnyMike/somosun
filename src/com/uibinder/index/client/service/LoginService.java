package com.uibinder.index.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.index.shared.LoginInfo;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService{
	public LoginInfo login(String requestUri);
	public String getSubject();
}
