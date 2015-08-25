package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.EventHandler;

public interface GradeChangeEventHandler extends EventHandler {
	public void onPlanChanges(String triggerer);
}
