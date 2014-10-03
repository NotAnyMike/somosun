package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.view.TopBarView;
import com.uibinder.index.client.view.TopBarViewImpl;

public class TopBarPresenter implements Presenter, TopBarView.Presenter {
	private final HandlerManager eventBus;
	private TopBarViewImpl view;
	
	public TopBarPresenter(HandlerManager eventBus, TopBarViewImpl view){
		this.eventBus = eventBus;
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setNameOfThePage(String s) {
		view.setMainLabel(s);
	}

	@Override
	public void setUserName(String s) {
		view.setUserName(s);
	}

}
