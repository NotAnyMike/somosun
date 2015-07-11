package com.uibinder.index.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.uibinder.index.shared.SomosUNUtils;
import com.uibinder.index.shared.control.SubjectGroup;

public class DefaultSubjectCreationViewImpl extends Composite implements DefaultSubjectCreationView{

	private Presenter presenter;
	
	@UiField Button createDefaultSubjectButton;
	@UiField Button cancelDefaultSubjectButton;
	@UiField ListBox listBoxSubjectGroups;
	@UiField TextBox creditsInput;
	
	private static DefaultSubjectCreationViewUiBinder uiBinder = GWT
			.create(DefaultSubjectCreationViewUiBinder.class);

	@UiTemplate("DefaultSubjectCreationView.ui.xml")
	interface DefaultSubjectCreationViewUiBinder extends
			UiBinder<Widget, DefaultSubjectCreationViewImpl> {
	}

	public DefaultSubjectCreationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		addEntryToList(SomosUNUtils.LIBRE_CODE);
		hideIt();
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@Override
	public void addEntryToList(String name) {
		listBoxSubjectGroups.addItem(name, name);
		
	}

	@Override
	public void clear() {
		listBoxSubjectGroups.clear();
		addEntryToList(SomosUNUtils.LIBRE_CODE);
		creditsInput.setText("");
		
	}

	@Override
	public void hideIt() {
		this.setVisible(false);
	}

	@Override
	public void showIt() {
		this.setVisible(true);
	}

	@Override
	public void setSemester(String s) {
		createDefaultSubjectButton.getElement().setAttribute("semester", s);		
	}

	@Override
	public void addEntriesToList(List<SubjectGroup> list) {
		for(SubjectGroup sG : list){
			addEntryToList(sG.getName());
		}
		
	}
	
	@UiHandler("createDefaultSubjectButton")
	public void onCreateDefaultSubjectButtonClick(ClickEvent event){
		presenter.onCreateDefaultSubjectButtonClick(listBoxSubjectGroups.getSelectedValue(), creditsInput.getText(), createDefaultSubjectButton.getElement().getAttribute("semester"));
	}
	
	@UiHandler("cancelDefaultSubjectButton")
	public void onCreateDefaultSubjectButton(ClickEvent event){
		hideIt();
	}

}
