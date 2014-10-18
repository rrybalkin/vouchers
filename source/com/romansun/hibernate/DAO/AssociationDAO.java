package com.romansun.hibernate.DAO;

import com.romansun.hibernate.logic.Association;

public interface AssociationDAO extends EntityDAO<Association> {
	
	public int getCount() throws Exception;
}
