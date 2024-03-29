package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.GwtEvent;
import com.somosun.plan.shared.control.Plan;

public class SavePlanAsDefaultEvent extends GwtEvent<SavePlanAsDefaultHandler> {
	
	public static Type<SavePlanAsDefaultHandler> TYPE = new Type<SavePlanAsDefaultHandler>();
	private Plan plan;

	public SavePlanAsDefaultEvent(Plan plan){
		this.plan = plan;
	}

	@Override
	public Type<SavePlanAsDefaultHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SavePlanAsDefaultHandler handler) {
		handler.onSavePlanAsDefault(getPlan());
	}
	
	public Plan getPlan(){
		return plan;
	}

}
