package com.uibinder.client.admin;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.client.index.AppController;
import com.uibinder.client.index.service.SUNServiceAsync;

public class AdminAppController implements ValueChangeHandler<String> {

	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	
	private String token;
	
	
	public AdminAppController(SUNServiceAsync rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
	}

	public void go(HasWidgets container){
		token = History.getToken();
		
		if(token.equals("")){
			History.newItem("index");
		}else{
			History.fireCurrentHistoryState();
		}
	}


	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		token = History.getToken();
		
		
	}

}
