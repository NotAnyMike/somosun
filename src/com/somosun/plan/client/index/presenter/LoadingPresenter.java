package com.somosun.plan.client.index.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.LoadingView;

public class LoadingPresenter implements Presenter, LoadingView.Presenter {
	
	private LoadingView view = null;
	private SUNServiceAsync rpcService = null;
	private HandlerManager eventBus = null;

	public LoadingPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, LoadingView view){
		this.setRpcService(rpcService);
		this.setEventBus(eventBus);
		this.setView(view);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public LoadingView getLoadingView() {
		return view;
	}

	public void setView(LoadingView view) {
		this.view = view;
	}

	public HandlerManager getEventBus() {
		return eventBus;
	}

	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public SUNServiceAsync getRpcService() {
		return rpcService;
	}

	public void setRpcService(SUNServiceAsync rpcService) {
		this.rpcService = rpcService;
	}

	@Override
	public void setLabel(String s) {
		view.setLabel(s);
	}

}
