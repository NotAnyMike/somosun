package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.DndViewImpl;

public class DndPresenter implements Presenter {
	
	private final SUNServiceAsync rpcService;
	private final HandlerManager eventBus;
	private DndViewImpl view;
	
	public DndPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, DndViewImpl view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

}
