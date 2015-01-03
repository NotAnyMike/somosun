package com.uibinder.index.server.dao;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Group;

public class GroupDao {

	static{
		ObjectifyService.register(Group.class);
	}
}
