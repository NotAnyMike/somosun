package com.uibinder.index.client;

import com.uibinder.index.client.AppController;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UiBinderIndex implements EntryPoint {

	@Override
	public void onModuleLoad() {
		SUNServiceAsync rpcService = GWT.create(SUNService.class);
		HandlerManager eventBus = new HandlerManager(null);
		AppController appView = new AppController(rpcService, eventBus);
		appView.go(RootPanel.get());
	}
	
}
