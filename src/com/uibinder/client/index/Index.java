package com.uibinder.client.index;

import com.uibinder.client.index.AppController;
import com.uibinder.client.index.service.SUNService;
import com.uibinder.client.index.service.SUNServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
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
