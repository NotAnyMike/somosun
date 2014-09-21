package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlanWidget extends HorizontalPanel {
	
	private List<SemesterWidget> semesterList = new ArrayList<SemesterWidget>();
	private int semesters = 0;
	private String carrer;
	
	public PlanWidget(){
		this.addStyleName("planPanel");
	}

	public void addSemester() {
		SemesterWidget semester = new SemesterWidget(semesters);
		this.add(semester);
		semesterList.add(semester);
		semesters++;
	}
	
	public List<SemesterWidget> getSemesterList(){
		return semesterList;
	}
	
	public int getNumberOfSemesters(){
		return semesters;
	}
	
	public SemesterWidget getSemester(int i){
		if(semesters != 0){
			return semesterList.get(i);			
		} else {
			return null;
		}
	}
	
	public SubjectWidget getSubject(int semester, int subject){
		if(semesters >= semester && semesterList.get(semester).getSubjectsNumbers() >= subject){
			return semesterList.get(semester).getSubject(subject);
		}else{
			return null;
		}
	}
	
	public VerticalPanel getMainPanelFromSemester(int semester){
		if(semesters >= semester){
			return semesterList.get(semester).getMainPanel();
		}else{
			return null;
		}
	}
	
	public List<SubjectWidget> getSubjectList(int semester){
		return semesterList.get(semester).getSubjectList();
	}

	public void addSubject(int semester, SubjectWidget subject) {
		if(semesters >= semester){
			semesterList.get(semester).addSubject(subject);
		}
	}
	
}
