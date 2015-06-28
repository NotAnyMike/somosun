package com.uibinder.index.server.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.server.SiaProxy;
import com.uibinder.index.server.dao.CareerDao;
import com.uibinder.index.server.dao.PlanDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Subject;

/**
 * 
 * @author Mike
 *
 * This class takes care of implementing the client's RPC services, 
 * Feel free to modify the methods implementation
 * 
 *  
 */
public class SUNServiceImpl extends RemoteServiceServlet implements SUNService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Subject getSubjectByCode(int code) {
		//Subject subject = new Subject(3,"1231231","Just bullshit");
		return null;
	}

	@Override
	public Subject getSubjectByCode(int code, String career) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubjectByName(String name, String career) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 
	 * It allows the admins to retrieve and store data in & out of the database infrastructure. 
	 * 
	 */
	@Override
	public List<RandomPhrase> getRandomPhrase() {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("RandomPhrase");
		List<RandomPhrase> lista = new LinkedList<RandomPhrase>();
		for(Entity n: datastore.prepare(q).asIterable()){
			String random = (String) n.getProperty("random");
			String author = (String) n.getProperty("author");
			lista.add(new RandomPhrase(random,author));
		}
		return lista;
	}

	@Override
	public void saveRandomPhrase(String phrase, String author) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		try{
			Entity daPhrase = new Entity("RandomPhrase");
			daPhrase.setProperty("random", phrase);
			daPhrase.setProperty("author", author);
			datastore.put(daPhrase);
			tx.commit();
		}finally{
			if(tx.isActive()){
				tx.rollback();
			}
		}
	}
	
	/**
	 * For more information goto SiaProxy.class
	 * @param subject
	 * @param sede
	 * @return
	 */
	@Override
	public SiaResultGroups getGroupsFromSia(Subject subject, String sede){
		return SiaProxy.getGroupsFromSubject(subject, sede);
	}

	/**
	 * For more information goto SiaProxy.class
	 * @param subjectSiaCode
	 * @param sede
	 * @return
	 */
	@Override
	public SiaResultGroups getGroupsFromSia(String subjectSiaCode, String sede) {
		return SiaProxy.getGroupsFromSubject(subjectSiaCode, sede);
	}

	/**
	 * For more information goto SiaProxy.class
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @return
	 */
	@Override
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede) {
		return SiaProxy.getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede);
	}

	@Override
	public List<Career> getCareers(String sede) {
		CareerDao careerDao = new CareerDao();
		List<Career> listFromDb = careerDao.getCareersBySede(sede);
		List<Career> listToReturn = new ArrayList<Career>();
		for(Career c : listFromDb){
			listToReturn.add(c);
		}
		return listToReturn;
	}

	@Override
	public Plan getPlanDefault(String careerCode) {
		PlanDao planDao = new PlanDao();
		return (Plan) planDao.createPlanFromDefaultString(careerCode);
	}

	@Override
	public String toTest() {
		return SiaProxy.getRequisitesForACareer("2557");
	}

	@Override
	public ComplementaryValues getComplementaryValues(String career, String code) {
		return (ComplementaryValues) SiaProxy.getRequisitesFromSia(code, career);
	}

	
	/**
	 * @param nameOrCode the name or the code as a String
	 * @para typology:
	 * 	For all = ""
	 * 	For under-graduate: Nivelación: "P" Fundamentación: "B" Disciplinar: "C" Libre elección: "L"
	 * 	For graduate: Obligatorio: "O" Elegible: "T"
	 * @param career: the String of the code of the career
	 * @param sede: "ama", "bog", "car", "man", "med", "ori", "pal" or  "tum" if nothing it will be taken as bog
	 */
	@Override
	public SiaResultSubjects getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page) {
		return getSubjectFromSia(nameOrCode, typology, career, "", page, 10, sede);
	}

}
