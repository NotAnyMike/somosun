package com.uibinder.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uibinder.shared.control.Subject;

@SuppressWarnings("serial")
public class SiaResultSubjects extends SiaResult implements Serializable{

	private List<Subject> subjectList = new ArrayList<Subject>();
	private Map<Subject, String> typology = new HashMap<Subject, String>();
	private int totalAsignaturas = 0;
	private int numPaginas = 0;
	private int page = 0;
	
	public SiaResultSubjects(){
	}
	
	public SiaResultSubjects(List<Subject> subjectList, Map<Subject, String> typology, int totalAsignaturas, int numPaginas, int page) {
		this.setSubjectList(subjectList);
		this.setTypology(typology);
		this.setTotalAsignaturas(totalAsignaturas);
		this.setNumPaginas(numPaginas);
		this.setPage(page);
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Map<Subject, String> getTypology() {
		return typology;
	}

	public void setTypology(Map<Subject, String> typology) {
		this.typology = typology;
	}

	/**
	 * This method will add an empty string to the Map subject type(String) 
	 * <br></br>
	 * @param subject
	 */
	public void addSubject(Subject subject) {
		this.subjectList.add(subject);
		this.typology.put(subject, "");
	}
	
	public String getTypologyForASubject(String subjectCode){
		String typologyToReturn = null;
		
		for(Subject subjectT : typology.keySet()){
			if(subjectT.getCode().equals(subjectCode)){
				typologyToReturn = typology.get(subjectT);
				break;
			}
		}
		
		return typologyToReturn;
	}
}
