package com.uibinder.index.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GradeChangeEvent extends GwtEvent<GradeChangeEventHandler> {
	
	private String triggerer;
	public static Type<GradeChangeEventHandler> TYPE = new Type<GradeChangeEventHandler>();
	
	public GradeChangeEvent(String triggerer){
		this.triggerer = triggerer;
	}
	
	@Override
	public Type<GradeChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GradeChangeEventHandler handler) {
		handler.onPlanChanges(triggerer);
	}

}
