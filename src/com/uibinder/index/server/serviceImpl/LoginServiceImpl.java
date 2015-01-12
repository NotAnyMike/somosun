package com.uibinder.index.server.serviceImpl;

import java.io.IOException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.LoginService;
import com.uibinder.index.server.SiaProxy;
import com.uibinder.index.server.dao.StudentDao;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.control.Student;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginInfo loginInfo = new LoginInfo();
	private UserService userService = UserServiceFactory.getUserService();
	private User user;
	private Student student;

	@Override
	public LoginInfo login(String requestUri) {
		user = userService.getCurrentUser();
		StudentDao StudentDao = new StudentDao();
		
		if(userService.isUserLoggedIn()){
			loginInfo.setLoggedIn(true);
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			student = StudentDao.getStudentByUser(user);
			loginInfo.setStudent(student);
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			loginInfo.setStudent(null);
		}
		return loginInfo;
	}

	@Override
	public String getSubject() {
		SiaProxy.getSubjects("plantas", "", "", "", 1, 10, "bog");
		return SiaProxy.getGroupsFromSubject("2015877", "bog");
	}

}
