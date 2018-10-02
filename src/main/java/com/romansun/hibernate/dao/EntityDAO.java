package com.romansun.hibernate.dao;

import java.util.Collection;

public interface EntityDAO<T> {
	
	void add(T e) throws Exception;
	
	void update(T e) throws Exception;
	
	void delete(T e) throws Exception;
	
	T getById(long id) throws Exception;
	
	Collection<T> getAll() throws Exception;
}
