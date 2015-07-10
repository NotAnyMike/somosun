package com.uibinder.index.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.uibinder.index.shared.control.Plan;

public interface SavePlanAsDefaultHandler extends EventHandler{
	
	void onSavePlanAsDefault(Plan plan);

}
