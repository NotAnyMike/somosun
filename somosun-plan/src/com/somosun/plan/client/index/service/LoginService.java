package com.somosun.plan.client.index.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.control.Subject;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService{
	public LoginInfo login(String requestUri);
}
