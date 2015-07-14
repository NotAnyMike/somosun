package com.uibinder.index.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class NewEmptyPlanEvent extends GwtEvent<NewEmptyPlanEventHandler> {

	private String careerCode;
	public static Type<NewEmptyPlanEventHandler> TYPE = new Type<NewEmptyPlanEventHandler>(); 
	
	public NewEmptyPlanEvent(String careerCode){
		this.careerCode = careerCode;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NewEmptyPlanEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewEmptyPlanEventHandler handler) {
		handler.onNewEmptyPlanButtonClicked(careerCode);
	}

	public String getCareerCode() {
		return careerCode;
	}
}
