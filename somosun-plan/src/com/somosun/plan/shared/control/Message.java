package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.somosun.plan.shared.control.controlAbstract.MessageIncomplete;

public class Message extends MessageIncomplete implements Serializable {

	private Student student;
	
	public Message(){
		super();
		setStudent(null);
	}

	public Message(String name, String topic, String type, String message, Student student) {
		super(name, topic, type, message);
		this.setStudent(student);
	}
	
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
}
