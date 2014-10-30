package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface TopBarView {
	
	public interface Presenter {
		void setNameOfThePage(String s);
		void setUserName(String s);
		void setNameOfThePage(String s, String author);
	  }
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	public abstract void setUserName(String s);
	public abstract void setMainLabel(String string);
	public abstract void setMainLabelTitle(String s);

}
