package com.somosun.plan.client.admin.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.admin.presenter.MessagesPresenter;

public class MessagesViewImpl extends Composite implements MessagesView{

	private static MessagesViewUiBinder uiBinder = GWT.create(MessagesViewUiBinder.class);

	private MessagesPresenter presenter;
	
	@UiField VerticalPanel messagesToShowVerticalPanel;
	
	@UiTemplate("MessagesView.ui.xml")
	interface MessagesViewUiBinder extends UiBinder<Widget, MessagesViewImpl> {
	}
	
	public MessagesViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(MessagesPresenter presenter) {
		this.presenter = presenter;
		init();
	}
	
	public void init(){
		messagesToShowVerticalPanel.setStyleName("table table-striped");
	}

	@Override
	public void addMessage(String fullMessage) {
		messagesToShowVerticalPanel.add(new HTML(fullMessage));
	}

	public void clearMessagesContainer() {
		messagesToShowVerticalPanel.clear();
	}

}
