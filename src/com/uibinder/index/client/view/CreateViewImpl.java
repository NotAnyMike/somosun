package com.uibinder.index.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CreateViewImpl extends Composite implements CreateView {
	
	private Presenter presenter;
	
	@UiField Button continueButton;
	@UiField Button continueDefaultButton;
	@UiField Button resetButton;
	@UiField TextBox textBoxCreate;
	@UiField ListBox listBoxCreate;

	private static CreateViewUiBinder uiBinder = GWT
			.create(CreateViewUiBinder.class);

	@UiTemplate("CreateView.ui.xml")
	interface CreateViewUiBinder extends UiBinder<Widget, CreateViewImpl> {
	}

	public CreateViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void addCareerToListBox(String career, String value) {
		listBoxCreate.addItem(career, value);
	}

	@Override
	public void clearListBox() {
		listBoxCreate.clear();
	}
	
	public void clearTextBoxCreate(){
		textBoxCreate.setValue("");
	}
	
	public void setTextOfContinueDefaultButton(String text) {
		continueDefaultButton.setText(text);
	}

	/********************* Firing Events *********************/
	
	@UiHandler("resetButton")
	void onResetButtonClicked(ClickEvent event){
		if(presenter != null){			
			presenter.onResetButtonClicked();
		}
	}
	
	@UiHandler("listBoxCreate")
	void onListBoxCreateChange(ChangeEvent event){
		if(presenter != null){			
			presenter.onListBoxCreateChange(listBoxCreate.getItemText(listBoxCreate.getSelectedIndex()));
		}
	}
	
	@UiHandler("continueButton")
	void onContinueButtonClicked(ClickEvent event){
		String academicHistory = textBoxCreate.getValue();
		if(presenter != null && academicHistory != null && academicHistory != ""){
			presenter.onContinueButtonClicked(academicHistory);
		}
	}
	
	@UiHandler("continueDefaultButton")
	void onContinueDefaultButton(ClickEvent event){
		if(presenter!=null){			
			presenter.onContinueDefaultButtonClick(listBoxCreate.getSelectedValue());
		}
	}

	@Override
	public Button getContinueDefaultButton() {
		return continueDefaultButton;
	}

	@Override
	public String getCurrentDefaultCareerValue() {
		return listBoxCreate.getSelectedValue();
	}


}
