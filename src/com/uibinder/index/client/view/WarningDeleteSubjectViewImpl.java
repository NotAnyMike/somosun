package com.uibinder.index.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;

public class WarningDeleteSubjectViewImpl extends Composite implements WarningDeleteSubjectView {
	
	@UiField Label subjectName;
	@UiField Button cancelButton;
	@UiField Button deleteButton;
	
	private String code;
	
	private PlanPresenter presenter;

	private static warningDeleteSubjectUiBinder uiBinder = GWT
			.create(warningDeleteSubjectUiBinder.class);

	@UiTemplate("WarningDeleteSubjectView.ui.xml")
	interface warningDeleteSubjectUiBinder extends
			UiBinder<Widget, WarningDeleteSubjectViewImpl> {
	}

	public WarningDeleteSubjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		//Adding the event handdles for cancel and delete
		cancelButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onCancelClicked();				
			}
		});
		deleteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onDeleteClicked();
			}});
	}
		
	private void onDeleteClicked() {
		presenter.confirmedDeleteSubject(code);
	}
	
	public void onCancelClicked(){
		this.hideIt();
	}

	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSubjectName(String code, String name) {
		subjectName.setText("Eliminar " + name + "?");
		this.code = code;
	}

	@Override
	public void hideIt() {
		this.asWidget().setVisible(false);
	}

	@Override
	public void showIt() {
		this.asWidget().setVisible(true);
	}

}
