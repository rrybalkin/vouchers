package com.rrybalkin.hibernate.dao;

import java.util.List;

public interface EntityDAO<T> {
	
	void add(T e) throws Exception;
	
	void update(T e) throws Exception;
	
	void delete(T e) throws Exception;
	
	T getById(long id) throws Exception;
	
	List<T> getAll() throws Exception;
}
