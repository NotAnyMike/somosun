package com.uibinder.index.server.serviceImpl;

import java.util.LinkedList;
import java.util.List;

import com.uibinder.index.shared.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.client.widget.SubjectWidget;
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

	@Override
	public Subject getSubjectByCode(int code) {
		Subject subject = new Subject(3,"1231231","Just bullshit");
		return subject;
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
		if(lista.isEmpty()){
			Entity daPhrase = new Entity("RandomPhrase");
			daPhrase.setProperty("random", "El que hace puede equivocarse, el que no, ya está equivocado.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "El que hace puede equivocarse, el que no, ya está equivocado.Nadie frena más a uno que uno mismo.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "Los de GEDAD son unos vagos que no hacen nada.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "El hombre, en su orgullo, creó a Dios a su imagen y semejanza.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "El dinero no es nada, pero mucho dinero ya es otra cosa.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "Nunca dejes que la universidad evite que aprendas.");
			daPhrase.setProperty("author", "Anonimo");
			datastore.put(daPhrase);
			daPhrase.setProperty("random", "Si el compañero tiene un mensaje, que lo de pero que no lo de");
			daPhrase.setProperty("author", "Mamerta de la loma");
			datastore.put(daPhrase);	
		}
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

}
