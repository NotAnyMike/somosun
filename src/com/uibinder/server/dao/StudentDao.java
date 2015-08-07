package com.uibinder.server.dao;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.shared.control.Student;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class StudentDao {
	
	static {
		ObjectifyService.register(Student.class);
	}
	
	public StudentDao(){
	}
	
	/**
	 * This class is private because the only one to create one is if someone logged in is not registered in our db
	 * 
	 * @param user
	 */
	public void saveStudent(Student student){
		if(student != null){
			ofy().save().entity(student).now();			
		}
	}
	
	public Student getStudentByUser(User user){
		
		Student student = null;
		
		if(user != null){	
			if(user.getUserId().equals("0") == false){				
				student = getStudentByIdG(user.getUserId());
				if(student == null){				
					student = new Student(user.getUserId(), user.getNickname(), user.getNickname(), user.getEmail());
					student.setBlocked(true);
					student.setIdSun(generateId());
					saveStudent(student);
				}
			}
		}
		return student;
	}
	
	private Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Student> key = f.allocateId(Student.class);
		return key.getId();
	}

	/**
	 * Returns the first entity found that matches the id given by google accounts
	 * , null otherwise. THis method will not add a user automatically.
	 * 
	 * @param id
	 */
	public Student getStudentByIdG(String id){
		Student studentToReturn = (Student) ofy().load().type(Student.class).filter("idG", id).first().now();
		return studentToReturn;
	}
	
	/**
	 * Returns the user based on the @Id annotation
	 * 
	 * @param id
	 * @return
	 */
	public Student getStudentByIdSun(Long idSun) {
		Key<Student> k = Key.create(Student.class, idSun);
		Student s = null;
		s = ofy().load().key(k).now();
		return s;
	}

	public Student getStudentByUserName(String userName) {
		return (Student) ofy().load().type(Student.class).filter("username", userName).first().now();
	}

}
