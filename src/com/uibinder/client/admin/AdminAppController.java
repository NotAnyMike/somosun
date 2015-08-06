package com.uibinder.client.admin;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.uibinder.client.admin.presenter.IndexPresenter;
import com.uibinder.client.admin.presenter.MessagesPresenter;
import com.uibinder.client.admin.service.AdminServiceAsync;
import com.uibinder.client.admin.view.IndexViewImpl;
import com.uibinder.client.admin.view.MessagesViewImpl;
import com.uibinder.client.index.AppController;
import com.uibinder.client.index.service.SUNServiceAsync;

public class AdminAppController implements ValueChangeHandler<String> {

	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	private AdminServiceAsync rpcAdminService;
	
	private IndexPresenter indexPresenter;
	private MessagesPresenter messagesPresenter;
	
	private IndexViewImpl indexView;
	private MessagesViewImpl messagesView;
	
	private String token;
	
	public AdminAppController(SUNServiceAsync rpcService, AdminServiceAsync rpcAdminService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.rpcAdminService = rpcAdminService;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
	}

	public void go(HasWidgets container){
		token = History.getToken();
		
		if(token.matches("(index)|(^$)") == true){
			History.fireCurrentHistoryState();
		}else{
			History.newItem("index");
		}
	}


	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		token = History.getToken();
		
		if(token.matches("(index)|(^$)") == true){
			if(indexView == null){
				indexView = new IndexViewImpl();
			}
			if(indexPresenter == null){
				indexPresenter = new IndexPresenter(rpcService, rpcAdminService, indexView);
			}
			indexPresenter.go(RootPanel.get("container"));
		}else if(token.contains("messages")){
			if(messagesView == null){
				messagesView = new MessagesViewImpl();
			}
			if(messagesPresenter == null){
				messagesPresenter = new MessagesPresenter(eventBus, rpcService, rpcAdminService, messagesView);
			}
			messagesPresenter.go(RootPanel.get("container"));
			if(token.contains("action=showAll")) messagesPresenter.showAllMessage();
			else if(token.contains("action=showError")) messagesPresenter.showAllErrors();
			else if(token.contains("action=showSuggestion")) messagesPresenter.showAllSuggestions();
			else if(token.contains("action=showOther")) messagesPresenter.showAllOthers();
			else if(token.contains("action=showId")) messagesPresenter.showId(token.substring(token.indexOf("id=")+3));
			else if(token.contains("action=showUsername")) messagesPresenter.showUsernameMessages(token.substring(token.indexOf("username=")+9));
		}
		else{
			History.newItem("index");
		}
	}

}
