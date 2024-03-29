package com.somosun.plan.server.cronJob;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.somosun.plan.server.dao.GroupDao;
import com.somosun.plan.server.dao.ScoreDao;
import com.somosun.plan.server.dao.SubjectDao;
import com.somosun.plan.server.dao.TeacherDao;
import com.somosun.plan.server.dummy.GradeDummy;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;

public class GradeUpdaterCronJob {
	
	private static Logger log = Logger.getLogger("GradeUpdaterCronJob");

	public static void updateAllGrades(){
		
		List<GradeDummy> list = new ArrayList<GradeDummy>();
		
		Queue q = QueueFactory.getQueue("updateSubjectGradePullQueue");
		List<TaskHandle> tasks = q.leaseTasks(600, TimeUnit.SECONDS, 1000);
		for(TaskHandle task : tasks){
			try {
				
				String oldGradeString = task.extractParams().get(0).getValue();
				String newGradeString = task.extractParams().get(1).getValue();
				String professorIdString = task.extractParams().get(3).getValue();
				String semesterString = task.extractParams().get(4).getValue();
				
				Double oldGrade = (oldGradeString.isEmpty() ? null : Double.valueOf(oldGradeString));
				Double newGrade = (newGradeString.isEmpty() ? null : Double.valueOf(newGradeString));
				Long subjectId = Long.valueOf(task.extractParams().get(2).getValue());
				Long professorId = (professorIdString.isEmpty() ? null : Long.valueOf(professorIdString));
				Double semester = (semesterString.isEmpty() ? null : Double.valueOf(semesterString));
				
				list.add(new GradeDummy(oldGrade, newGrade, subjectId, professorId, semester));
				
			} catch (UnsupportedEncodingException | UnsupportedOperationException e) {
				e.printStackTrace();
				log.warning("Error while reading the parameters of the task from the pull queue: " + e.toString());
			}
			q.deleteTask(task.getName());
			log.info("Task deleted");
		}
		
		//sorting the list
		list = GradeDummy.sortBySubjectId(list);
		
		//dividing the list and sorting by groupId
		List<GradeDummy> listWithSameSubject = new ArrayList<GradeDummy>();
		Long lastSubjectId = null;
		GroupDao groupDao = new GroupDao();
		ScoreDao scoreDao = new ScoreDao();
		SubjectDao subjectDao = new SubjectDao();
		for(GradeDummy gradeDummy : list){
			if(gradeDummy.getSubjectId().equals(lastSubjectId) == false || listWithSameSubject.size() == listWithSameSubject.indexOf(gradeDummy)+1) {
				
				if(listWithSameSubject.size() == listWithSameSubject.indexOf(gradeDummy)+1) listWithSameSubject.add(gradeDummy);
				
				if(listWithSameSubject.isEmpty() == false){
					//Deal with the general grade of a subject
					//get the score
					Score score = scoreDao.getBySubjectId(listWithSameSubject.get(0).getSubjectId());
					if(score == null){
						Subject subject = subjectDao.getById(listWithSameSubject.get(0).getSubjectId());
						if(subject != null){							
							score = new Score();
							score.setSubject(subject);
							score.setId(scoreDao.generateId());
						}
					}

					if(score != null){
						//get the data to sum and arrange it
						Double totalOld = 0.0;
						Double totalNew = 0.0;
						int amountOld = 0;
						int amountNew = 0;
						for(GradeDummy gradeDummy_temporary : listWithSameSubject){
							if(gradeDummy_temporary.getOldGrade() != null) {
								if(gradeDummy_temporary.getOldGrade() >= 0 && gradeDummy_temporary.getOldGrade() <= 5){									
									amountOld++;
									totalOld += gradeDummy_temporary.getOldGrade() ;
								}
							}
							if(gradeDummy_temporary.getNewGrade() != null) {
								if(gradeDummy_temporary.getNewGrade() >= 0 && gradeDummy_temporary.getNewGrade() <= 5){									
									amountNew ++;
									totalNew += gradeDummy_temporary.getNewGrade();
								}
							}
						}
						
						int amount = score.getTotalAmount();
						double grade = score.getTotalAverage()*amount;
						
						if(amount == 0 || (double) (grade/amount) < 0 || (double) (grade/amount) > 5) {
							amountOld = 0;
							totalOld = 0.0;
							grade = 0;
							amount = 0;
						}
						amount = amount - amountOld + amountNew;
						if(amount > 0) grade = (grade - totalOld + totalNew)/amount;
						
						score.setTotalAmount(amount);
						score.setTotalAverage(grade);
						
						//set the subject with this grade
						if(amount > 0) score.getSubject().setAverageGrade(grade);
						else score.getSubject().setAverageGrade(null);
						
						subjectDao.save(score.getSubject());
						
						//sort by group
						listWithSameSubject = GradeDummy.sortByProfessorId(listWithSameSubject);
						
						//repeat this loop and deal with specific groups
						List<GradeDummy> listWithSameProfessor = new ArrayList<GradeDummy>();
						//for(GradeDummy gradeDummy2 : listWithSameSubject){ OLD
						for(int position = 0; position <= listWithSameSubject.size(); position ++){
						
							GradeDummy gradeDummy2  = null;	
							if(position != listWithSameSubject.size()){
								gradeDummy2 = listWithSameSubject.get(position);
							}
						
							if(position == listWithSameSubject.size() || (gradeDummy2 != null && gradeDummy2.getSubjectId().equals(lastSubjectId) == false)) {
								if(listWithSameProfessor.isEmpty() == false || position == listWithSameSubject.size()){
									/******* <do the stuff for grades with the subject and the professor even if it is null> *********/
									
									Score score2 = scoreDao.getBySubjectAndProfesor(listWithSameProfessor.get(0).getSubjectId(), listWithSameProfessor.get(0).getProfessorId());
									if(score2 == null){
										Subject subject = subjectDao.getById(listWithSameProfessor.get(0).getSubjectId());
										if(subject != null){							
											score2 = new Score();
											score2.setSubject(subject);
											
											if(listWithSameProfessor.get(0).getProfessorId() != null){												
												TeacherDao teacherDao = new TeacherDao();
												Teacher teacher = teacherDao.getById(listWithSameProfessor.get(0).getProfessorId());
												
												if(teacher != null){
													score2.setTeacher(teacher);
												}
											}
											
											score2.setId(scoreDao.generateId());
										}
									}
									
									int amount2 = 0;
									Double grade2 = null;
									int oldAmount2 = 0;
									Double oldGrade2 = 0.0;
									//get the score with this professor
									//get the new amount and new grade
									for(GradeDummy gradeDummy_temporary : listWithSameProfessor){
										if(gradeDummy_temporary.getOldGrade() != null) {
											if(gradeDummy_temporary.getOldGrade() >= 0 && gradeDummy_temporary.getOldGrade() <= 5){									
												oldAmount2++;
												oldGrade2 += gradeDummy_temporary.getOldGrade() ;
											}
										}
										if(gradeDummy_temporary.getNewGrade() != null) {
											if(gradeDummy_temporary.getNewGrade() >= 0 && gradeDummy_temporary.getNewGrade() <= 5){									
												amount2 ++;
												if(grade2 == null) grade2 = 0.0;
												grade2 += gradeDummy_temporary.getNewGrade();
											}
										}
									}
									
									//Edit the grade in the grade entity
									score2.setTotalAverage((score2.getTotalAverage()*score2.getTotalAmount() + grade2 - oldGrade2)/(score2.getTotalAmount() + amount2 - oldAmount2));
									score2.setTotalAmount(score2.getTotalAmount() + amount2 - oldAmount2);
									scoreDao.save(score2);
									
									 /*
									  * Update the grade for all groups which have this professor and this subject if not null
									  */
									List<Group> groups = groupDao.getGroups(listWithSameProfessor.get(0).getSubjectId(), listWithSameProfessor.get(0).getProfessorId());
									if(groups != null && groups.isEmpty() == false){
										for(Group g : groups){
											g.setAverageGrade(score2.getTotalAverage());
											groupDao.save(g);
										}										
									}
									
									listWithSameProfessor = GradeDummy.sortBySemester(listWithSameProfessor);
									//repeat the loop but sorted by semester
									int amount3 = 0;
									int amountOld3 = 0;
									double grade3 = 0.0;
									double gradeOld3 = 0.0;
									for(int pos = 0; pos < listWithSameProfessor.size(); pos ++){
										if(pos == listWithSameProfessor.size() -1 || (pos > 0 && listWithSameProfessor.get(pos).getSemesterNumber() != listWithSameProfessor.get(pos-1).getSemesterNumber())){
											
											if(pos == listWithSameProfessor.size()-1){
												if(listWithSameProfessor.get(pos).getNewGrade() != null) {
													amount++;
													grade3 += listWithSameProfessor.get(pos).getNewGrade();
												}
												if(listWithSameProfessor.get(pos).getOldGrade() != null) {
													amountOld++;
													gradeOld3 += listWithSameProfessor.get(pos).getOldGrade();
												}
											}
											
											//Do something with the this semester
											SingleScore singleScore2 = score2.getSemester(listWithSameProfessor.get(pos).getSemesterNumber());
											if(singleScore2 != null) singleScore2.sumToAmountAndAverage(amount3 - amountOld3, grade3 - gradeOld3);
											
											amount3 = 0;
											amountOld3 = 0;
											grade3 = 0.0;
											gradeOld3 = 0.0;
											
										}
										if(listWithSameProfessor.get(pos).getNewGrade() != null) {
											amount++;
											grade3 += listWithSameProfessor.get(pos).getNewGrade();
										}
										if(listWithSameProfessor.get(pos).getOldGrade() != null) {
											amountOld++;
											gradeOld3 += listWithSameProfessor.get(pos).getOldGrade();
										}
									}
									scoreDao.save(score2);
									
									/******* </do the stuff for grades with the subject and the professor even if it is null> ********/
								}
								
								listWithSameProfessor = new ArrayList<GradeDummy>();
							}
							listWithSameProfessor.add(gradeDummy2);
						}
						
						scoreDao.save(score);
					}
					
				}
				listWithSameSubject = new ArrayList<GradeDummy>();
			}
			listWithSameSubject.add(gradeDummy);
			lastSubjectId = gradeDummy.getSubjectId();
		}
		
	}
	
}
