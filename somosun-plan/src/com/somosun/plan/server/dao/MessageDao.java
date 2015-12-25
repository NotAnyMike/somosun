package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.MessageServer;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.values.MessageTypeCodes;



public class MessageDao implements Dao<MessageServer> {

	static{
		ObjectifyService.register(MessageServer.class);
	}
	
	/**
	 * This method will save/update the @param message, generate the id if @param message has no id and return it
	 * @param message
	 * @return
	 */
	public Long save(MessageServer message){
		
		Long toReturn = null;
		if(message != null){
			if(message.getId() == null){
				message.setId(generateId());
				toReturn = message.getId();
			}
			ofy().save().entity(message).now();
		}
		
		return toReturn;
	}
	
	public Long generateId(){
		ObjectifyFactory f = new ObjectifyFactory();
		Key<MessageServer> key = f.allocateId(MessageServer.class);
		return key.getId();
	}

	public List<MessageServer> getAllMessages() {
		return ofy().load().type(MessageServer.class).list();
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id!=null){			
			Key<MessageServer> key = Key.create(MessageServer.class, id);
			ofy().delete().key(key);
			toReturn = true;
		}
		return toReturn;
	}

	public List<MessageServer> getAllErrorMessages() {
		return ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.ERROR).list();
	}

	public List<MessageServer> getAllSuggestionMessages() {
		return ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.SUGGESTION).list();
	}
	
	public List<MessageServer> getAllOtherMessages() {
		return ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.OTHER).list();
	}

	public MessageServer getById(Long id) {
		Key<MessageServer> key = Key.create(MessageServer.class, id);
		return ofy().load().key(key).now();
	}

	/**
	 * if @param username is empty or null then wil search for a null student
	 * @param username
	 * @return
	 */
	public List<MessageServer> getUserMessages(String username) {
		List<MessageServer> toReturn = null;
		if(username == null || username.isEmpty() == true){
			toReturn =  ofy().load().type(MessageServer.class).filter("student", null).list();
		}else{			
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.getStudentByUserName(username);
			if(student != null){		
				Ref<Student> ref = Ref.create(student);
				toReturn =  ofy().load().type(MessageServer.class).filter("student", ref).list();
			}
		}
		return toReturn;
	}
	
}
