package com.uibinder.shared.control;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.uibinder.shared.values.MessageTypeCodes;

@Entity
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null; 
	private String name;
	private String topic;
	@Index private String type;
	private String message;
	@Index private Student student;
	@Index private Date date;
	
	public Message(){
		setName(null);
		setTopic(null);
		setType(MessageTypeCodes.SUGGESTION);
		setMessage(null);
		setStudent(null);
		setDate(new Date());
	}

	public Message(String name, String topic, String type, String message, Student student) {
		setName(name);
		this.setTopic(topic);
		this.setType(type);
		this.setMessage(message);
		this.setStudent(student);
		setDate(new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
		
}
