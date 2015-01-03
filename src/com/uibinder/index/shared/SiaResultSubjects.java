package com.uibinder.index.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.uibinder.index.shared.control.Subject;

@SuppressWarnings("serial")
public class SiaResultSubjects extends SiaResult implements Serializable{

	private List<Subject> subjectList = new ArrayList<Subject>();
	private int totalAsignaturas = 0;
	private int numPaginas = 0;
	
	public SiaResultSubjects(){
	}
	
	public SiaResultSubjects(List<Subject> subjectList, int totalAsignaturas, int numPaginas) {
		this.subjectList = subjectList;
		this.totalAsignaturas = totalAsignaturas;
		this.numPaginas = numPaginas;
	}

	public List<Subject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}

	public int getTotalAsignaturas() {
		return totalAsignaturas;
	}

	public void setTotalAsignaturas(int totalAsignaturas) {
		this.totalAsignaturas = totalAsignaturas;
	}

	public int getNumPaginas() {
		return numPaginas;
	}

	public void setNumPaginas(int numPaginas) {
		this.numPaginas = numPaginas;
	}
	
}
