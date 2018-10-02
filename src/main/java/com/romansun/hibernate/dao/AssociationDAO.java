package com.romansun.hibernate.dao;

import com.romansun.hibernate.entity.Association;

public interface AssociationDAO extends EntityDAO<Association> {
	
	int getCount() throws Exception;
}
