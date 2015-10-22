package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoadPlanEvent extends GwtEvent<LoadPlanEventHandler> {

	public static Type<LoadPlanEventHandler> TYPE = new Type<LoadPlanEventHandler>();
	private String planId = null;
	
	public LoadPlanEvent(String planId){
		this.planId = planId;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoadPlanEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoadPlanEventHandler handler) {
		handler.onLoadPlanEventHandler(planId);
	}

	public String getPlanId() {
		return planId;
	}

}
