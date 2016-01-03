package com.somosun.plan.server.control;

import java.io.Serializable;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.controlAbstract.MessageAbstract;

@Entity
public class MessageServer extends MessageAbstract implements Serializable {

	@Index @Load private Ref<Student> student = null;
	
	public MessageServer(){
		super();
		setStudent(null);
	}
	
	public MessageServer(Message m){
		super(m.getName(),m.getTopic(), m.getType(), m.getMessage());
		this.setId(m.getId());
		if(m.getStudent() == null) this.setStudent(null); 
		else this.setStudent(Ref.create(m.getStudent()));
	}

	public MessageServer(String name, String topic, String type, String message, Student student) {
		super(name, topic, type, message);
		this.setStudent(Ref.create(student));
	}
	
	
	public Ref<Student> getStudent() {
		return student;
	}

	public void setStudent(Ref<Student> student) {
		this.student = student;
	}
	
	public Message getClientInstance(){
		Message m = new Message();
		
		m.setId(getId());
		m.setDate(getDate());
		m.setMessage(getMessage());
		m.setName(getName());
		m.setStudent(getStudent().get());
		m.setTopic(getTopic());
		m.setType(getType());
		
		return m;
	}
}
