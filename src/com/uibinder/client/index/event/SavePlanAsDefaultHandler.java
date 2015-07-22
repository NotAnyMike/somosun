package com.uibinder.client.index.event;

import com.google.gwt.event.shared.EventHandler;
import com.uibinder.shared.control.Plan;

public interface SavePlanAsDefaultHandler extends EventHandler{
	
	void onSavePlanAsDefault(Plan plan);

}
