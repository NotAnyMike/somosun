package com.somosun.plan.client.index;

import com.somosun.plan.client.index.AppController;
import com.somosun.plan.client.index.service.LoginService;
import com.somosun.plan.client.index.service.LoginServiceAsync;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.control.Student;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	@Override
	public void onModuleLoad() {
		SUNServiceAsync rpcService = GWT.create(SUNService.class);
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		HandlerManager eventBus = new HandlerManager(null);
		AppController appView = new AppController(rpcService, loginService, eventBus);
		appView.go(RootPanel.get());
	}
	
}
