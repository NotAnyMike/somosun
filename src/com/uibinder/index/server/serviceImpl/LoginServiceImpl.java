package com.uibinder.index.server.serviceImpl;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.LoginService;
import com.uibinder.index.server.dao.UserSunDao;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.control.UserSun;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginInfo loginInfo = new LoginInfo();
	private UserService userService = UserServiceFactory.getUserService();
	private User user;
	private UserSun userSun;

	@Override
	public LoginInfo login(String requestUri) {
		user = userService.getCurrentUser();
		UserSunDao UserSunDao = new UserSunDao();
		
		if(userService.isUserLoggedIn()){
			loginInfo.setLoggedIn(true);
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			userSun = UserSunDao.getUserByUser(user);
			loginInfo.setUser(userSun);
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			loginInfo.setUser(null);
		}
		return loginInfo;
	}

	@Override
	public String getSubject() {
		return "hola";
	}

}
