package com.uibinder.index.server.dao;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.UserSun;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class UserSunDao {
	
	static {
		ObjectifyService.register(UserSun.class);
	}
	
	/**
	 * This class is private because the only one to create one is if someone logged in is not registered in our db
	 * 
	 * @param user
	 */
	private void addUser(User user){
		if(getUserByIdG(user.getUserId())==null){ //looking if there is someone with its id from the google account service
			UserSun userToSave = new UserSun(user.getUserId(), user.getNickname(), user.getNickname(), user.getEmail());
			ofy().save().entity(userToSave).now();			
		}
	}
	
	public UserSun getUserByUser(User user){
		UserSun userSun = getUserByIdG(user.getUserId());
		if(userSun == null){
			addUser(user);
			userSun = new UserSun(user.getUserId(), user.getNickname(), user.getNickname(), user.getEmail());
		}
		return userSun;
	}
	
	/**
	 * Returns the first entity found that matches the id given by google accounts
	 * , null otherwise. THis method will not add a user automatically.
	 * 
	 * @param id
	 */
	public UserSun getUserByIdG(String id){
		UserSun userToReturn = (UserSun) ofy().load().type(UserSun.class).filter("idG", id).first().now();
		return userToReturn;
	}
	
	/**
	 * Returns the user based on the @Id annotation
	 * 
	 * @param id
	 * @return
	 */
	public UserSun getUserByIdSun(String id){
		Key<UserSun> key = Key.create(UserSun.class, id);
		return (UserSun) ofy().load().key(key).now();
	}

}
