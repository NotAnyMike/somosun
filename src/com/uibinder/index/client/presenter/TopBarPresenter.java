package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.TopBarView;
import com.uibinder.index.client.view.TopBarViewImpl;

public class TopBarPresenter implements Presenter, TopBarView.Presenter {
	
	private final SUNServiceAsync rpcService;
	private final HandlerManager eventBus;
	private TopBarViewImpl view;
	
	public TopBarPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, TopBarViewImpl view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setUserName(String s) {
		view.setUserName(s);
	}

	@Override
	public void setNameOfThePage(String phrase, String author) {
		setMainTitle(phrase, author);
	}

	@Override
	public void setNameOfThePage(String phrase) {
		setMainTitle(phrase, "");
	}

	private void setMainTitle(String phrase, String author) {
		if(phrase != "" && phrase != null){			
			phrase = "'" + phrase + "'";
		}
		if(author==""){
			author=phrase;
		}else{
			author = phrase + " - " + author;
		}
		view.setMainLabel(phrase);
		view.setMainLabelTitle(author);
	}

}
