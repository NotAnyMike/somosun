package com.uibinder.index.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Teacher;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class TeacherDao extends Dao {

	static{
		ObjectifyService.register(Teacher.class);
	}
	
	private Teacher getTeacherByUsername(String username){
		return (Teacher) ofy().load().type(Teacher.class).filter("userId", username).first().now();
	}
	
	/**
	 * If the teacher does not exists it creates one
	 * 
	 * @param t
	 * @param isSiaProxy: will give rights to update (delete and create) and create a new teacher from the bd, it will be true if and only if the request is coming from the SiaProxy class
	 * @return
	 */
	public Teacher getTeacherByTeacher(Teacher t, boolean isSiaProxy){
		Teacher teacherToReturn = getTeacherByUsername(t.getUsername());
		if(teacherToReturn == null && isSiaProxy == true){
			teacherToReturn = t;
			saveTeacher(teacherToReturn);
		} else {
			if(teacherToReturn.equals(t) == false && isSiaProxy == true){
				teacherToReturn.setEmail(t.getEmail());
				teacherToReturn.setName(t.getName());
				teacherToReturn.setUsername(t.getUsername());
				teacherToReturn.setSede(t.getSede());
				updateTeacher(teacherToReturn);
			}
		}
		return teacherToReturn;
	}
	
	private void updateTeacher(Teacher t) {
		deleteTeacher(t);
		saveTeacher(t);
	}

	private void deleteTeacher(Teacher t) {
		if(t.getIdSun()!=null){
			Key<Teacher> key = Key.create(Teacher.class, t.getIdSun());
			ofy().delete().key(key).now();
		}
	}

	private void saveTeacher(Teacher t){
		if(t != null)
		{
			t.setName(standardizeString(t.getName()));
			ofy().save().entity(t).now();
		}
	}
}
