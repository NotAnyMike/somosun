package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.somosun.plan.server.control.GroupServer;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;
import com.somosun.plan.shared.control.controlAbstract.GroupAbstract;

public class GroupDao implements Dao<Group> {

	static{
		ObjectifyService.register(GroupServer.class);
		ObjectifyService.register(Career.class);
		ObjectifyService.register(Subject.class);
		ObjectifyService.register(Teacher.class);
		ObjectifyService.register(Block.class);
		ObjectifyService.register(SemesterValue.class);
	}
	
//	public Long save(Group g){
//		Long toReturn = null; 
//		if(g != null) {
//			if(g.getSubject() != null){
//				if(g.getSubject().getId() == null){
//					SubjectDao subjectDao = new SubjectDao();
//					g.getSubject().setId(subjectDao.save(g.getSubject()));
//				}
//				//g.setSubjectRef(Ref.create(g.getSubject()));
//			}else{
//				//g.setSubjectRef(null);
//			}
//			
//			if(g.getTeacher() != null){
//				if(g.getTeacher().getIdSun() != null){
//					TeacherDao teacherDao = new TeacherDao();
//					g.getTeacher().setIdSun(teacherDao.save(g.getTeacher()));
//				}
//				//g.setTeacherRef(Ref.create(g.getTeacher()));
//			}else{
//				//g.setTeacherRef(null);
//			}
//			
//			ofy().save().entity(g).now();
//			toReturn = g.getId();
//		}
//		return toReturn;
//	}
	
	public Long save(GroupAbstract g){
		Long id = null;
		
		/*
		 * 1. Check if the g has id
		 * 2. Check if it is the same as the one in the db (be carefull with the case where it is a new g, therefore originPlan = null [null pointer exception])
		 * 2.1 Compare the values and references if they are the same) if false save it
		 * 2.2 Take the g's references (not gOrigina !IMportant) from the db and re do this thing (i.e. compare values and reference, then check the references values and its references and so on)
		 */
		if(g != null){
			
			if(g.getId() == null) g.setId(generateId());

			/******** check for the references' values *************************************/
			//subject must not be modified (only from sia or directly with its dao)
			//teacher must not be modified (only from sia or directly with its dao)
			//semesterValue must not be modified (only from sia or directly with its dao)
			//careers must not be modified (only from sia or directly with its dao)
			//schedule must not be modified (it changes the entity instead of modifying it)
			/*******************************************************************************/
			
			/******* save the entity's g *******/
			GroupServer original = getServerById(g.getId());
			if(original == null || original.compare(g) == false){
				
				if(original == null) original = new GroupServer();
				
				original.setId(g.getId());
				original.setGroupNumber(g.getGroupNumber());
				original.setFreePlaces(g.getFreePlaces());
				original.setTotalPlaces(g.getTotalPlaces());
				original.setAverageGrade(g.getAverageGrade());
				original.setSubject(g.getSubject());
				original.setTeacher(g.getTeacher());
				original.setSemesterValue(g.getSemesterValue());
				original.setSchedule(g.getSchedule());
				original.setCareers(g.getCareers());
				
				//save original
				ofy().defer().save().entity(original);
				
			}
			id = original.getId();
			/**************************************/
			
		}
		
		return id;
	}
	
	private GroupServer getServerById(Long id){
		GroupServer group = null;
		if(id != null){
			Key<GroupServer> key = Key.create(GroupServer.class, id);
			group = ofy().load().key(key).now();
		}
		return group;
	}
	
	public Group getById(Long id){
		GroupServer group = getServerById(id);
		Group groupToReturn = null;
		if(group != null) groupToReturn = group.getClientInstance();
		
		return groupToReturn;
	}

	public List<Group> getGroups(Subject subject) {
		List<GroupServer> list = null;
		if(subject != null && subject.getId() != null){
			SubjectDao subjectDao = new SubjectDao();
			Ref<Subject> ref = null;
			Subject s = subjectDao.getByCode(subject.getCode());
			if(s != null) ref = Ref.create(s);
			list = ofy().load().type(GroupServer.class).filter("subject", ref).list();
		}
		List<Group> toReturn = null;
		if(list != null){
			for(GroupServer gS : list){
				if(toReturn == null) toReturn = new ArrayList<Group>();
				toReturn.add(gS.getClientInstance());
			}
		}
		return toReturn;
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
		GroupServer group = null;
		Group groupToReturn = null;
		if(subject != null){			
			Query<GroupServer> query = ofy().load().type(GroupServer.class).filter("groupNumber", groupNumber);
			if(subject.getId() != null){
				SubjectDao subjectDao = new SubjectDao();
				Ref<Subject> subjectRef = null;
				Subject s = subjectDao.getByCode(subject.getCode());
				if(s != null) subjectRef = Ref.create(s);
				
				query = query.filter("subject", subjectRef);
			}
			
			if(semesterValue != null){	
				SemesterValueDao semesterValueDao = new SemesterValueDao();
				Ref<SemesterValue> sVRef = null;
				SemesterValue sV = semesterValueDao.get(semesterValue.getYear(), semesterValue.getNumberSemester());
				if(sV != null) sVRef = Ref.create(sV);
				
				//The following query was just for year i.e. ("semesterValue.year", x)
				query = query.filter("semesterValue", sVRef);
			}else{
				query = query.filter("semesterValue", "null");
			}
			
			group = query.first().now();
		}
		if(group != null) groupToReturn = group.getClientInstance();
		
		return groupToReturn;
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
				Score score = scoreDao.getBySubjectAndProfesor(group.getSubject().getId(), group.getTeacher().getIdSun());
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
			Key<GroupServer> key = Key.create(GroupServer.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<GroupServer> key = f.allocateId(GroupServer.class);
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
			Score score = scoreDao.getBySubjectAndProfesor(group.getSubject().getId(), group.getTeacher().getIdSun());
			if(score != null){
				group.setAverageGrade(score.getTotalAverage());
			}
		}

		return group;
	}

	public void deleteAll() {
		List<GroupServer> list = ofy().load().type(GroupServer.class).list();
		for(GroupServer g : list){
			delete(g.getClientInstance());
		}
	}

	public List<Group> getGroups(Long subjectId, Long professorId) {
		List<GroupServer> list = null;
		
		if(subjectId != null && professorId != null){			
			SubjectDao subjectDao = new SubjectDao();
			Subject s = subjectDao.getById(subjectId);
			if(s != null){				
				Ref<Subject> subjectRef = null;
				subjectRef = Ref.create(s);
				
				Query q = ofy().load().type(GroupServer.class).filter("subject", subjectRef);
				
				TeacherDao tDao = new TeacherDao();
				Ref<Teacher> tRef = null;
				Teacher t = tDao.getById(professorId);
				if(t != null) tRef = Ref.create(t);
				List<GroupServer> notToReturn = q.filter("teacher", tRef).list();
				
				if(notToReturn != null && notToReturn.isEmpty() == false){
					for(GroupServer g : notToReturn){
						if(list == null) list = new ArrayList<GroupServer>();
						list.add(g);
					}
				}
				
			}
		}
		
		List<Group> toReturn = null;
		if(list != null){
			for(GroupServer gS : list){
				if(toReturn == null) toReturn = new ArrayList<Group>();
				toReturn.add(gS.getClientInstance());
			}
		}
		
		return toReturn;
	}

	protected Ref<GroupServer> getRef(Long id) {
		GroupServer g = getServerById(id);
		Ref<GroupServer> toReturn = null;
		if(g != null) toReturn = Ref.create(g);
		return toReturn;
	}
	
}
