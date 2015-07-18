package com.uibinder.index.client.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.event.PlanChangeEvent;
import com.uibinder.index.client.event.PlanChangeEventHandler;

public class SubjectPanel extends VerticalPanel implements HasChangeHandlers {
	
	@Override
	public void add(Widget w){

		if(getWidgetCount() == 0){
			super.insert(w, getWidgetCount());
		}else{
			super.insert(w, getWidgetCount() -1);
		}
		
		if (w.getStyleName().contains("subjectBox")) {
			
			GWT.log("subject registered");
			
			this.fireEvent(new PlanChangeEvent("SubjectAdd"));
			
			w.addHandler(new PlanChangeEventHandler(){

				@Override
				public void onPlanChanges(String triggerer) {
					fireEvent(new PlanChangeEvent(triggerer));
				}
				
			}, PlanChangeEvent.TYPE);
			
		}
		
	}

	@Override
	public void insert (Widget w, int beforeIndex){
		if (w.getStyleName().contains("subjectBox")) {
			
			GWT.log("subject registered");
			
			this.fireEvent(new PlanChangeEvent("SubjectAdd"));
			
			w.addHandler(new PlanChangeEventHandler(){

				@Override
				public void onPlanChanges(String triggerer) {
					fireEvent(new PlanChangeEvent(triggerer));
				}
				
			}, PlanChangeEvent.TYPE);
			
			
		}

		if (beforeIndex == getWidgetCount()) {
			beforeIndex--;
		}
		super.insert(w, beforeIndex);
	}


	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}
}
