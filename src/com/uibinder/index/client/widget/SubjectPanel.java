package com.uibinder.index.client.widget;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubjectPanel extends VerticalPanel {
	
	@Override
	public void add(Widget w){
		if(getWidgetCount() == 0){
			super.insert(w, getWidgetCount());
		}else{
			super.insert(w, getWidgetCount() -1);
		}
	}

	@Override
	public void insert (Widget w, int beforeIndex){
		if (beforeIndex == getWidgetCount()) {
			beforeIndex--;
		}
		super.insert(w, beforeIndex);
	}
}
