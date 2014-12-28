package com.uibinder.index.server.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.LoginService;
import com.uibinder.index.server.dao.StudentDao;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.UserSun;

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
		StudentDao UserSunDao = new StudentDao();
		
		if(userService.isUserLoggedIn()){
			loginInfo.setLoggedIn(true);
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			student = UserSunDao.getStudentByUser(user);
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
		String s = "hO";
		
		try {
            URL url = new URL("http://unsia.unal.edu.co/buscador/JSON-RPC");
            HttpURLConnection request = ( HttpURLConnection ) url.openConnection();
            
            request.setDoOutput(true);
            request.setDoInput(true);
            
            OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
            String data = "{method:buscador.obtenerAsignaturas,params:['matematic','PRE','','PRE','','',1,1]}";
            post.write(data);
            post.flush();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
               s="data = " + data + " line = " + line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            s="erro";
        } catch (IOException e) {
            s="error";
        }
		
        return s;
	}

}
