package com.somosun.plan.client.index.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.AboutUsView;
import com.somosun.plan.client.index.view.AboutUsViewImpl;

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
