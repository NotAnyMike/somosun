package com.uibinder.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.uibinder.shared.control.Block;
import com.uibinder.shared.control.Group;

@SuppressWarnings("serial")
public class SiaResultGroups extends SiaResult implements Serializable {
	
	private List<Group> groupList = new ArrayList<Group>();

	public void setList(List<Group> groupList) {
		this.groupList = groupList;
	}

}
