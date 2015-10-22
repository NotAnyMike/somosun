package com.somosun.plan.client.index.event;

import com.google.gwt.event.shared.GwtEvent;

public class AboutUsEvent extends GwtEvent<AboutUsEventHandler> {
	
	public static Type<AboutUsEventHandler> TYPE = new Type<AboutUsEventHandler>();
	
	private String msg;
	
	public void GetMsg(String msg){
		this.msg = msg;
	}

	@Override
	public Type<AboutUsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AboutUsEventHandler handler) {
		handler.onAboutUs(this);
	}
	
	public String getText(){
		return msg;
	}

}
