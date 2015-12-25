package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.incomplete.MessageIncomplete;
import com.somosun.plan.shared.values.MessageTypeCodes;

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
