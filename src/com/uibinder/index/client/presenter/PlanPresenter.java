package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.view.PlanView;
import com.uibinder.index.client.view.PlanViewImpl;

public class PlanPresenter implements Presenter, PlanView.Presenter {
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	
	public PlanPresenter(HandlerManager eventBus, PlanViewImpl view){
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
