package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

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
	
	private Button addButton = new Button();
	private Label creditsLabel = new Label();
	private Label semesterLabel = new Label();
	private HorizontalPanel bottomPart = new HorizontalPanel();
	private VerticalPanel subjectPanel = new SubjectPanel();
	private VerticalPanel bottomBigPanel = new VerticalPanel();
	private Button deleteSemesterButton = new Button("Eliminar");
	
	public SemesterWidget (int semester, PlanPresenter planPresenter){
		
		semesterLabel = new Label(SEMESTER_ROMAN[semester]);

		this.addStyleName("semesterPanel");
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.setVerticalAlignment(ALIGN_TOP);
		
		subjectPanel.addStyleName("mainPanelSemesterPanel");
		subjectPanel.setHorizontalAlignment(ALIGN_CENTER);
		subjectPanel.setVerticalAlignment(ALIGN_TOP);
		
		semesterLabel.addStyleName("semesterLabelSemesterPanel");
		
		addButton.setStyleName("addImageSemesterPanel");
		addButton.setIcon(IconType.PLUS);
		addButton.setSize(ButtonSize.EXTRA_SMALL);
		addButton.setType(ButtonType.SUCCESS);
		addButton.setIconSize(IconSize.LARGE);
		addButton.setTitle("Agregar Clase");
		
		//This part is the little box of "credits: x"
		bottomPart.addStyleName("bottomPartSemesterPanel");
		bottomPart.setHorizontalAlignment(ALIGN_CENTER);
		bottomPart.setVerticalAlignment(ALIGN_MIDDLE);
		
		//arranging everything about the Delete semester button
		/*deleteSemesterButton.addStyleName("btn-warning");
		deleteSemesterButton.setStylePrimaryName("btn");*/
		deleteSemesterButton.asWidget().getElement().setAttribute("semester", Integer.toString(semester));
		deleteSemesterButton.setIcon(IconType.TIMES_CIRCLE);
		deleteSemesterButton.setType(ButtonType.DANGER);
		deleteSemesterButton.setSize(ButtonSize.EXTRA_SMALL);
		
		generateWidget();
	}
	
	private void generateWidget() {
		bottomPart.add(creditsLabel);
		bottomPart.add(addButton);
		
		bottomBigPanel.setHorizontalAlignment(ALIGN_CENTER);
		
		bottomBigPanel.add(bottomPart);
		bottomBigPanel.add(deleteSemesterButton);
		
		subjectPanel.add(bottomBigPanel);
		
		this.add(semesterLabel);
		this.add(subjectPanel);
	}

	public void setSemester(int semester) {
		semesterLabel.setText(SEMESTER_ROMAN[semester]);
		deleteSemesterButton.asWidget().getElement().setAttribute("semester", Integer.toString(semester));
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
	
	public Button getDeleteSemesterButton(){
		return deleteSemesterButton;
	}
	
	public Button getAddButton(){
		return addButton;
	}
}
