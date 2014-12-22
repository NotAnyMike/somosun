package com.uibinder.index.server.serviceImpl;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.LoginService;
import com.uibinder.index.shared.LoginInfo;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	
	private UserService userService = UserServiceFactory.getUserService();
	private User user = userService.getCurrentUser();
	private LoginInfo loginInfo = new LoginInfo();

	@Override
	public LoginInfo login(String requestUri) {
		if(userService.isUserLoggedIn() == true){
			loginInfo.setLoggedIn(true);
			loginInfo.setName(user.getNickname());
			loginInfo.setId(user.getUserId());
			loginInfo.setEmail(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
			loginInfo.setName("invitado");
		}
		return loginInfo;
	}

}
