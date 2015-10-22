package com.somosun.plan.client.index;

import com.somosun.plan.client.index.AppController;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	@Override
	public void onModuleLoad() {
		SUNServiceAsync rpcService = GWT.create(SUNService.class);
		HandlerManager eventBus = new HandlerManager(null);
		AppController appView = new AppController(rpcService, eventBus);
		appView.go(RootPanel.get());
	}
	
}
