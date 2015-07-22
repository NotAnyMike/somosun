package com.uibinder.client.index.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.client.index.service.SUNServiceAsync;
import com.uibinder.client.index.view.TopBarView;
import com.uibinder.client.index.view.TopBarViewImpl;

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
		if(phrase != "" && phrase != null){	//for the case when there will be no phrase but just a message or a name of the page	
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

	@Override
	public void setLogInUrl(String s) {
		view.setLogInUrl(s);
	}

	@Override
	public void setLogOutUrl(String s) {
		view.setLogOutUrl(s);
	}

	public void showAdminLink(boolean admin) {
		view.showAdminLink(admin);
	}

}
