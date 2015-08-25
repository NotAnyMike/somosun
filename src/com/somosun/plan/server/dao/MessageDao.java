package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.values.MessageTypeCodes;



public class MessageDao {

	static{
		ObjectifyService.register(Message.class);
	}
	
	/**
	 * This method will save/update the @param message, generate the id if @param message has no id and return it
	 * @param message
	 * @return
	 */
	public Long saveMessage(Message message){
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
		Key<Message> key = f.allocateId(Message.class);
		return key.getId();
	}

	public List<Message> getAllMessages() {
		return ofy().load().type(Message.class).list();
	}
	
	public void deleteMessage(Long id){
		if(id!=null){			
			Key<Message> key = Key.create(Message.class, id);
			ofy().delete().key(key);
		}
	}

	public List<Message> getAllErrorMessages() {
		return ofy().load().type(Message.class).filter("type", MessageTypeCodes.ERROR).list();
	}

	public List<Message> getAllSuggestionMessages() {
		return ofy().load().type(Message.class).filter("type", MessageTypeCodes.SUGGESTION).list();
	}
	
	public List<Message> getAllOtherMessages() {
		return ofy().load().type(Message.class).filter("type", MessageTypeCodes.OTHER).list();
	}

	public Message getMessageById(Long id) {
		Key<Message> key = Key.create(Message.class, id);
		return ofy().load().key(key).now();
	}

	/**
	 * if @param username is empty or null then wil search for a null student
	 * @param username
	 * @return
	 */
	public List<Message> getUserMessages(String username) {
		List<Message> toReturn = null;
		if(username == null || username.isEmpty() == true){
			toReturn =  ofy().load().type(Message.class).filter("student", null).list();
		}else{			
			toReturn =  ofy().load().type(Message.class).filter("student.username", username).list();
		}
		return toReturn;
	}
	
}
