package com.somosun.plan.server.cronJob;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;

public class GradeUpdaterCronJob {
	
	private static Logger log = Logger.getLogger("GradeUpdaterCronJob");

	public static void updateAllGrades(){
		//THIS SHOULD LEASE TASKS AND GET RID OF THEM
				Queue q = QueueFactory.getQueue("updateSubjectGradePullQueue");
				List<TaskHandle> tasks = q.leaseTasks(600, TimeUnit.SECONDS, 1000);
				for(TaskHandle task : tasks){
					try {
						log.info("old-grade:" + task.extractParams().get(0).getValue() + "and new-grade: " + task.extractParams().get(1).getValue() + " and group-id: " + task.extractParams().get(2).getValue());
					} catch (UnsupportedEncodingException | UnsupportedOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					q.deleteTask(task.getName());
				}
	}
	
}
