package com.somosun.plan.server.dao;

public interface Dao<T> {

//	public Long save(T t);
	public Long generateId();
	public boolean delete(Long id);
	public T getById(Long id);
	
	
}
