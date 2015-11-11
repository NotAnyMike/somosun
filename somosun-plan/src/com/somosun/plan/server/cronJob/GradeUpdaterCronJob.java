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
import com.somosun.plan.server.dummy.GradeDummy;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.Subject;

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
				
				Double oldGrade = (oldGradeString.isEmpty() ? null : Double.valueOf(oldGradeString));
				Double newGrade = (newGradeString.isEmpty() ? null : Double.valueOf(newGradeString));
				Long groupId = Long.valueOf(task.extractParams().get(2).getValue());
				Long subjectId = Long.valueOf(task.extractParams().get(3).getValue());
				
				list.add(new GradeDummy(oldGrade, newGrade, groupId, subjectId));
				
				log.info("old-grade:" + oldGrade + " and new-grade: " + newGrade + " and group-id: " + groupId + " and subject-id: " + subjectId);
			} catch (UnsupportedEncodingException | UnsupportedOperationException e) {
				e.printStackTrace();
				log.warning("Error while reading the parameters of the task from the pull queue: " + e.toString());
			}
			q.deleteTask(task.getName());
		}
		
		//sorting the list
		list = GradeDummy.sortBySubjectId(list);
		
		//dividing the list and sorting by groupId
		List<GradeDummy> listWithSameSubject = new ArrayList<GradeDummy>();;
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
						
						if(amount == 0 || grade < 0 || grade > 5) {
							amountOld = 0;
							totalOld = 0.0;
							grade = 0;
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
						listWithSameSubject = GradeDummy.sortByGroupId(listWithSameSubject);
						
						//repeat this loop and deal with specific groups
						//TODO
						
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
