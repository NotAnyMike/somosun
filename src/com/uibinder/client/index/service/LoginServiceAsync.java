package com.uibinder.client.index.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.uibinder.shared.LoginInfo;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
