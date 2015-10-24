package com.somosun.plan.server.serviceImpl;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.somosun.plan.client.index.service.LoginService;
import com.somosun.plan.server.dao.StudentDao;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.control.Student;

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
			student = StudentDao.getByUser(user);
			loginInfo.setStudent(student);
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			loginInfo.setStudent(null);
		}
		return loginInfo;
	}

}
