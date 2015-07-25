package com.uibinder.client.admin;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.uibinder.client.admin.presenter.IndexPresenter;
import com.uibinder.client.admin.service.AdminServiceAsync;
import com.uibinder.client.admin.view.IndexViewImpl;
import com.uibinder.client.index.AppController;
import com.uibinder.client.index.service.SUNServiceAsync;

public class AdminAppController implements ValueChangeHandler<String> {

	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	private AdminServiceAsync rpcAdminService;
	
	private IndexPresenter indexPresenter;
	private IndexViewImpl indexView;
	
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
		}else{
			History.newItem("index");
		}
	}

}