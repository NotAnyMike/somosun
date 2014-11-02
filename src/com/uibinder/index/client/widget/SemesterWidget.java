package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class SemesterWidget extends VerticalPanel {
	
	private static final String[] SEMESTER_ROMAN = {"I", "II", "III", "IV", "V", "VI", 
		"VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII"};
	private static final String CREDITS_STRING = "créditos: ";
	
	private Image addImage = new Image();
	private Label creditsLabel = new Label();
	private Label semesterLabel = new Label();
	private HorizontalPanel bottomPart = new HorizontalPanel();
	private VerticalPanel subjectPanel = new SubjectPanel();
	
	public SemesterWidget (int semester, PlanPresenter planPresenter){
		
		semesterLabel = new Label(SEMESTER_ROMAN[semester]);
		addImage.setUrl("images/add.png");

		this.addStyleName("semesterPanel");
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.setVerticalAlignment(ALIGN_TOP);
		
		subjectPanel.addStyleName("mainPanelSemesterPanel");
		subjectPanel.setHorizontalAlignment(ALIGN_CENTER);
		subjectPanel.setVerticalAlignment(ALIGN_TOP);
		
		semesterLabel.addStyleName("semesterLabelSemesterPanel");
		
		addImage.addStyleName("addImageSemesterPanel");
		
		//This part is the little box of "credits: x"
		bottomPart.addStyleName("bottomPartSemesterPanel");
		bottomPart.setHorizontalAlignment(ALIGN_CENTER);
		bottomPart.setVerticalAlignment(ALIGN_MIDDLE);
		
		generateWidget();
	}
	
	private void generateWidget() {
		bottomPart.add(creditsLabel);
		bottomPart.add(addImage);
		subjectPanel.add(bottomPart);
		
		this.add(semesterLabel);
		this.add(subjectPanel);
	}

	public void setSemester(int semester) {
		semesterLabel.setText(SEMESTER_ROMAN[semester]);
	}
	
	public void setCredits(int credits) {
		creditsLabel.setText(CREDITS_STRING + credits);
	}

	public void clearSemester(){
		subjectPanel.clear();
	}
	
	public VerticalPanel getMainPanel(){
		return subjectPanel;
	}

	public void addSubject(SubjectWidget subject) {
		subjectPanel.add(subject);		
	}
}
