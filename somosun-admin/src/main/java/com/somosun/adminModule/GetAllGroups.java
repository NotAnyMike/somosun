package com.somosun.adminModule;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somosun.plan.server.expensiveOperation.AnalyseAllCareersExpensiveOperation;
import com.somosun.plan.server.expensiveOperation.GetAllGroupsExpensiveOperation;

public class GetAllGroups extends HttpServlet{
	
	final static private Logger log = Logger.getLogger("admin-module");
	  
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		
		log.info("<------------------ Admin Module - started ------------------->");
		
		GetAllGroupsExpensiveOperation.run();
		
	}
}
