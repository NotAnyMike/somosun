package com.somosun.plan.server.cronJob;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class updateGradesCronJob extends HttpServlet {

	private static final Logger log = Logger.getLogger("SunServiceImpl");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		log.info("this the doGet of a http servelt");
		out.println("this the doGet of a http servelt");
	}

}
