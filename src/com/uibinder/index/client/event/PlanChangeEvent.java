package com.uibinder.index.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlanChangeEvent extends GwtEvent<PlanChangeEventHandler> {
	
	private String triggerer;
	public static Type<PlanChangeEventHandler> TYPE = new Type<PlanChangeEventHandler>();
	
	public PlanChangeEvent(String triggerer){
		this.triggerer = triggerer;
	}
	
	@Override
	public Type<PlanChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PlanChangeEventHandler handler) {
		handler.onPlanChanges(triggerer);
	}

}
