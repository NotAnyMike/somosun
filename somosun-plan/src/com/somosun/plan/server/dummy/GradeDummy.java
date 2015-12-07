package com.somosun.plan.server.dummy;

import java.util.ArrayList;
import java.util.List;

public class GradeDummy {

	private Double oldGrade = null;
	private Double newGrade = null;
	private Long subjectId = null;
	private Long professorId = null;
	private Double semesterNumber = null;
	
	public GradeDummy(Double oldGrade, Double newGrade, Long subjectId, Long professorId, Double semesterNumber) {
		super();
		this.oldGrade = oldGrade;
		this.newGrade = newGrade;
		this.subjectId = subjectId;
		this.professorId = professorId;
		this.semesterNumber = semesterNumber;
	}
	
	public Double getOldGrade() {
		return oldGrade;
	}
	public void setOldGrade(Double oldGrade) {
		this.oldGrade = oldGrade;
	}
	public Double getNewGrade() {
		return newGrade;
	}
	public void setNewGrade(Double newGrade) {
		this.newGrade = newGrade;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	
	public static List<GradeDummy> sortBySubjectId(List<GradeDummy> originalList){
		return sortByCallable(0, originalList);
	}
	
	public static List<GradeDummy> sortByProfessorId(List<GradeDummy> originalList){
		return sortByCallable(1, originalList);
	}
	
	public static List<GradeDummy> sortBySemester(List<GradeDummy> originalList){
		return sortByCallable(2, originalList);
	}
	
	/**
	 * 
	 * @param type: 0 = sort by subjectId, 1 = sort by professorID, 2 = sortBySemester
	 * @param originalList
	 * @return
	 */
	private static List<GradeDummy> sortByCallable(int type, List<GradeDummy> originalList){
		/*
		 * 1. Divide the list in two
		 * 2. Send recursively both to this method only if the list contains more than one entry
		 * 3. Take the two lists and merge them
		 * 3.1. for(sum of the size of both lists)
		 * 3.2. if x > y add x to the list to return otherwise add y to it, and continue
		 * 4. Return the list to return
		 */
		List<GradeDummy> listToReturn = new ArrayList<GradeDummy>();
		if(originalList != null && originalList.size() != 0){			
			
			List<GradeDummy> listOne = new ArrayList<GradeDummy>();
			List<GradeDummy> listTwo = new ArrayList<GradeDummy>();
			int half = originalList.size()/2;
			
			for(int x = 0; x < originalList.size(); x++){
				if(x < half) listOne.add(originalList.get(x));
				else listTwo.add(originalList.get(x));
			}
			
			if(listOne.isEmpty() == false && listOne.size() != 1) listOne = sortByCallable(type, listOne);
			if(listTwo.isEmpty() == false && listTwo.size() != 1) listTwo = sortByCallable(type, listTwo);
			
			int one = 0;
			int two = 0;
			for(int x = 0; x < originalList.size(); x++){
				if(
						(listOne.size() > one) && 
						(listTwo.size() <= two ||
						(
								(listOne.get(one).getSubjectId() >= listTwo.get(two).getSubjectId() && type == 0) ||
								(((listOne.get(one).getProfessorId() == null || listTwo.get(two).getProfessorId() == null) || (listOne.get(one).getProfessorId() >= listTwo.get(two).getProfessorId())) && type == 1) ||
								(listOne.get(one).getSemesterNumber() >= listTwo.get(two).getSemesterNumber() && type == 2)
								))) {
					listToReturn.add(listOne.get(one));
					one++;
				}
				else {
					listToReturn.add(listTwo.get(two));
					two ++;
				}
			}
		}
		
		return listToReturn;
	}
	public Long getProfessorId() {
		return professorId;
	}
	public void setProfessorId(Long professorId) {
		this.professorId = professorId;
	}
	public Double getSemesterNumber() {
		return semesterNumber;
	}
	public void setSemesterNumber(Double semesterNumber) {
		this.semesterNumber = semesterNumber;
	}
	
}
