package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.AboutUsView;
import com.uibinder.index.client.view.AboutUsViewImpl;

public class AboutUsPresenter implements Presenter, AboutUsView.Presenter {
	
	private SUNServiceAsync rpcService;
	private HandlerManager eventBus;
	private AboutUsViewImpl view;
	
	public AboutUsPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, AboutUsViewImpl view){
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
