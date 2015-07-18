package com.uibinder.index.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlanChangeEventHandler extends EventHandler {
	public void onPlanChanges(String triggerer);
}
