package com.uibinder.client.index.view;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.presenter.ContactUsPresenter;
import com.uibinder.client.index.presenter.PlanPresenter;

public class ContactUsViewImpl extends Composite implements ContactUsView{

	private ContactUsPresenter presenter;
	
	@UiField RadioButton suggestionRadioButton;
	@UiField RadioButton errorRadioButton;
	@UiField RadioButton otherRadioButton;
	
	@UiField AnchorButton clearButton;
	@UiField AnchorButton sendButton;
	
	@UiField AnchorButton yourWelcomeButton;
	@UiField AnchorButton takeMeHomeButton;
	
	@UiField HTMLPanel showThanks;
	
	@UiField TextArea messageTextArea;
	
	@UiField TextBox nameTextBox;
	@UiField TextBox subjectTextBox;
	
	private static ContactUsUiBinder uiBinder = GWT.create(ContactUsUiBinder.class);

	@UiTemplate("ContactUsView.ui.xml")
	interface ContactUsUiBinder extends UiBinder<Widget, ContactUsViewImpl> {
	}

	public ContactUsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(ContactUsPresenter presenter) {
		this.presenter = presenter;
		init();
	}

	public void init(){
		hideThanks();
	}

	@Override
	public void selectSuggestion() {
		suggestionRadioButton.setValue(true);
	}

	@Override
	public void selectError() {
		errorRadioButton.setValue(true);
	}

	@Override
	public void selectOther() {
		otherRadioButton.setValue(true);
	}
	
	public void clean(){
		messageTextArea.clear();
		subjectTextBox.clear();
		nameTextBox.clear();
	}

	@Override
	public void showThanks() {
		showThanks.setVisible(true);
	}
	
	@Override
	public void hideThanks() {
		showThanks.setVisible(false);
	}

	
	
	/*************** HANDLERS **************/
	
	@UiHandler("clearButton")
	public void onCleanButtonClick(ClickEvent e) {
		clean();
	}
	
	@UiHandler("yourWelcomeButton")
	public void onYourWelcome(ClickEvent e) {
		hideThanks();
	}
	
	@UiHandler("sendButton")
	public void onSendButtonClick(ClickEvent e) {
		clean();
		showThanks();
	}

}

		