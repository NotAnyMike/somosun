package com.uibinder.index.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.uibinder.index.shared.control.Group;

@SuppressWarnings("serial")
public class SiaResultGroups extends SiaResult implements Serializable {
	
	private List<Group> groupList = new ArrayList<Group>();

}
