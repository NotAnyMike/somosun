package com.uibinder.client.admin.presenter;

import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.client.admin.service.AdminServiceAsync;
import com.uibinder.client.admin.view.MessagesView;
import com.uibinder.client.admin.view.MessagesViewImpl;
import com.uibinder.client.index.presenter.Presenter;
import com.uibinder.client.index.service.SUNServiceAsync;
import com.uibinder.shared.control.Message;

public class MessagesPresenter implements Presenter, MessagesView.Presenter{

	MessagesViewImpl view;
	HandlerManager eventBus;
	SUNServiceAsync rpcService;
	AdminServiceAsync rpcAdminService;
	
	public MessagesPresenter(HandlerManager eventBus, SUNServiceAsync rpcService, AdminServiceAsync rpcAdminService, MessagesViewImpl view){
		this.view = view;
		this.eventBus = eventBus;
		this.rpcAdminService = rpcAdminService;
		this.rpcService = rpcService;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void addMessage(String fullMessage) {
		view.addMessage(fullMessage);
	}
	
	private String getStringFromMessage(Message m) {
		return "Message id:" + m.getId() + 
				"<br/>Username: '" + (m.getStudent() != null ? m.getStudent().getUsername() : "none") +  
				"'<br/>Name: " + m.getName() +
				"<br/>Type: " + m.getType() +
				"<br/>Topic: " + m.getTopic() + 
				"<br/>Message: " + m.getMessage();
	}

	@Override
	public void showAllMessage() {
		view.clearMessagesContainer();
		rpcAdminService.getAllMessages(new AsyncCallback<List<Message>>(){


			@Override
			public void onSuccess(List<Message> result) {
				for(Message m : result){
					String messageString = "";
					messageString = getStringFromMessage(m);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}

	public void showAllErrors() {
		view.clearMessagesContainer();
		rpcAdminService.getAllErrorMessages(new AsyncCallback<List<Message>>(){

			@Override
			public void onSuccess(List<Message> result) {
				for(Message m : result){
					String messageString = "";
					messageString = getStringFromMessage(m);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}

	public void showAllSuggestions() {
		view.clearMessagesContainer();
		rpcAdminService.getAllSuggestionMessages(new AsyncCallback<List<Message>>(){
			
			@Override
			public void onSuccess(List<Message> result) {
				for(Message m : result){
					String messageString = "";
					messageString = getStringFromMessage(m);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}

	public void showAllOthers() {
		view.clearMessagesContainer();
		rpcAdminService.getAllOtherMessages(new AsyncCallback<List<Message>>(){
			
			@Override
			public void onSuccess(List<Message> result) {
				for(Message m : result){
					String messageString = "";
					messageString = getStringFromMessage(m);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}

	public void showUsernameMessages(String username) {
		view.clearMessagesContainer();
		rpcAdminService.getUserMessages(username, new AsyncCallback<List<Message>>(){
			
			@Override
			public void onSuccess(List<Message> result) {
				for(Message m : result){
					String messageString = "";
					messageString = getStringFromMessage(m);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}

	
	public void showId(String id) {
		view.clearMessagesContainer();
		rpcAdminService.getMessageById(Long.valueOf(id), new AsyncCallback<Message>(){
			
			@Override
			public void onSuccess(Message result) {
				if(result != null){					
					String messageString = "";
					messageString = getStringFromMessage(result);
					view.addMessage(messageString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error showing all the messages");
			}
			
		});
	}
	
}
