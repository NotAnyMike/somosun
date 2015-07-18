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
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

public class WarningDeleteSubjectViewImpl extends Composite implements WarningDeleteSubjectView {
	
	@UiField Label subjectName;
	@UiField Label subjectGrade;
	@UiField Label subjectCode;
	@UiField Label subjectOblig;
	@UiField Label subjectCredits;
	@UiField Label subjectType;
	@UiField Button cancelButton;
	@UiField Button deleteButton;
	
	private SubjectValues sV;
	
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
		presenter.confirmedDeleteSubject(sV);
	}
	
	public void onCancelClicked(){
		this.hideIt();
	}

	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void hideIt() {
		this.asWidget().setVisible(false);
	}

	@Override
	public void showIt() {
		this.asWidget().setVisible(true);
		deleteButton.setFocus(true);
	}

	@Override
	public void setSubject(SubjectValues sV, Subject s) {
		this.sV = sV;
		subjectName.setText(s.getName());
		subjectCode.setText(s.getCode());
		subjectGrade.setText(Double.toString(sV.getGrade()));
		subjectCredits.setText(Integer.toString(s.getCredits()));
		subjectOblig.setText((sV.getComplementaryValues().isMandatory() == true ? "Oblig: Si" : "Oblig: No"));
		//Type = 0 Leveling, 1 Foundations, 2 Disciplinary, 3 Free Election, 4 Add to post-grade.
		switch(sV.getComplementaryValues().getTypology()){
			case "N":
			case "n":
				subjectType.setText("N");
				break;
			case "D":
			case "d":
				subjectType.setText("D");
				break;
			case "L":
			case "l":
				subjectType.setText("L");
				break;
		}
		//TODO: complete the switch with all the other values
	}

}
