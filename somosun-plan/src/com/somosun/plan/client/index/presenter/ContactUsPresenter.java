package com.somosun.plan.client.index.presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.ContactUsView;
import com.somosun.plan.client.index.view.ContactUsViewImpl;
import com.somosun.plan.shared.control.Message;

public class ContactUsPresenter implements Presenter, ContactUsView.Presenter{

	private ContactUsViewImpl view;
	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	
	public ContactUsPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, ContactUsViewImpl view){
		this.view = view;
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		view.setPresenter(this);
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		view.hideThanks();
	}

	@Override
	public void setType(String type) {
		if(type == null || type.isEmpty() || type.equals("suggestion")){
			view.selectSuggestion();
		}else if(type.equals("error")){
			view.selectError();
		}else{
			view.selectOther();
		}
	}

	@Override
	public void sendMessage(String name, String subject, String type, String message) {
		rpcService.saveMessage(name, subject, type, message, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Message could not be sent");
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("Message sent");
				view.clean();
			}
			
		});
	}

}
