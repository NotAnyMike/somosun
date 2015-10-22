package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.GwtEvent;

public class ContinueDefaultCareerEvent extends GwtEvent<ContinueDefaultCareerEventHandler>{
	
	public static Type<ContinueDefaultCareerEventHandler> TYPE = new Type<ContinueDefaultCareerEventHandler>();
	private String careerCode;
	
	public ContinueDefaultCareerEvent(String careerCode){
		this.careerCode = careerCode;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContinueDefaultCareerEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ContinueDefaultCareerEventHandler handler) {
		handler.onContinueDefaultCareerButtonClicked(careerCode);
	}

}
