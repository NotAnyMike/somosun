package com.somosun.gradeUpdater;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GradeUpdaterServlet extends HttpServlet{

	private static Logger log = Logger.getLogger("GradeUpdaterCronJob [grade-updater]");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("hello there");
		
		//call some cron job 
		//GradeUpdaterCronJob.updateAllGrades();
	}

}
