package com.rrybalkin.hibernate.dao;

import com.rrybalkin.hibernate.entity.Association;

public interface AssociationDAO extends EntityDAO<Association> {
	
	int getCount() throws Exception;
}
