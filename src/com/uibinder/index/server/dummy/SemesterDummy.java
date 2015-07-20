package com.uibinder.index.server.dummy;

import java.util.ArrayList;
import java.util.List;

public class SemesterDummy {

	private int position = 0;
	private int year = 0;
	private int semester = 0;
	
	private List<SubjectDummy> subjects = new ArrayList<SubjectDummy>();;
	
	public SemesterDummy(){
	}
	
	public SemesterDummy(int position, int year, int semester) {
		this.setPosition(position);
		this.setYear(year);
		this.setSemester(semester);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}
	
	public void addSubjectDummy(SubjectDummy subjectDummy){
		getSubjects().add(subjectDummy);
	}

	public List<SubjectDummy> getSubjects() {
		return subjects;
	}
	
}
