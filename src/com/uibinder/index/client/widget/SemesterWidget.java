package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.widget.SubjectWidget;

public class SemesterWidget extends VerticalPanel {
	
	private static final String[] SEMESTER_ROMAN = {"I", "II", "III", "IV", "V", "VI", 
		"VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII"};
	
	private int semester = 0;
	private int subjects = 0;
	private List<SubjectWidget> subjectList = new ArrayList<SubjectWidget>();
	private int credits = 0;
	
	private Image addImage = new Image();
	private Label creditsLabel = null;
	private Label semesterLabel = null;
	private HorizontalPanel bottomPart = new HorizontalPanel();
	private VerticalPanel subjectPanel = new SubjectPanel();
	
	public SemesterWidget (int semester){
		this.semester = semester;
		
		semesterLabel = new Label(SEMESTER_ROMAN[semester]);
		creditsLabel = new Label("cr: " + Integer.toString(credits));
		addImage.setUrl("images/add.png");

		this.addStyleName("semesterPanel");
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.setVerticalAlignment(ALIGN_TOP);
		
		subjectPanel.addStyleName("mainPanelSemesterPanel");
		subjectPanel.setHorizontalAlignment(ALIGN_CENTER);
		subjectPanel.setVerticalAlignment(ALIGN_TOP);
		
		semesterLabel.addStyleName("semesterLabelSemesterPanel");
		
		addImage.setStyleName("addImageSemesterPanel");
		
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
		this.semester = semester;
	}
	
	private void updateCredits() {
		creditsLabel.setText("cr: " + credits);
	}

	public void clearSemester(){
		subjectList.clear();
		subjectPanel.clear();
		subjects = 0;
	}
	
	public void deleteSubject(SubjectWidget subject){
		if(subjectList.contains(subject) == true){
			subjectList.remove(subject);
			subjects--;
		}
	}

	public List<SubjectWidget> getSubjectList() {
		return subjectList;
	}
	
	public int getSubjectsNumbers(){
		return subjects;
	}

	public SubjectWidget getSubject(int subject) {
		return subjectList.get(subject);
	}
	
	public VerticalPanel getMainPanel(){
		return subjectPanel;
	}
	
	public int getSemester(){
		return semester;
	}

	public void addSubject(SubjectWidget subject) {
		subjectList.add(subject);
		subjectPanel.add(subject);
		
		this.credits+= subject.getCredits();
		subjects++;
		
		updateCredits();
	}
}
