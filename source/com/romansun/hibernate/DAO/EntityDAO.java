package com.romansun.hibernate.DAO;

import java.util.Collection;

public interface EntityDAO<T> {
	
	public void add(T e) throws Exception;
	
	public void update(T e) throws Exception;
	
	public void delete(T e) throws Exception;
	
	public T getById(long id) throws Exception;
	
	public Collection<T> getAll() throws Exception;
}
