package com.somosun.plan.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Teacher;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class TeacherDao implements Dao<Teacher> {

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
	public Teacher getByTeacher(Teacher t, boolean isSiaProxy){
		Teacher teacherToReturn = getTeacherByUsername(t.getUsername());
		if(teacherToReturn == null && isSiaProxy == true){
			teacherToReturn = t;
			save(teacherToReturn);
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
	
	public Teacher getById(Long id){
		Teacher toReturn = null;
		if(id != null){
			Key<Teacher> key = Key.create(Teacher.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}
	
	private void updateTeacher(Teacher t) {
		delete(t);
		save(t);
	}

	private boolean delete(Teacher t) {
		boolean toReturn = false;
		if(t!=null){
			toReturn = delete(t.getIdSun());
		}
		return toReturn;
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id!=null){
			Key<Teacher> key = Key.create(Teacher.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public Long save(Teacher t){
		Long toReturn = null;
		if(t != null)
		{
			t.setName(SomosUNUtils.removeAccents(t.getName()));
			//OLD t.setName(SomosUNUtils.standardizeString(t.getName(), false));
			ofy().save().entity(t).now();
			toReturn = t.getIdSun();
		}
		return toReturn;
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Teacher> key = f.allocateId(Teacher.class);
		return key.getId();
	}
}
