package com.uibinder.index.client.presenter;

import com.uibinder.index.client.event.AboutUsEvent;
import com.uibinder.index.client.view.IndexView;
import com.uibinder.index.client.view.IndexViewImpl;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class IndexPresenter implements Presenter, IndexView.Presenter {
	private final HandlerManager eventBus;
	private IndexViewImpl view;
	
	public IndexPresenter(HandlerManager eventBus, IndexViewImpl view){
		this.eventBus = eventBus;
		this.view = view; 
		this.view.setPresenter(this);
	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
	
	@Override
	public void onAboutUsButtonClicked(){
		eventBus.fireEvent(new AboutUsEvent());
	}

}
