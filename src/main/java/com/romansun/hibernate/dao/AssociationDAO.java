package com.romansun.hibernate.dao;

import com.romansun.hibernate.entity.Association;

public interface AssociationDAO extends EntityDAO<Association> {
	
	public int getCount() throws Exception;
}
