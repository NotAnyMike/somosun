package com.somosun.plan.server.dao;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.somosun.plan.server.control.ScoreServer;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GroupDao implements Dao<Group> {

	static{
		ObjectifyService.register(Group.class);
	}
	
	public Long save(Group g){
		Long toReturn = null; 
		if(g != null) {
			if(g.getSubject() != null){
				if(g.getSubject().getId() == null){
					SubjectDao subjectDao = new SubjectDao();
					g.getSubject().setId(subjectDao.save(g.getSubject()));
				}
				//g.setSubjectRef(Ref.create(g.getSubject()));
			}else{
				//g.setSubjectRef(null);
			}
			
			if(g.getTeacher() != null){
				if(g.getTeacher().getIdSun() != null){
					TeacherDao teacherDao = new TeacherDao();
					g.getTeacher().setIdSun(teacherDao.save(g.getTeacher()));
				}
				//g.setTeacherRef(Ref.create(g.getTeacher()));
			}else{
				//g.setTeacherRef(null);
			}
			
			ofy().save().entity(g).now();
			toReturn = g.getId();
		}
		return toReturn;
	}
	
	public Group getById(Long id){
		Group toReturn = null;
		if(id != null){
			Key<Group> key = Key.create(Group.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}

	public List<Group> getGroups(Subject subject) {
		if(subject != null && subject.getId() != null){
			return ofy().load().type(Group.class).filter("subject.code", subject.getCode()).list();
		}
		else return null;
	}
	
	public Group get(Group g){
		return get(g.getSubject(), g.getSemesterValue(), g.getGroupNumber());
	}

	/**
	 * The subject must have an id not null
	 * @param subject
	 * @param semesterValue
	 * @param groupNumber
	 * @return
	 */
	public Group get(Subject subject, SemesterValue semesterValue, Integer groupNumber){
		Group toReturn = null;
		if(subject != null){			
			Query<Group> query = ofy().load().type(Group.class).filter("groupNumber", groupNumber);
			if(subject.getId() != null){
				query = query.filter("subject.code", subject.getCode());
			}
			
			if(semesterValue != null){				
				query = query.filter("semesterValue.year", semesterValue.getYear());
			}else{
				query = query.filter("semesterValue", "null");
			}
			
			toReturn = query.first().now();
		}
		return toReturn;
	}
	
	/**
	 * 
	 * 
	 * @param group
	 * @param b
	 * @return
	 */
	public Group getByGroup(Group group, boolean isSiaProxy) {
		Group groupToReturn = get(group);
		if(groupToReturn == null){
			groupToReturn = group;
			
			if(group.getAverageGrade() == null && group.getTeacher() != null){
				ScoreDao scoreDao = new ScoreDao();
				ScoreServer score = scoreDao.getBySubjectAndProfesor(group.getSubject().getId(), group.getTeacher().getIdSun());
				if(score != null && score.getTotalAverage() != null){
					groupToReturn.setAverageGrade(score.getTotalAverage());
				}
			}
			
			save(groupToReturn);
		}else{
			if(groupToReturn.equals(group) == false && isSiaProxy == true){
				groupToReturn.setFreePlaces(group.getFreePlaces());
				groupToReturn.setTotalPlaces(group.getTotalPlaces());
				groupToReturn.setSchedule(group.getSchedule());
				groupToReturn.setTeacher(group.getTeacher());
				updateGroup(groupToReturn);
			}
		}
		return groupToReturn;
	}

	private void updateGroup(Group group) {
		delete(group);
		save(group);
	}

	private boolean delete(Group group) {
		boolean toReturn = false;
		if(group != null) toReturn = delete(group.getId());
		return toReturn;
	}
	
	public boolean delete(Long id){
		boolean toReturn = false;
		if(id!=null){
			Key<Group> key = Key.create(Group.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Group> key = f.allocateId(Group.class);
		return key.getId();
	}

	public Group getOrCreateGroup(Subject subject, SemesterValue semesterValue, Integer groupInt) {
		Group group = null;
		
		group = get(subject, semesterValue, groupInt);
		
		if(group == null){
			group = new Group(subject, semesterValue, groupInt);
			group.setId(generateId());
			
			save(group);
		}
		
		//Adding the average grade to the group #80
		if(group.getAverageGrade() == null && group.getTeacher() != null){
			ScoreDao scoreDao = new ScoreDao();
			ScoreServer score = scoreDao.getBySubjectAndProfesor(group.getSubject().getId(), group.getTeacher().getIdSun());
			if(score != null){
				group.setAverageGrade(score.getTotalAverage());
			}
		}

		
		
		return group;
	}

	public void deleteAll() {
		List<Group> list = ofy().load().type(Group.class).list();
		for(Group g : list){
			delete(g);
		}
	}

	public List<Group> getGroups(Long subjectId, Long professorId) {
		List<Group> toReturn = null;
		
		if(subjectId != null && professorId != null){			
			SubjectDao subjectDao = new SubjectDao();
			Subject s = subjectDao.getById(subjectId);
			if(s != null){				
				Query q = ofy().load().type(Group.class).filter("subject.code", s.getCode());
				
				TeacherDao teacherDao = new TeacherDao();
				Teacher teacher = teacherDao.getById(professorId);
				
				if(teacher != null){
					List<Group> notToReturn = q.filter("teacher.username", teacher.getUsername()).list();
					
					if(notToReturn != null && notToReturn.isEmpty() == false){
						for(Group g : notToReturn){
							if(toReturn == null) toReturn = new ArrayList<Group>();
							toReturn.add(g);
						}
					}
				}
				
			}
		}
		
		
		return toReturn;
	}
	
}
