package com.somosun.plan.client.index.widget;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.somosun.plan.client.index.event.GradeChangeEvent;
import com.somosun.plan.client.index.event.GradeChangeEventHandler;
import com.somosun.plan.client.index.presenter.PlanPresenter;

public class SemesterWidget extends VerticalPanel implements HasChangeHandlers {
	
	private static final String[] SEMESTER_ROMAN = {"I", "II", "III", "IV", "V", "VI", 
		"VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"};
	private static final String CREDITS_STRING = "créditos: ";
	
	private Button addButton = new Button();
	private Label creditsLabel = new Label();
	private Label semesterLabel = new Label();
	private HorizontalPanel bottomPart = new HorizontalPanel();
	private SubjectPanel subjectPanel = new SubjectPanel();
	private VerticalPanel bottomBigPanel = new VerticalPanel();
	private Button deleteSemesterButton = new Button("Eliminar semestre");
	
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
		addButton.asWidget().getElement().setAttribute("semester", Integer.toString(semester));
		
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
		
		subjectPanel.addHandler(new GradeChangeEventHandler(){

			@Override
			public void onPlanChanges(String triggerer) {
				fireEvent(new GradeChangeEvent(triggerer));
			}
			
		}, GradeChangeEvent.TYPE);

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
		addButton.asWidget().getElement().setAttribute("semester", Integer.toString(semester));
	}
	
	public void setCredits(int credits) {
		creditsLabel.setText(CREDITS_STRING + credits);
	}

	public void clearSemester(){
		subjectPanel.clear();
		this.fireEvent(new GradeChangeEvent("SemesterClear"));
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
	
	public void setAsCurrent(){
		this.getElement().addClassName("current");
	}
	
	public void unsetAsCurrent(){
		this.getElement().removeClassName("current");
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}
}
