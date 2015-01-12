package com.uibinder.index.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.uibinder.index.shared.LoginInfo;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
