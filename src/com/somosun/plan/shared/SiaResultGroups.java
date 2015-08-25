package com.somosun.plan.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Group;

@SuppressWarnings("serial")
public class SiaResultGroups extends SiaResult implements Serializable {
	
	private List<Group> groupList = new ArrayList<Group>();

	public void setList(List<Group> groupList) {
		this.groupList = groupList;
	}
	
	public List<Group> getGroups(){
		return groupList;
	}

}
