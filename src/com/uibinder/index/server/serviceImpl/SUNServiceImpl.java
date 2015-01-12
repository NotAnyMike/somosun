package com.uibinder.index.server.serviceImpl;

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
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
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
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology,
			String career, String scheduleCP, int page, int ammount, String sede) {
		return SiaProxy.getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede);
	}

}
