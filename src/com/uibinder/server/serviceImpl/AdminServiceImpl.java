package com.uibinder.server.serviceImpl;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.client.admin.service.AdminService;
import com.uibinder.server.SiaProxy;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("AdminServiceImpl");
	
	@Override
	public void resetCareer() {
		SiaProxy.updateCareersFromSia("bog");
	}

}
