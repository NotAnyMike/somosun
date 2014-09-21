package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.view.AboutGedadView;
import com.uibinder.index.client.view.AboutGedadViewImpl;

public class AboutGedadPresenter implements Presenter, AboutGedadView.Presenter {
	private final HandlerManager eventBus;
	private AboutGedadViewImpl view;
	
	public AboutGedadPresenter(HandlerManager eventBus, AboutGedadViewImpl view){
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
