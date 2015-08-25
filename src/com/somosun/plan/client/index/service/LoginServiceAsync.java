package com.somosun.plan.client.index.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.somosun.plan.shared.LoginInfo;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
