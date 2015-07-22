package com.uibinder.client.index.view;

import com.google.gwt.user.client.ui.Widget;

public interface LoadingView {
	
	public interface Presenter
	{
		public void setLabel(String s);
	}
	
	Widget asWidget();
	public void setLabel(String s);
	public void setPresenter(Presenter p);

}
