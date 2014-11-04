package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.AnnouncementView;
import com.uibinder.index.client.view.AnnouncementViewImpl;

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
