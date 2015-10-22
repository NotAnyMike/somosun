package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.EventHandler;
import com.somosun.plan.shared.control.Plan;

public interface SavePlanAsDefaultHandler extends EventHandler{
	
	void onSavePlanAsDefault(Plan plan);

}
