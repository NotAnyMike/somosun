package com.somosun.adminModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.server.expensiveOperation.AnalyseAllCareersExpensiveOperation;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.control.Career;

public class AnalyseAllCareers extends HttpServlet {
	
	final static private Logger log = Logger.getLogger("admin-module");
	  
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		
		log.info("<------------------ Admin Module - started ------------------->");

		String allCareers = req.getParameter("allCareers");
		boolean includeAnalyzed = false;
		if(allCareers.equals("true")){
			includeAnalyzed = true;
		}
		
		AnalyseAllCareersExpensiveOperation.run(includeAnalyzed);
		
	}
}
