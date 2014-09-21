package com.uibinder.index.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GenerateAcademicHistoryFromStringEvent extends GwtEvent<GenerateAcademicHistoryFromStringEventHandler> {

	public static Type<GenerateAcademicHistoryFromStringEventHandler> TYPE = new Type<GenerateAcademicHistoryFromStringEventHandler>();
	private String academicHistory = null;

	public GenerateAcademicHistoryFromStringEvent(String academicHistory) {
		this.academicHistory = academicHistory;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GenerateAcademicHistoryFromStringEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GenerateAcademicHistoryFromStringEventHandler handler) {
		handler.DoGenerateAcademicHistoryFromString(academicHistory);
	}

}
