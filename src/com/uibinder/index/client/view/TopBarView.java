package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface TopBarView {
	
	public interface Presenter {
		void setNameOfThePage(String s);
		void setUserName(String s);
	  }
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	public abstract void setMainLabel(String string);
	public abstract void setUserName(String s);

}
