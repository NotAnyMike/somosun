package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.EventHandler;

public interface NewEmptyPlanEventHandler extends EventHandler {

	public void onNewEmptyPlanButtonClicked(String careerCode);

}
