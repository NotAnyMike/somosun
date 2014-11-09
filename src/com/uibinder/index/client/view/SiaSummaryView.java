package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface SiaSummaryView {
	
	interface Presenter{
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	void setFoundationCredits(int approved, int necessary);
	void setDisciplinaryCredits(int approved, int necessary);
	void setFreeElectionCredits(int approved, int necessary);
	void setLevelingCredits(int approved, int necessary);
	void setGPA(int x);
	void setAvance(int x);
	void setApprovedCredits(int x);
	void setAdditionalyCredits(int x);

}
