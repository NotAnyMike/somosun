package com.somosun.plan.server;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;

public class SomosUNServerUtils {

	public static void createGradeUpdaterTask(Teacher teacher, SemesterValue semesterValue, Double oldGrade, Double newGrade, Subject subject){
		
		if(subject != null){
			
			/****** add the oldGrade and the newGrade with the group to a cron job *******/
			Queue q = QueueFactory.getQueue("updateSubjectGradePullQueue");
			String professorId = (teacher == null ? "" : "" + teacher.getIdSun());
			String semester = (semesterValue == null ? "" : semesterValue.toStringDouble().toString());
			q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).param("old-grade", (oldGrade == null ? "" : oldGrade.toString())).param("new-grade", (newGrade == null ? "" : newGrade.toString())).param("subject-id", "" + subject.getId()).param("professor-id", professorId).param("semester", semester));
			
		}
		
	}
	
}
