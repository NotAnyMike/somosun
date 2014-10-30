package com.uibinder.index.client.dnd;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.widget.SemesterWidget;

public class SemesterDropController extends VerticalPanelDropController {
	
	SemesterWidget semester;

	public SemesterDropController(VerticalPanel dropTarget, SemesterWidget semester) {
		super(dropTarget);
		this.semester = semester;
	}
	
	@Override
	public void onDrop(DragContext context){
		Window.alert(String.valueOf(context.selectedWidgets.get(0).asWidget().getElement().getPropertyInt("credits")));
		semester.addCredits(1);
		super.onDrop(context);
	}

}
