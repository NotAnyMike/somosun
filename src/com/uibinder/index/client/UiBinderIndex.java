package com.uibinder.index.client;

import com.uibinder.index.client.AppController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UiBinderIndex implements EntryPoint {

	@Override
	public void onModuleLoad() {
		HandlerManager eventBus = new HandlerManager(null);
		AppController appView = new AppController(eventBus);
		appView.go(RootPanel.get());
	}
	
}
