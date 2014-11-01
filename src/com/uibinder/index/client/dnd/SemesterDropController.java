package com.uibinder.index.client.dnd;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SemesterWidget;

public class SemesterDropController extends VerticalPanelDropController {
	
	private final SemesterWidget semester;
	private final PlanPresenter planPresenter;

	public SemesterDropController(VerticalPanel dropTarget, SemesterWidget semester, PlanPresenter planPresenter) {
		super(dropTarget);
		this.planPresenter = planPresenter;
		this.semester = semester;
	}
	
	private void notifyPresenter(String code){
		//TODO fill this thing up
		planPresenter.updateSemesters(code,semester);
	}
	
	@Override
	public void onDrop(DragContext context){
		super.onDrop(context);
		notifyPresenter(context.selectedWidgets.get(0).getElement().getAttribute("code"));
		//semester.addCredits(Integer.valueOf(context.selectedWidgets.get(0).getElement().getAttribute("credits")));
	}
	
	@Override
	public void onEnter(DragContext context){
		super.onEnter(context);
		//semester.addCredits(Integer.valueOf(context.selectedWidgets.get(0).getElement().getAttribute("credits")));
	}

	@Override
	public void onLeave(DragContext context){
		super.onLeave(context);
		//semester.removeCredits(Integer.valueOf(context.selectedWidgets.get(0).getElement().getAttribute("credits")));
	}
}
