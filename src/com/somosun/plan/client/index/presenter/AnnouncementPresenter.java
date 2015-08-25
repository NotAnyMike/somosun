package com.somosun.plan.client.index.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.index.event.GenerateAcademicHistoryFromStringEvent;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.AnnouncementView;
import com.somosun.plan.client.index.view.AnnouncementViewImpl;

public class AnnouncementPresenter implements Presenter, AnnouncementView.Presenter {
	
	private final SUNServiceAsync rpcService;
	private final HandlerManager eventBus;
	private AnnouncementViewImpl view;
	
	public AnnouncementPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, AnnouncementViewImpl view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}


}
