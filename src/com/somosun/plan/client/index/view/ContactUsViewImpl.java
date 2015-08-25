package com.somosun.plan.client.index.view;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.SubmitButton;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.ContactUsPresenter;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.values.MessageTypeCodes;

public class ContactUsViewImpl extends Composite implements ContactUsView{

	private ContactUsPresenter presenter;
	
	@UiField RadioButton suggestionRadioButton;
	@UiField RadioButton errorRadioButton;
	@UiField RadioButton otherRadioButton;
	
	@UiField Button clearButton;
	@UiField SubmitButton sendButton;
	
	@UiField AnchorButton yourWelcomeButton;
	@UiField AnchorButton takeMeHomeButton;
	
	@UiField HTMLPanel showThanks;
	
	@UiField TextArea messageTextArea;
	
	@UiField TextBox nameTextBox;
	@UiField TextBox subjectTextBox;
	
	@UiField ButtonGroup radioButtonGroup;
	
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
		
		nameTextBox.setFocus(true);
		
		suggestionRadioButton.setFormValue(MessageTypeCodes.SUGGESTION);
		errorRadioButton.setFormValue(MessageTypeCodes.ERROR);
		otherRadioButton.setFormValue(MessageTypeCodes.OTHER);
		
		nameTextBox.getElement().setAttribute("required", "required");
		subjectTextBox.getElement().setAttribute("required", "required");
		messageTextArea.getElement().setAttribute("required", "required");
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
		yourWelcomeButton.setFocus(true);
	}
	
	@Override
	public void hideThanks() {
		showThanks.setVisible(false);
		nameTextBox.setFocus(true);
	}

	private String getCheckBoxesValue() {
		if(suggestionRadioButton.getValue() == true) return	suggestionRadioButton.getFormValue();
		else if(errorRadioButton.getValue() == true) return errorRadioButton.getFormValue();
		else return otherRadioButton.getFormValue();
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
		if(nameTextBox.getValue() != null && nameTextBox.getValue().isEmpty() == false &&
				subjectTextBox.getValue() != null && subjectTextBox.getValue().isEmpty() == false &&
				messageTextArea.getValue() != null && messageTextArea.getValue().isEmpty() == false){			
			e.preventDefault();
			showThanks();
			presenter.sendMessage(nameTextBox.getValue(), subjectTextBox.getValue(), getCheckBoxesValue(), messageTextArea.getValue());
		}
	}

}

		