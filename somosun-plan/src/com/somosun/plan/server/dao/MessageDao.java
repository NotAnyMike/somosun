package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.somosun.plan.server.control.MessageServer;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.controlAbstract.MessageAbstract;
import com.somosun.plan.shared.values.MessageTypeCodes;



public class MessageDao implements Dao<Message> {

	static{
		ObjectifyService.register(MessageServer.class);
	}
	
	/**
	 * This method will save/update the @param message, generate the id if @param message has no id and return it
	 * @param message
	 * @return
	 */
	public Long save(MessageAbstract message){
		
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

	public List<Message> getAllMessages() {
		List<Message> toReturn = toClientList(ofy().load().type(MessageServer.class).list());
		return toReturn;
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

	public List<Message> getAllErrorMessages() {
		List<MessageServer> list = ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.ERROR).list(); 
		List<Message> toReturn = toClientList(list);
		return toReturn;
	}
	
	private List<Message> toClientList(List<MessageServer> list){
		List<Message> toReturn = null;
		if(list != null){
			for(MessageServer mS : list){
				if(toReturn == null) toReturn = new ArrayList<Message>();
				toReturn.add(mS.getClientInstance());
			}
		}
		
		return toReturn;
	}

	public List<Message> getAllSuggestionMessages() {
		return toClientList(ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.SUGGESTION).list());
	}
	
	public List<Message> getAllOtherMessages() {
		return toClientList(ofy().load().type(MessageServer.class).filter("type", MessageTypeCodes.OTHER).list());
	}

	public Message getById(Long id) {
		Key<MessageServer> key = Key.create(MessageServer.class, id);
		MessageServer mS = ofy().load().key(key).now();
		Message m = null;
		if(mS != null) m = mS.getClientInstance();
		return m;
	}

	/**
	 * if @param username is empty or null then wil search for a null student
	 * @param username
	 * @return
	 */
	public List<Message> getUserMessages(String username) {
		List<MessageServer> list = null;
		List<Message> toReturn = null;
		if(username == null || username.isEmpty() == true){
			list =  ofy().load().type(MessageServer.class).filter("student", null).list();
		}else{			
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.getStudentByUserName(username);
			if(student != null){		
				Ref<Student> ref = Ref.create(student);
				list =  ofy().load().type(MessageServer.class).filter("student", ref).list();
			}
		}
		
		toReturn = toClientList(list);
		
		return toReturn;
	}
	
}
