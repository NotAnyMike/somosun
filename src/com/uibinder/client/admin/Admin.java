package com.uibinder.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.uibinder.client.admin.service.AdminService;
import com.uibinder.client.admin.service.AdminServiceAsync;
import com.uibinder.client.index.service.SUNService;
import com.uibinder.client.index.service.SUNServiceAsync;

public class Admin implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		SUNServiceAsync rpcService = GWT.create(SUNService.class);
		AdminServiceAsync rpcAdminService = GWT.create(AdminService.class);
		HandlerManager eventBus = new HandlerManager(null);
		AdminAppController appController = new AdminAppController(rpcService, rpcAdminService, eventBus);
		appController.go(RootPanel.get());
		
	}

}
